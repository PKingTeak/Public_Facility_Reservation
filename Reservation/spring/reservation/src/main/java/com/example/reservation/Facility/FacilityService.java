package com.example.reservation.Facility;

import org.springframework.stereotype.Service;

import com.example.reservation.Facility.Exception.DataNotFoundException;
import com.example.reservation.Facility.Repository.FacilityRepository;


import java.util.Collection;

@Service
//해당 클래스를 통해서 실제 사용자들이 편리한 이름을 사용하여 Id를 조회하여 변경을 알려주고 관리하는 역할
public class FacilityService  {

    private final FacilityRepository facilityRepository;


    
    FacilityService(FacilityRepository _FacilityRepository) 
    {
        this.facilityRepository = _FacilityRepository;

        if(facilityRepository.count() == 0)
        {

            InitFacility();
        }
    

    }

    private void InitFacility()
    {
        Facility facility = new Facility("월계체육관","GYM", 50);
        Facility facility2 = new Facility("월계수영장", "SwimmingPool", 100);
        Facility facility3 = new Facility("월계축구장", "Soccer", 20);

        setFacility(facility);
        setFacility(facility2);
        setFacility(facility3);
    }

    public void setFacility(Facility _Facility) { // 시설등록
        facilityRepository.save(_Facility);
    }

    public void removeFacility(long _id) // 해당 시설이 폐쇄되었을때
    {
       
        facilityRepository.deleteById(_id);
    }
    // 해당 키값만 없앳기 때문에 다른곳에서 해당 시설을 참고하고있으면 메모리 공간에 계속 존재할수 있음

    public Facility getFacilityByName(String _facilityName) {
       
        Facility facility =  facilityRepository.findByName(_facilityName).orElseThrow(() -> new DataNotFoundException("해당하는 시설이 존재하지 않습니다."));
    
        return facility;
    }

    public Long getFacilityIdByName(String _facilityName)
    {
        return facilityRepository.findByName(_facilityName).orElseThrow(() -> new DataNotFoundException("해당하는 시설이 존재하지 않습니다.")).getFacilityId();
    }

    

    

    public String getFacilityNameById(long _id) {
       return facilityRepository.findById(_id).orElseThrow(() -> new DataNotFoundException("해당하는 ID 가 존재하지 않습니다.")).getFacilityName();
    }

    public Facility geFacilityInfoById(long _id)
    {
        return facilityRepository.findById(_id).orElseThrow(() -> new DataNotFoundException("해당하는 id가 존재하지 않습니다."));
    }


    public Collection<Facility> getAllFacitiy()
    {
        return facilityRepository.findAll();
    }
}
