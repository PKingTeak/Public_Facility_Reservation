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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.reservation.Facilitys.Facility;
import com.example.reservation.Facilitys.Reservation;
import com.example.reservation.Facilitys.User;
import com.example.reservation.Facilitys.DTO.ReservationRequest;
import com.example.reservation.Facilitys.Exception.DuplicateDataException;
import com.example.reservation.Facilitys.Repository.FacilityRepository;
import com.example.reservation.Facilitys.Repository.ReservationRepository;
import com.example.reservation.Facilitys.Repository.UserRepository;
import com.example.reservation.Facilitys.Service.ReservationService;

/**
 * 서로 다른 사용자 100명이 같은 시설, 같은 날짜, 같은 시간을 동시에 예약할 때
 * 정확히 1건만 성공하는지 확인한다.
 *
 * Mock이 아니라 실제 DB 트랜잭션과 락이 동작해야 하므로 @SpringBootTest로 띄운다.
 * (기본은 H2, -Dspring.datasource.url=... 로 MySQL에서도 실행 가능)
 */
@SpringBootTest
class ReservationConcurrencyTest {

    private static final int REQUEST_COUNT = 100;
    private static final int THREAD_COUNT = 32;

    @Autowired
    ReservationService reservationService;
    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    FacilityRepository facilityRepository;
    @Autowired
    UserRepository userRepository;

    @AfterEach
    void cleanUp() {
        reservationRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("동시 예약 100건 중 1건만 성공한다")
    @Test
    void onlyOneReservationSucceedsUnderConcurrency() throws InterruptedException {
        // Given: 사용자 100명과 예약할 시설 1개
        Facility facility = facilityRepository.findByName("월계체육관").orElseThrow();
        List<String> emails = new ArrayList<>();
        for (int i = 0; i < REQUEST_COUNT; i++) {
            String email = "user" + i + "@test.com";
            userRepository.save(new User("사용자" + i, 20, email, "password"));
            emails.add(email);
        }
        LocalDate date = LocalDate.of(2026, 12, 1);

        AtomicInteger success = new AtomicInteger();
        AtomicInteger duplicate = new AtomicInteger();
        AtomicInteger otherError = new AtomicInteger();

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(REQUEST_COUNT);

        // When: 100건을 출발선에 세워 두었다가 한 번에 보낸다
        for (String email : emails) {
            executor.submit(() -> {
                try {
                    start.await();
                    reservationService.addReservation(
                            request(facility.getFacilityId(), date, LocalTime.of(10, 0), LocalTime.of(12, 0)),
                            email);
                    success.incrementAndGet();
                } catch (DuplicateDataException e) {
                    duplicate.incrementAndGet();
                } catch (Exception e) {
                    otherError.incrementAndGet();
                    System.out.println("[기타 오류] " + e);
                } finally {
                    done.countDown();
                }
            });
        }
        // 출발 신호: 대기 중이던 요청들이 동시에 예약을 시도한다
        start.countDown();
        done.await(60, TimeUnit.SECONDS);
        executor.shutdown();

        long reservedInDb = reservationRepository
                .getReservationByFacilityIdAndDate(facility.getFacilityId(), date).stream()
                .filter(r -> r.getStatus() == Reservation.ReservationStatus.RESERVED)
                .count();

        System.out.println("===== 동시 예약 테스트 결과 =====");
        System.out.println("요청 " + REQUEST_COUNT + "건 / 성공 " + success.get()
                + "건 / 중복 거절 " + duplicate.get() + "건 / 기타 오류 " + otherError.get() + "건");
        System.out.println("DB에 저장된 예약: " + reservedInDb + "건");

        // Then: 성공 1건, 나머지 99건은 중복으로 거절, DB에도 1건만 남는다
        assertEquals(1, success.get());
        assertEquals(REQUEST_COUNT - 1, duplicate.get());
        assertEquals(1, reservedInDb);
    }

    private ReservationRequest request(long facilityId, LocalDate date, LocalTime start, LocalTime end) {
        ReservationRequest request = new ReservationRequest();
        request.setFacilityId(facilityId);
        request.setDate(date);
        request.setStartTime(start);
        request.setEndTime(end);
        return request;
    }
}
