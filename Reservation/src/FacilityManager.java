import java.util.HashMap;

//해당 클래스를 통해서 실제 사용자들이 편리한 이름을 사용하여 Id를 조회하여 변경을 알려주고 관리하는 역할
public class FacilityManager {
    private static int FacilityNums; // 총 갯수
    HashMap<Long, Facility> facilityMaps = new HashMap<>();

    public void setFacility(Facility _facility) {
        // 시설만 하여 자동으로 Id를 기입하는것
    }

    public void setFacility(long _id, Facility _Facility) {
        if (facilityMaps.containsKey(_id)) {
            throw new IllegalStateException("이미 존재하는 _id입니다.");
        }
        facilityMaps.put(_id, _Facility);
        FacilityNums++;
    }

    public void removeFacility(long _id)
    {
        if(!facilityMaps.containsKey(_id))
        {
            throw new IllegalStateException("해당 아이디는 존재하지 않습니다.");
        }
        facilityMaps.remove(_id);
    }
    //해당 키값만 없앳기 때문에 다른곳에서 해당 시설을 참고하고있으면 메모리 공간에 계속 존재할수 있음 

    public long getFacilityIdByName(String _FacilityName) {
        for (Facility fa : facilityMaps.values()) {
            if (fa.getFacilityName().equals(_FacilityName)) {
                return fa.getFacilityId();
            }
        }

        return -1;
    }


    public String getFacilityNameById(long _id)
    {
        if(facilityMaps.containsKey(_id))
        {
            return facilityMaps.get(_id).getFacilityName();
        }


        return null;
    }
}
