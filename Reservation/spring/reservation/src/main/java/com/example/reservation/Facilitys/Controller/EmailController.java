package com.example.reservation.Facilitys.Controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.reservation.Facilitys.DTO.EmailVerifyRequest;
import com.example.reservation.Facilitys.Service.EmailService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController 
@RequestMapping("/email")
public class EmailController {
    
    private final EmailService service;
    EmailController(EmailService _service)
    {
        service = _service;
    }
    
    @PostMapping("/verify")
   public boolean verfyEmail(@RequestBody EmailVerifyRequest request)
   {
    return service.verifyCode(request.getEmail(),request.getVerificationCode());

   }

   @PostMapping("/send")
   public void sendVerifyCode(@RequestBody EmailVerifyRequest  request ) {
       service.sendVerificationCode(request.getEmail());
   }
   
    

   
}
