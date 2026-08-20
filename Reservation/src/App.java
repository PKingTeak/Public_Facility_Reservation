import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Scanner;

public class App {

    public static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void main(String[] args) throws Exception {
        // 초기화

        FacilityManager Fmanager = new FacilityManager(); // 이미 값은 생성
        Schedule_Table scheduleTable = new Schedule_Table();
        Scanner sc = new Scanner(System.in);

        for (int i = 0; i < 3; i++) {

            // 사용자 정보 입력
            System.out.print("본인 인적사항을 적어주세요 : (이름,나이 순으로 적어주세요");
            String sinput = sc.nextLine();
            int ageinput = sc.nextInt();
            sc.nextLine();
            User user1 = new User(sinput, ageinput);

            clearConsole();

            

            System.out.print("예약을 원하시면 1번 취소를 원하시면 2번");
            String intInput = sc.nextLine();
            if(intInput.equals("1"))
            {

                System.out.print("예약할 날짜를 적어주세요/ YYYY-MM_DD");
                String reDate = sc.nextLine();
                
                System.out.print("예약할 기관명을 적어주세요");
                sinput = sc.nextLine();
                System.out.print("예약 시간을 적어주세요 /시작시간~끝나는시간");
                
                String timeInput = sc.nextLine();
                String[] slice = timeInput.split("~");
                LocalTime starTime = LocalTime.parse(slice[0]);
                LocalTime endTime = LocalTime.parse(slice[1]);
                
                long resId = Fmanager.getFacilityIdByName(sinput);
                Facility resfacility = Fmanager.geFacilityInfoById(resId);
                scheduleTable.addReservation(reDate, user1, resfacility, starTime, endTime);
            }
            else
            {
                System.out.print("예약할 날짜를 적어주세요/ YYYY-MM_DD");
                String reDate = sc.nextLine();
                
                // 조회
                
                System.out.print("예약한 기관명을 적어주세요");
                sinput = sc.nextLine();
                System.out.print("예약 시간을 적어주세요 /시작시간~끝나는시간");
                String timeInput = sc.nextLine();
                String[] slice = timeInput.split("~");
                LocalTime starTime = LocalTime.parse(slice[0]);
                LocalTime endTime = LocalTime.parse(slice[1]);
                
                scheduleTable.cancelReservation(reDate,sinput,starTime,endTime);
            }

        }

    }

}
