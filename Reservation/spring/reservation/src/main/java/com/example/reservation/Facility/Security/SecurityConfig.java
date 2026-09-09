package com.example.reservation.Facility.Security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean 
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
        .csrf(csrf-> csrf.disable())

			.authorizeHttpRequests((authorize) -> authorize
            .requestMatchers("/Input.html",
                "/users","/facility/**"
            ).permitAll()
             .requestMatchers("/admin/**").hasRole("ADMIN")

    .anyRequest().authenticated()
			
            )
            .formLogin(from -> from.permitAll());
            

	
		return http.build();
	}

   
}