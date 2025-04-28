package org.locations.eventspheremvc.Exceptions;

import DTOs.userRegisterDTO;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

@ControllerAdvice
public class GlobalExceptionHandler {
//    @ExceptionHandler(HttpClientErrorException.class)
//    public String handleHttpClientErrorException(HttpClientErrorException e, Model model){
//        model.addAttribute("error", e.getResponseBodyAsString());
//        return "register";
//    }
//
//    @ExceptionHandler(RestClientException.class)
//    public String handleRestClientException(RestClientException e,Model model){
//        model.addAttribute("error",e.getMessage());
//        model.addAttribute("userRegister", new userRegisterDTO());
//        return "register";
//    }

}
