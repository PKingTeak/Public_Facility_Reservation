import java.util.Scanner;
import java.util.ArrayList;
public class App {

    public static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void main(String[] args) throws Exception {
        // 초기화
        int Id = 0;
        Facility facility1 = new Facility(1, "월계 체육관", "Gym", 50);
       
        Schedule_Table scheduleTable = new Schedule_Table();
        Scanner sc = new Scanner(System.in);

        // 입력
        System.out.print("본인 인적사항을 적어주세요 : (이름,나이 순으로 적어주세요");
        String sinput = sc.nextLine();
        int ageinput = sc.nextInt();
        sc.nextLine();
        User user1 = new User(sinput, ageinput);
        
        clearConsole();

        System.out.print("예약할 기관명을 적어주세요");
        sinput = sc.nextLine();

        System.out.print("예약할 날짜를 적어주세요/ YYYY-MM_DD");
        String reDate = sc.nextLine();

        ArrayList<Reservation> AllReservation =scheduleTable.getAllListDate(sinput);
        

        for(Reservation re : AllReservation)
        {
            if(re.getReservationDate() == reDate && re.getReservationFacilityName() == sinput)
            {
                //이미 해당 날짜에 예약됨
                System.out.print("해당 날짜는 이미 예약이 완료되었습니다.");
            }
        }
        
        Reservation res = new Reservation(Id, user1, facility1, reDate);
        scheduleTable.addReservation(reDate,res);
        


    }
}
