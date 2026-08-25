package com.example.reservation;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello(@RequestParam String name) 
    {
        return "Hello" + name;
    }
    
}
