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

        // 사용자 정보 입력
        System.out.print("본인 인적사항을 적어주세요 : (이름,나이 순으로 적어주세요");
        String sinput = sc.nextLine();
        int ageinput = sc.nextInt();
        sc.nextLine();
        User user1 = new User(011, sinput, ageinput);

        clearConsole();

        System.out.print("예약할 날짜를 적어주세요/ YYYY-MM_DD");
        String reDate = sc.nextLine();

        // 조회
        if (scheduleTable.CheckDate(reDate)) {

            System.out.print("예약할 기관명을 적어주세요");
            sinput = sc.nextLine();

            long resId = Fmanager.getFacilityIdByName(sinput);
            Facility resfacility = Fmanager.geFacilityInfoById(resId);
            scheduleTable.addReservation(reDate, user1, resfacility);

        } else {
            System.out.print("{reDate}날은 이미 예약되었습니다.");
        }

    }

}
