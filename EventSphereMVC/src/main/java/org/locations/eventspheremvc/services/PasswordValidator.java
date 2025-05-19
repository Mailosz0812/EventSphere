package org.locations.eventspheremvc.services;

import DTOs.userRegisterDTO;
import org.locations.eventspheremvc.Exceptions.PasswordException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordValidator {
    private PasswordEncoder passwordEncoder;
    private String pattern = "^(?=.*[A-Z])(?=.*[!@#$%^&*(),.?\":{}|<>]).+$";

    public PasswordValidator(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public String validatePassword(userRegisterDTO user,String password){
        if(password.length() < 7){
            throw new PasswordException("Try longer password",user);
        }
        if(!password.matches(pattern)){
            throw new PasswordException("Password should contain special character and uppercase letter",user);
        }
        return passwordEncoder.encode(password);
    }
}
