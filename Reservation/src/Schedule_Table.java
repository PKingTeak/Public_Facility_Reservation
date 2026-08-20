import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

public class Schedule_Table {

    Map<String, ArrayList<Reservation>> schedule; // Date,Reservationinfo

    Schedule_Table() {
        schedule = new HashMap<String, ArrayList<Reservation>>();
    }

    // 해당 기관을 찾아서 정보 조회기능

    public void addReservation(String _date, User _user, Facility _Facility) {
        ArrayList<Reservation> arr = schedule.get(_date);
        if (arr == null) {
            arr = new ArrayList<Reservation>();
            Reservation res = new Reservation(_user, _Facility, _date); //객체 생성
            schedule.put(_date, arr);
            arr.add(res);
            System.out.print(res.getReservationFacilityName());
            return;
        }
        
        for(Reservation res : arr)
        {
           if(res.getReservationFacilityId() == _Facility.getFacilityId())
           {
            System.out.print("이미 예약된 기관입니다.");
            return;
           }
        }

        //예약가능 예약 객체생성
        Reservation res = new Reservation(_user, _Facility, _date);
        schedule.put(_date, arr);
        arr.add(res);
        
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

    public Boolean CheckDate(String _date)
    {
        if(schedule.containsKey(_date))
        {
            return false; //예약 불가
        }
        return true; //예약 가능
       
    }

    private ArrayList<Reservation> getAllListDate(String _date) {
        ArrayList<Reservation> outPutArr = new ArrayList<Reservation>();
        if (schedule.containsKey(_date)) {
            ArrayList<Reservation> arr = schedule.get(_date);
            for (Reservation res : arr) {
                outPutArr.add(res);
            }
        }

        return outPutArr;
    }

    public int getdateTableSize(String _date) {
        if (schedule.containsKey(_date)) {
            return schedule.get(_date).size();
        }
        return -1;
    }

}
