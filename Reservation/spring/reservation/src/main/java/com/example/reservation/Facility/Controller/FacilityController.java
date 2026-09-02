
package com.example.reservation.Facility.Controller;

import java.util.Collection;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.reservation.Facility.Facility;
import com.example.reservation.Facility.FacilityService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



//HTTP에서 요청을 처리 및 상호작용
@RestController
@RequestMapping("/facility")
public class FacilityController {
    private final FacilityService facilityservice;

    public FacilityController(FacilityService _FacilityController)
    {
        facilityservice = _FacilityController;  
    }

    @GetMapping
    public Collection<Facility> getAllFacility()
    {
        return facilityservice.getAllFacitiy();
    }

    @GetMapping("/{id}")
    public String getMethodName(@PathVariable long id) {
        return facilityservice.getFacilityNameById(id);
    }
    
  


    
}
