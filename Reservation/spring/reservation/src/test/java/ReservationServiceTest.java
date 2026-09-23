
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

import com.example.reservation.Facilitys.*;
import com.example.reservation.Facilitys.Reservation.ReservationStatus;
import com.example.reservation.Facilitys.DTO.ReservationRequest;
import com.example.reservation.Facilitys.Exception.DuplicateDataException;
import com.example.reservation.Facilitys.Exception.InvalidRequestException;
import com.example.reservation.Facilitys.Repository.ReservationRepository;
import com.example.reservation.Facilitys.Service.FacilityService;
import com.example.reservation.Facilitys.Service.ReservationService;
import com.example.reservation.Facilitys.Service.UserService;

@ExtendWith(MockitoExtension.class)

public class ReservationServiceTest {

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

    @DisplayName("중복 예약 방지 테스트")
    @Test
    void ReservationDuplicationTest() {
        // Given 테스트 상황
        String email = "test@test.com";
        long facilityId = 1;
        LocalDate date = LocalDate.of(2026, 9, 20);
        Reservation existingReservation = new Reservation(tUser, tFacility, date, LocalTime.of(10, 0),
                LocalTime.of(12, 0));
        ReservationRequest request = new ReservationRequest(); // 들어오는 예약 중복으로 예약 해야함
        request.setFacilityId(facilityId);
        request.setStartTime(LocalTime.of(10, 0));
        request.setEndTime(LocalTime.of(13, 0));
        request.setDate(date);

        // When
        when(tUserService.getUserByEmail(email)).thenReturn(tUser);
        when(tFacilityService.getFacilityInfoByIdWithLock(facilityId)).thenReturn(tFacility);
        when(tReservationService.getReservationsByFacilityAndDate(facilityId, date))
                .thenReturn(List.of(existingReservation));

        // When + Then 같이 사용해서 Return 반환 가능하다.
        assertThrows(DuplicateDataException.class, () -> tReservationService.addReservation(request, email));

        verify(tRepository, never()).save(any(Reservation.class));

    }

    @DisplayName("정상 예약 확인") // 등록 한번만 되는지 확인
    @Test
    void NormalReservationTest() {
        String email = "Test@test.com";
        User testUser = new User("테스트용", 10, email, "260922");
        long facilityId = 1;
        LocalDate date = LocalDate.of(2026, 9, 22);
      
        ReservationRequest request = new ReservationRequest();
        request.setDate(date);
        request.setEndTime(LocalTime.of(12, 0));
        request.setStartTime(LocalTime.of(10, 0));
        request.setFacilityId(facilityId);

        when(tUserService.getUserByEmail(email)).thenReturn(testUser);
        when(tFacilityService.getFacilityInfoByIdWithLock(facilityId)).thenReturn(tFacility);

        assertDoesNotThrow(() -> tReservationService.addReservation(request, email));

        verify(tRepository, times(1)).save(any(Reservation.class));
    }

    @DisplayName("타인 예약 취소 테스트")
    @Test
    void OtherReservationCancel() {

        // Given
        String otherEmail = "Test2@test.com";

        User ownerUser = mock(User.class);
        User otherUser = mock(User.class);

        when(ownerUser.getId()).thenReturn(1L);
        when(otherUser.getId()).thenReturn(2L);

        Facility facility = new Facility("월계체육관", "GYM", 50);

        LocalDate date = LocalDate.of(2026, 9, 22);
        long reservationId = 1L;

        Reservation reservation = new Reservation(
                ownerUser,
                facility,
                date,
                LocalTime.of(10, 0),
                LocalTime.of(12, 0));

        // DB에 reservationId=1인 예약이 존재한다고 가정
        when(tRepository.findById(reservationId))
                .thenReturn(Optional.of(reservation));

        // 현재 취소를 요청한 사용자는 otherUser라고 가정
        when(tUserService.getUserByEmail(otherEmail))
                .thenReturn(otherUser);

        // When + Then
        assertThrows(
                InvalidRequestException.class,
                () -> tReservationService.cancelReservation(reservationId, otherEmail));

        // 타인이 취소했으므로 기존 RESERVED 상태가 유지되어야 함
        assertEquals(
                ReservationStatus.RESERVED,
                reservation.getStatus());
    }

}
