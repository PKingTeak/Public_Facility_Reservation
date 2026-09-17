

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.reservation.Facilitys.*;
import com.example.reservation.Facilitys.DTO.ReservationRequest;
import com.example.reservation.Facilitys.Exception.DuplicateDataException;
import com.example.reservation.Facilitys.Repository.ReservationRepository;
import com.example.reservation.Facilitys.Service.FacilityService;
import com.example.reservation.Facilitys.Service.ReservationService;
import com.example.reservation.Facilitys.Service.UserService;

@ExtendWith (MockitoExtension.class)

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
    
    
    @DisplayName ("중복 예약 방지 테스트")
    @Test 
    void ReservationDuplicationTest()
    {
        //Given 테스트 상황
        String email = "test@test.com";
        long facilityId = 1;
        LocalDate date = LocalDate.of(2026,9,20);

        Reservation existingReservation= new Reservation(tUser,tFacility,date,LocalTime.of(10,0),LocalTime.of(12,0)); 


        ReservationRequest request = new ReservationRequest(); //들어오는 예약 중복으로 예약 해야함
        
        request.setFacilityId(facilityId);
        request.setStartTime(LocalTime.of(10,0));
        
        request.setEndTime(LocalTime.of(13,0));

        request.setDate(date);


        //When
        when(tUserService.getUserByEmail(email)).thenReturn(tUser);
        when(tFacilityService.geFacilityInfoById(facilityId)).thenReturn(tFacility);
        when(tReservationService.getReservationsByFacilityAndDate(facilityId, date)).thenReturn(List.of(existingReservation));
        

        //When + Then 같이 사용해서 Return 반환 가능하다. 
        assertThrows(DuplicateDataException.class,()->tReservationService.addReservation(request, email));


        verify(tRepository, never()).save(any(Reservation.class));
        
    }


}
