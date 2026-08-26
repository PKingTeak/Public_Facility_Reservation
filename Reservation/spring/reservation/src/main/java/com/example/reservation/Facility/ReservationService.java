
package com.example.reservation.Facility;

import com.example.reservation.ReservationApplication;
import java.util.Map;
import java.util.Collection;

import org.springframework.stereotype.Service;

import com.example.reservation.Facility.DTO.ReservationRequest;
import com.example.reservation.Facility.Reservation.ReservationStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

@Service
public class ReservationService {

    Map<LocalDate, ArrayList<Reservation>> schedule = new HashMap<LocalDate, ArrayList<Reservation>>(); // Date,Reservationinfo
    static long totalScheduleNum = 1;
    
    private final UserService userService;
    private final FacilityService facilityService;

    public ReservationService(UserService _userService, FacilityService _FacilityService,
            ReservationApplication reservationApplication) {
        this.userService = _userService;
        this.facilityService = _FacilityService;
        
    }


    //#region 오버라이드 함수
    //해당 기관 정보 조회 Spring 연동
    public void addReservation(ReservationRequest _request) { //오버라이드
        ArrayList<Reservation> arr = schedule.get(_request.getDate());
        User user = userService.getUserById(_request.getUserId());
        Facility facility = facilityService.geFacilityInfoById(_request.getFailityId());
        System.out.println("받은 userId = " + _request.getUserId());
        if (user == null || facility == null) {
            throw new IllegalStateException("user 혹은 facility가 null입니다");
        }
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
                    throw new IllegalStateException("해당 시간은 이미 예약이 되어있습니다.");
                }
            }
        }

        // 존재하지만 예약 가능할때 타임 슬롯 비교
        Reservation res = new Reservation(totalScheduleNum, user, facility, _request.getDate(), _request.getStartTime(), _request.getEndTime());
        schedule.put(_request.getDate(), arr);
        arr.add(res);
        totalScheduleNum++;

    }
    // 해당 기관을 찾아서 정보 조회기능

    public void addReservation(LocalDate _date, User _user, Facility _Facility, LocalTime _startTime,
            LocalTime _endTime) {
        ArrayList<Reservation> arr = schedule.get(_date);
        if (arr == null) {
            arr = new ArrayList<Reservation>();
            Reservation res = new Reservation(totalScheduleNum, _user, _Facility, _date, _startTime, _endTime); // 객체 생성
            schedule.put(_date, arr);
            arr.add(res);
            System.out.print(res.getReservationFacilityName());
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
                    throw new IllegalStateException("해당 시간은 이미 예약이 되어있습니다");
                }
            }
        }

        // 타임 슬롯 비교
        Reservation res = new Reservation(totalScheduleNum, _user, _Facility, _date, _startTime, _endTime);
        schedule.put(_date, arr);
        arr.add(res);
        totalScheduleNum++;

    }

    public void cancelReservation(LocalDate _date, String _FacilityName, LocalTime _starTime, LocalTime _endTime) {
        if (schedule.containsKey(_date)) {
            ArrayList<Reservation> arr = schedule.get(_date);

            for (Reservation res : arr) {
                if (res.getReservationFacilityName().equals(_FacilityName)) {
                    Reservation.TimeSlot slot = res.getReservationTimeSlot();
                    if (slot.isEqual(_starTime, _endTime)) {
                        res.cancel();
                        System.out.print("취소가 완료 되었습니다.");

                    }
                }
            }
        }

        System.out.print("해당 스케쥴은 해당하는 예약이 존재하지 않습니다.");
        return;
    }
//#endregion

    public Reservation getReservation(LocalDate _date, long _reservationId) {
        if (schedule.containsKey(_date)) {
            ArrayList<Reservation> arr = schedule.get(_date);

            for (Reservation res : arr) {
                if (res.getReservationId() ==  _reservationId) {
                    return res;
                }
            }

        }
        return null;
        // 값 없음
    }

    public void cancelReservation(LocalDate _date,long _reservationId)
    {
        for(Reservation res : schedule.get(_date))
        {
            if(res.getReservationId() == _reservationId)
            {
                res.cancel();
                return;
            }
        }
        throw new IllegalStateException("해당 예약 내역이 존재하지 않습니다");

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

    public int getdateTableSize(LocalDate _date) {
        if (schedule.containsKey(_date)) {
            return schedule.get(_date).size();
        }
        return -1;
    }

    public Collection<ArrayList<Reservation>> getAllReservation()
    {
        return schedule.values();
    }

    public ArrayList<Reservation> getReservationsByDate(LocalDate _date)
    {
        return schedule.get(_date);
    }
}
