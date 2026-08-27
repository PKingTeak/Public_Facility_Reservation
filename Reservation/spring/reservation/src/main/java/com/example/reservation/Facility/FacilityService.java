package com.example.reservation.Facility;

import org.springframework.stereotype.Service;

import com.example.reservation.Facility.Exception.DataNotFoundException;
import com.example.reservation.Facility.Exception.GlobalExceptionHandler;

import java.util.HashMap;
import java.util.Collection;

@Service
//해당 클래스를 통해서 실제 사용자들이 편리한 이름을 사용하여 Id를 조회하여 변경을 알려주고 관리하는 역할
public class FacilityService  {
    HashMap<Long, Facility> facilityMaps = new HashMap<>();

    FacilityService()
    {
        Facility facility = new Facility(1, "월계체육관","GYM", 50);
        Facility facility2 = new Facility(2, "월계수영장", "SwimmingPool", 100);
        Facility facility3 = new Facility(3, "월계축구장", "Soccer", 20);

        setFacility(facility.getFacilityId(), facility);
        
        setFacility(facility2.getFacilityId(), facility2);
        setFacility(facility3.getFacilityId(), facility3);
    }

    public void setFacility(long _id, Facility _Facility) { // 시설등록
        if (facilityMaps.containsKey(_id)) {
            throw new IllegalStateException("이미 존재하는 _id입니다.");
        }
        facilityMaps.put(_id, _Facility);
    }

    public void removeFacility(long _id) // 해당 시설이 폐쇄되었을때
    {
        if (!facilityMaps.containsKey(_id)) {
           throw new DataNotFoundException("해당 시설은 폐쇄되었습니다.");
        }
        facilityMaps.remove(_id);
    }
    // 해당 키값만 없앳기 때문에 다른곳에서 해당 시설을 참고하고있으면 메모리 공간에 계속 존재할수 있음

    public long getFacilityIdByName(String _FacilityName) {
        for (Facility fa : facilityMaps.values()) {
            if (fa.getFacilityName().equals(_FacilityName)) {
                return fa.getFacilityId();
            }
        }

        return -1;
    }

    

    public String getFacilityNameById(long _id) {
        if (facilityMaps.containsKey(_id)) {
            return facilityMaps.get(_id).getFacilityName();
        }

        return null;
    }

    public Facility geFacilityInfoById(long _id)
    {
        if(facilityMaps.containsKey(_id))
        {
            return facilityMaps.get(_id);
        }
        
        return null;
    }


    public Collection<Facility> getAllFacitiy()
    {
        return facilityMaps.values();
    }
}
