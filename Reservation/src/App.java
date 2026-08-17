import java.util.ArrayList;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception 
    {
        
        User user1 = new User(1,"PKT",25);
        User user2  = new User(0,"Faker", 11);
        Facility facility1 = new Facility(1,"월계 체육관","Gym",50);
        Facility facility2 = new Facility(2,"공릉 체육관","Gym",45);
        
        Scanner sc = new Scanner(System.in);

        Reservation reservation1 = new Reservation(1,user1,facility1,"2026-08-15");
        Reservation reservation2 = new Reservation(0, user2, facility2, "2026-08-15");

        Schedule_Table scheduleTable = new Schedule_Table();



        scheduleTable.addReservation(reservation1.getReservationDate(), reservation1);
        scheduleTable.addReservation(reservation2.getReservationDate(),reservation2);

        String date = "2026-08-15";
       
        ArrayList<Reservation> answer = scheduleTable.getAllListDate(date);

        for (var i : answer)
        {
            System.err.println(i.getReservationUserName());
            System.err.println(i.getReservationFacilityName());
        }
    }
}
