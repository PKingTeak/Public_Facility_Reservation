import java.util.Map;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

public class Schedule_Table {

    Map<String, ArrayList<Reservation>> schedule; // Date,Reservationinfo
    static long totalScheduleNum = 1;

    Schedule_Table() {
        schedule = new HashMap<String, ArrayList<Reservation>>();
    }

    // 해당 기관을 찾아서 정보 조회기능

    public void addReservation(String _date, User _user, Facility _Facility, LocalTime _startTime, LocalTime _endTime) {
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

    public void cancelReservation(String _date,String _FacilityName,LocalTime _starTime,LocalTime _endTime)
    {
        if(schedule.containsKey(_date))
        {
            ArrayList<Reservation> arr = schedule.get(_date);

            for(Reservation res : arr)
            {
                if(res.getReservationFacilityName().equals(_FacilityName))
                {
                    Reservation.TimeSlot slot = res.getReservationTimeSlot(); 
                    if(slot.isEqual(_starTime,_endTime))
                    {
                        res.cancel();
            System.out.print("취소가 완료 되었습니다.");
                        
                    }
                }
            }
        }

        System.out.print("해당 스케쥴은 해당하는 예약이 존재하지 않습니다.");
        return;
    }

    public Reservation getReservation(String _date, long _reservationId) {
        if (schedule.containsKey(_date)) {
            ArrayList<Reservation> arr = schedule.get(_date);

            for (Reservation res : arr) {
                if (res.getReservationId() == _reservationId) {
                    return res;
                }
            }

        }
        return null;
        // 값 없음
    }
/*
public void cancelReservation(String _date, long _reservationId) {
    
ArrayList<Reservation> arr = schedule.get(_date);
if (arr == null) {
    return;
}

for (Reservation res : arr) {
    if (res.getReservationId() == _reservationId) {
        res.cancel();
        return;
    }
}

}

*/
 

    public int getdateTableSize(String _date) {
        if (schedule.containsKey(_date)) {
            return schedule.get(_date).size();
        }
        return -1;
    }

}
