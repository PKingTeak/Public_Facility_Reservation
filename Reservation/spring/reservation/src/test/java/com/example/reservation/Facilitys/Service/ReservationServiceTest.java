package com.example.reservation.Facilitys.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.reservation.Facilitys.Facility;
import com.example.reservation.Facilitys.Reservation;
import com.example.reservation.Facilitys.Reservation.ReservationStatus;
import com.example.reservation.Facilitys.User;
import com.example.reservation.Facilitys.DTO.ReservationRequest;
import com.example.reservation.Facilitys.Exception.DuplicateDataException;
import com.example.reservation.Facilitys.Exception.InvalidRequestException;
import com.example.reservation.Facilitys.Repository.ReservationRepository;

/**
 * ReservationService 단위 테스트 (DB 없이 Mockito로 의존성 대체).
 * 정상 / 예외 / 경계값 케이스로 나눠 예약 규칙을 검증한다.
 * 실제 DB와 여러 스레드를 쓰는 동시성 검증은 ReservationConcurrencyTest에 있다.
 */
@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    private static final String EMAIL = "test@test.com";
    private static final long FACILITY_ID = 1L;
    // 테스트 날짜를 고정 값으로 두면 시간이 지나 "지난 날짜" 규칙에 걸린다 → 항상 내일로 계산
    private static final LocalDate DATE = LocalDate.now().plusDays(1);

    @Mock
    UserService tUserService;
    @Mock
    FacilityService tFacilityService;
    @Mock
    ReservationRepository tRepository;

    @Mock
    User tUser;
    @Mock
    Facility tFacility;

    @InjectMocks
    ReservationService tReservationService;

    // ───────────── 정상 케이스 ─────────────

    @DisplayName("정상 예약은 한 번 저장된다")
    @Test
    void normalReservation() {
        givenUserAndFacility();

        assertDoesNotThrow(() -> tReservationService.addReservation(request(10, 12), EMAIL));

        verify(tRepository, times(1)).save(any(Reservation.class));
    }

    @DisplayName("[경계값] 기존 예약이 끝나는 시각에 바로 시작하는 예약은 허용된다 (10~12 다음 12~14)")
    @Test
    void adjacentSlotIsAllowed() {
        givenUserAndFacility();
        givenExisting(reservation(10, 12));

        assertDoesNotThrow(() -> tReservationService.addReservation(request(12, 14), EMAIL));

        verify(tRepository, times(1)).save(any(Reservation.class));
    }

    @DisplayName("취소된 예약 시간대는 다시 예약할 수 있다")
    @Test
    void cancelledSlotCanBeReservedAgain() {
        givenUserAndFacility();
        Reservation cancelled = reservation(10, 12);
        cancelled.cancel();
        givenExisting(cancelled);

        assertDoesNotThrow(() -> tReservationService.addReservation(request(10, 12), EMAIL));

        verify(tRepository, times(1)).save(any(Reservation.class));
    }

    // ───────────── 예외 케이스 ─────────────

    @DisplayName("시간이 겹치는 예약은 거절된다 (10~12 예약 중 10~13 요청)")
    @Test
    void overlappingReservationIsRejected() {
        givenUserAndFacility();
        givenExisting(reservation(10, 12));

        assertThrows(DuplicateDataException.class,
                () -> tReservationService.addReservation(request(10, 13), EMAIL));

        verify(tRepository, never()).save(any(Reservation.class));
    }

    @DisplayName("[경계값] 시작 시간과 종료 시간이 같으면 거절된다 (400)")
    @Test
    void zeroLengthSlotIsRejected() {
        givenUserAndFacility();

        assertThrows(InvalidRequestException.class,
                () -> tReservationService.addReservation(request(10, 10), EMAIL));

        verify(tRepository, never()).save(any(Reservation.class));
    }

    @DisplayName("종료 시간이 시작 시간보다 빠르면 거절된다 (400)")
    @Test
    void reversedSlotIsRejected() {
        givenUserAndFacility();

        assertThrows(InvalidRequestException.class,
                () -> tReservationService.addReservation(request(12, 10), EMAIL));
    }

    @DisplayName("지난 날짜는 예약할 수 없고, DB(락)까지 가지 않고 바로 거절된다")
    @Test
    void pastDateIsRejected() {
        ReservationRequest request = request(10, 12);
        request.setDate(LocalDate.now().minusDays(1));

        assertThrows(InvalidRequestException.class,
                () -> tReservationService.addReservation(request, EMAIL));

        verifyNoInteractions(tFacilityService, tRepository);
    }

    @DisplayName("본인이 아닌 예약은 취소할 수 없고 상태가 유지된다")
    @Test
    void otherUserCannotCancel() {
        String otherEmail = "other@test.com";
        User owner = mock(User.class);
        User other = mock(User.class);
        when(owner.getId()).thenReturn(1L);
        when(other.getId()).thenReturn(2L);

        Reservation reservation = new Reservation(owner, tFacility, DATE, LocalTime.of(10, 0), LocalTime.of(12, 0));
        when(tRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(tUserService.getUserByEmail(otherEmail)).thenReturn(other);

        assertThrows(InvalidRequestException.class,
                () -> tReservationService.cancelReservation(1L, otherEmail));
        assertEquals(ReservationStatus.RESERVED, reservation.getStatus());
    }

    @DisplayName("이미 취소된 예약을 다시 취소하면 거절된다")
    @Test
    void cancelTwiceIsRejected() {
        when(tUser.getId()).thenReturn(1L);
        Reservation reservation = reservation(10, 12);
        when(tRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(tUserService.getUserByEmail(EMAIL)).thenReturn(tUser);

        tReservationService.cancelReservation(1L, EMAIL);

        assertThrows(DuplicateDataException.class,
                () -> tReservationService.cancelReservation(1L, EMAIL));
        assertEquals(ReservationStatus.CANCELLED, reservation.getStatus());
    }

    // ───────────── TimeSlot 겹침 판정 경계값 ─────────────

    @DisplayName("[경계값] TimeSlot 겹침 판정: 끝과 시작이 맞닿으면 겹치지 않는다")
    @Test
    void timeSlotOverlapBoundaries() {
        Reservation.TimeSlot slot = reservation(10, 12).getReservationTimeSlot();

        assertFalse(slot.overlap(LocalTime.of(8, 0), LocalTime.of(10, 0)), "앞에 맞닿음");
        assertFalse(slot.overlap(LocalTime.of(12, 0), LocalTime.of(13, 0)), "뒤에 맞닿음");
        assertTrue(slot.overlap(LocalTime.of(9, 0), LocalTime.of(10, 1)), "1분 겹침");
        assertTrue(slot.overlap(LocalTime.of(11, 0), LocalTime.of(11, 30)), "안쪽에 포함");
        assertTrue(slot.overlap(LocalTime.of(9, 0), LocalTime.of(13, 0)), "바깥에서 감쌈");
    }

    // ───────────── 헬퍼 ─────────────

    private void givenUserAndFacility() {
        when(tUserService.getUserByEmail(EMAIL)).thenReturn(tUser);
        when(tFacilityService.getFacilityInfoByIdWithLock(FACILITY_ID)).thenReturn(tFacility);
    }

    private void givenExisting(Reservation existing) {
        // 서비스가 실제로 호출하는 Repository 메서드를 직접 스텁한다
        when(tRepository.getReservationByFacilityIdAndDate(FACILITY_ID, DATE)).thenReturn(List.of(existing));
    }

    private Reservation reservation(int startHour, int endHour) {
        return new Reservation(tUser, tFacility, DATE, LocalTime.of(startHour, 0), LocalTime.of(endHour, 0));
    }

    private ReservationRequest request(int startHour, int endHour) {
        ReservationRequest request = new ReservationRequest();
        request.setFacilityId(FACILITY_ID);
        request.setDate(DATE);
        request.setStartTime(LocalTime.of(startHour, 0));
        request.setEndTime(LocalTime.of(endHour, 0));
        return request;
    }
}
