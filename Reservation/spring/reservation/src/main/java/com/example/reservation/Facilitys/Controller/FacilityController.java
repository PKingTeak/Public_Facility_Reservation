
package com.example.reservation.Facilitys.Controller;

import java.util.Collection;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.reservation.Facilitys.Facility;
import com.example.reservation.Facilitys.Service.FacilityService;

import jakarta.validation.Valid;

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
        return facilityservice.getAllFacilities();
    }

    @GetMapping("/{id}")
    public String getMethodName(@Valid@PathVariable long id) {
        return facilityservice.getFacilityNameById(id);
    }
    
  


    
}
