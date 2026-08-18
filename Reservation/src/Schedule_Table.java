import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

public class Schedule_Table 
{

    Map<String, ArrayList<Reservation>> schedule; // Date,Reservationinfo

    Schedule_Table() 
    {
        schedule = new HashMap<String, ArrayList<Reservation>>();
    }

    //해당 기관을 찾아서 정보 조회기능
    

    public void addReservation(String _date, Reservation _reservation) 
    {
        ArrayList<Reservation> arr = schedule.get(_date);
        if (arr == null) 
        {
            arr = new ArrayList<Reservation>();
            schedule.put(_date, arr);
            arr.add(_reservation);
            return;
        }
        schedule.put(_date, arr);
        arr.add(_reservation);
    }

    public Reservation getReservation(String _date, long _facilityId) {
        if (schedule.containsKey(_date)) {
            ArrayList<Reservation> arr = schedule.get(_date);

            for (Reservation res : arr) {
                if (res.getReservationId() == _facilityId) {
                    return res;
                }
            }

        }
        return null;
        // 값 없음
    }

    public void cancelReservation(String _date, long _reservationId) 
    {

        ArrayList<Reservation> arr = schedule.get(_date);
        if(arr == null)
        {
            return;
        }



        
        for (Reservation res : arr) 
        {
        if (res.getReservationId() == _reservationId) 
        {
        res.cancel();
        return;
    }
}


    }

    public ArrayList<Reservation> getAllListDate(String _date) 
    {
        ArrayList<Reservation> outPutArr = new ArrayList<Reservation>();
        if (schedule.containsKey(_date)) 
        {
            ArrayList<Reservation> arr = schedule.get(_date);
            for (Reservation res : arr) {
                outPutArr.add(res);
            }
        }

        return outPutArr;
    }

    public int getdateTableSize(String _date) 
    {
        if (schedule.containsKey(_date)) 
        {
            return schedule.get(_date).size();
        }
        return -1;
    }

}
