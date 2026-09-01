
package com.example.reservation.Facility;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.reservation.Facility.DTO.ReservationRequest;
import com.example.reservation.Facility.Exception.DataNotFoundException;
import com.example.reservation.Facility.Exception.DuplicateDataException;
import com.example.reservation.Facility.Repository.ReservationRepository;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class ReservationService {

   // Map<LocalDate, ArrayList<Reservation>> schedule = new HashMap<LocalDate, ArrayList<Reservation>>(); // Date,Reservationinfo
// static long totalScheduleNum = 1;
   //이전에 사용하던 자료구조를 레퍼지토리로 변경
    
    //Repository
    private final ReservationRepository resRepository;

    //other tool
    private final UserService userService;
    private final FacilityService facilityService;

    public ReservationService(UserService _userService, FacilityService _FacilityService,
            ReservationRepository _Repository) {
        this.userService = _userService;
        this.facilityService = _FacilityService;
        this.resRepository = _Repository;
        
    }


    //#region 오버라이드 함수
    //해당 기관 정보 조회 Spring 연동
    public void addReservation(ReservationRequest _request) { 
        User user = userService.getUserById(_request.getUserId());
        Facility facility = facilityService.geFacilityInfoById(_request.getFailityId());
        System.out.println("받은 userId = " + _request.getUserId());
        if (user == null || facility == null) {
            throw new DataNotFoundException("user 혹은 facility가 null입니다");
        }


        List<Reservation> list = resRepository.getReservationByFacilityId(_request.getFailityId(), _request.getDate());
        
        for (Reservation reservation : list) 
        {
            if(reservation.getReservationTimeSlot().overlap(_request.getStartTime(),_request.getEndTime()))
            {
               throw new DuplicateDataException("해당 날짜는 중복된 예약입니다.");
            }
        }


        //모든 조건에 만족하지 않을때 
        Reservation res = new Reservation(user, facility, _request.getDate(),_request.getStartTime(), _request.getEndTime());
        resRepository.save(res);
    

        /*
        if (arr == null) // 배열이 존재하지 않을때 해당 날짜에 아무것도 없을때 
        {
            arr = new ArrayList<Reservation>();

            Reservation res = new Reservation(totalScheduleNum, user, facility, _request.getDate(),
                    _request.getStartTime(), _request.getEndTime());
            schedule.put(_request.getDate(), arr);
            arr.add(res);
            totalScheduleNum++;
            return;
        }

        for (Reservation res : arr) // 존재하면 시간 비교
        {
            if (res.getStatus() == ReservationStatus.CANCELLED) {
                continue;
            }
            
            if (res.getReservationFacilityId() == _request.getFailityId()) {
                if (res.getReservationTimeSlot().overlap(_request.getStartTime(), _request.getEndTime())) {
                    throw new DuplicateDataException("해당 시간은 이미 예약이 되어있습니다.");
                }
            }
        }
        
        // 존재하지만 예약 가능할때 타임 슬롯 비교
        Reservation res = new Reservation(totalScheduleNum, user, facility, _request.getDate(), _request.getStartTime(), _request.getEndTime());
        schedule.put(_request.getDate(), arr);
        arr.add(res);
        totalScheduleNum++;
        */
        
    }
    // 해당 기관을 찾아서 정보 조회기능
/*
public void addReservation(LocalDate _date, User _user, Facility _Facility, LocalTime _startTime,
LocalTime _endTime) {
    ArrayList<Reservation> arr = schedule.get(_date);
    if (arr == null) {
            arr = new ArrayList<Reservation>();
            Reservation res = new Reservation(totalScheduleNum, _user, _Facility, _date, _startTime, _endTime); // 객체 생성
            schedule.put(_date, arr);
            arr.add(res);
            
            totalScheduleNum++;
            return;
        }

        for (Reservation res : arr) {
            
            if (res.getStatus() == Reservation.ReservationStatus.CANCELLED) {
                continue;
            }
            
            if (res.getReservationFacilityId() == _Facility.getFacilityId()) {
                
            // 해당 시설 조회 + 예약 시간 비교
            if (res.getReservationTimeSlot().overlap(_startTime, _endTime)) {
                //throw new IllegalStateException("해당 시간은 이미 예약이 되어있습니다");
                    throw new DuplicateDataException("해당 시간은 이미 예약이 되어있습니다.");
                }
            }
        }
        
        // 타임 슬롯 비교
        Reservation res = new Reservation(totalScheduleNum, _user, _Facility, _date, _startTime, _endTime);
        schedule.put(_date, arr);
        arr.add(res);
        totalScheduleNum++;
        
    }
    */

    public void cancelReservation(LocalDate _date, String _FacilityName, LocalTime _startTime, LocalTime _endTime) {
       
        List<Reservation> reservations = resRepository.getReservationByFacilityId(facilityService.getFacilityIdByName(_FacilityName),_date);

        for(Reservation res : reservations)
        {
            if(res.getStartTime().equals(_startTime)&& res.getEndTime().equals(_endTime))
            {
                res.cancel();
                resRepository.save(res);
                return;
            }
        }


        throw new DataNotFoundException("해당 스케쥴은 해당하는 예약이 없습니다");
    }
//#endregion

    public Reservation getReservation(LocalDate _date, long _reservationId) {
        
        return resRepository.findById(_reservationId).orElseThrow(() -> new DataNotFoundException("해당하는 예약 아이디가 존재하지 않습니다."));
       // return null;
        // 값 없음
    }

    public void cancelReservation(LocalDate _date,long _reservationId)
    {
        if(resRepository.existsById(_reservationId))
        {
            Reservation res = resRepository.findById(_reservationId).get();
            res.cancel();
            resRepository.save(res);
        }
        else
        {
            throw new DataNotFoundException("해당하는 예약 아이디가 존재하지 않습니다.");
        }
    }

    public List<Reservation> getReservationsByDate(LocalDate _date)
    {
        return resRepository.getReservationByDate(_date);
    }
    /*
     * public void cancelReservation(String _date, long _reservationId) {
     * 
     * ArrayList<Reservation> arr = schedule.get(_date);
     * if (arr == null) {
     * return;
     * }
     * 
     * for (Reservation res : arr) {
     * if (res.getReservationId() == _reservationId) {
     * res.cancel();
     * return;
     * }
     * }
     * 
     * }
     * 
     */

    public int getDateTableSize(LocalDate _date) {
       
        return resRepository.getReservationByDate(_date).size();
    }

    public List<Reservation> getAllReservationByDate(LocalDate _date)
    {
        return resRepository.getReservationByDate(_date);
    }

}
