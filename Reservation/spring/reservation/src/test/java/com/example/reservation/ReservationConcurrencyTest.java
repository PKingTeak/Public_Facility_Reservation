package com.example.reservation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.reservation.Facilitys.Facility;
import com.example.reservation.Facilitys.User;
import com.example.reservation.Facilitys.DTO.ReservationRequest;
import com.example.reservation.Facilitys.Exception.DuplicateDataException;
import com.example.reservation.Facilitys.Repository.FacilityRepository;
import com.example.reservation.Facilitys.Repository.ReservationRepository;
import com.example.reservation.Facilitys.Repository.UserRepository;
import com.example.reservation.Facilitys.Service.ReservationService;

/**
 * 동시 예약 통합 테스트.
 *
 * 목(Mock)이 아니라 실제 DB(H2)와 실제 트랜잭션, 여러 스레드를 사용해서
 * "같은 시설, 같은 시간대에 동시에 예약 요청이 몰려도 1건만 저장되는지"를 검증한다.
 *
 * 핵심 원리: ReservationService.addReservation()은 시설 행을
 * SELECT ... FOR UPDATE(비관적 락)로 먼저 잡는다.
 * 그래서 같은 시설에 대한 예약 트랜잭션은 한 번에 하나씩 순서대로 실행되고,
 * 뒤에 들어온 요청은 앞 요청이 커밋한 예약을 보고 중복으로 거절된다.
 *
 * 트랜잭션이 실제로 커밋돼야 다른 스레드가 결과를 볼 수 있으므로
 * 이 테스트 클래스에는 @Transactional을 붙이지 않고, @AfterEach에서 직접 정리한다.
 */
@SpringBootTest
class ReservationConcurrencyTest {

    private static final int THREAD_COUNT = 10;

    @Autowired
    ReservationService reservationService;
    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    FacilityRepository facilityRepository;

    private final List<String> emails = new ArrayList<>();
    private long facilityId;

    @BeforeEach
    void setUp() {
        // 서로 다른 사용자 10명이 같은 시간대를 노리는 상황
        for (int i = 0; i < THREAD_COUNT; i++) {
            String email = "user" + i + "@test.com";
            userRepository.save(new User("사용자" + i, 20, email, "encoded-password"));
            emails.add(email);
        }
        Facility facility = facilityRepository.save(new Facility("동시성테스트체육관", "GYM", 50));
        facilityId = facility.getFacilityId();
    }

    @AfterEach
    void tearDown() {
        reservationRepository.deleteAll();
        userRepository.deleteAll();
        facilityRepository.deleteById(facilityId);
        emails.clear();
    }

    @DisplayName("같은 시간대에 10명이 동시에 예약하면 1건만 성공한다")
    @Test
    void onlyOneReservationSucceedsUnderConcurrentRequests() throws InterruptedException {
        LocalDate date = LocalDate.now().plusDays(1);
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch ready = new CountDownLatch(THREAD_COUNT); // 모든 스레드가 출발선에 섰는지
        CountDownLatch start = new CountDownLatch(1);            // 동시에 출발시키는 신호
        CountDownLatch done = new CountDownLatch(THREAD_COUNT);  // 모두 끝났는지

        AtomicInteger success = new AtomicInteger();
        AtomicInteger duplicate = new AtomicInteger();
        AtomicInteger unexpected = new AtomicInteger();

        for (String email : emails) {
            executor.submit(() -> {
                ready.countDown();
                try {
                    start.await();
                    reservationService.addReservation(request(date, 10, 12), email);
                    success.incrementAndGet();
                } catch (DuplicateDataException e) {
                    duplicate.incrementAndGet();
                } catch (Exception e) {
                    unexpected.incrementAndGet();
                    e.printStackTrace();
                } finally {
                    done.countDown();
                }
            });
        }

        ready.await();
        start.countDown(); // 10개 스레드 동시 출발
        done.await(30, TimeUnit.SECONDS);
        executor.shutdown();

        assertEquals(0, unexpected.get(), "락 타임아웃 등 예상하지 못한 예외가 없어야 한다");
        assertEquals(1, success.get(), "성공한 예약은 정확히 1건이어야 한다");
        assertEquals(THREAD_COUNT - 1, duplicate.get(), "나머지는 모두 중복 예약으로 거절돼야 한다");
        assertEquals(1, reservationRepository.getReservationByFacilityIdAndDate(facilityId, date).size(),
                "DB에도 1건만 저장돼야 한다");
    }

    private ReservationRequest request(LocalDate date, int startHour, int endHour) {
        ReservationRequest request = new ReservationRequest();
        request.setFacilityId(facilityId);
        request.setDate(date);
        request.setStartTime(LocalTime.of(startHour, 0));
        request.setEndTime(LocalTime.of(endHour, 0));
        return request;
    }
}
