package org.locations.eventspheremvc.Exceptions;

import DTOs.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.JsonParseException;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.locations.eventspheremvc.services.categoryRequestService;
import org.locations.eventspheremvc.services.eventRequestService;
import org.locations.eventspheremvc.services.poolRequestService;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    private categoryRequestService categoryService;
    private poolRequestService poolService;
    private eventRequestService eventService;

    public GlobalExceptionHandler(categoryRequestService categoryService, poolRequestService poolService, eventRequestService eventService) {
        this.categoryService = categoryService;
        this.poolService = poolService;
        this.eventService = eventService;
    }

    @ExceptionHandler(PasswordException.class)
    public String handlePasswordException(PasswordException e, Model model, HttpServletRequest request, RedirectAttributes attributes) {
        String uri = request.getRequestURI();
        if(uri.equals("/register")){
            model.addAttribute("Response",e.getMessage());
            model.addAttribute("userRegister",e.getTarget());
            return "register";
        } else if (uri.equals("/reset")) {
            attributes.addFlashAttribute("error",e.getMessage());
            return "redirect:/reset?token="+request.getParameter("token");
        }else{
            return "errorView";
        }
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleMethodArgumentNotValid(MethodArgumentNotValidException e,Model model,HttpServletRequest request){
        String uri = request.getRequestURI();
        System.out.println(uri);
        if(uri.equals("/register")) {
            userRegisterDTO user = (userRegisterDTO) e.getBindingResult().getTarget();
            model.addAttribute("userRegister", user);
            Map<String, String> errors = getStringStringMap(e);
            model.addAttribute("errors", errors);
            return "register";
        } else if (uri.equals("/admin/message")) {
            messageDTO messageDTO = (DTOs.messageDTO) e.getBindingResult().getTarget();
            model.addAttribute("message",messageDTO);
            Map<String, String> errors = getStringStringMap(e);
            model.addAttribute("errors", errors);
            return "sendMessageView";
        } else if (uri.equals("/organizer")) {
            eventDTO event = (eventDTO) e.getBindingResult().getTarget();
            Map<String,String> errors = new HashMap<>();
            List<categoryDTO> categories = categoryService.getCategories();
            for (FieldError error: e.getFieldErrors()) {
                errors.put(error.getField(),error.getDefaultMessage());
            }
            model.addAttribute("errors",errors);
            model.addAttribute("event",event);
            model.addAttribute("categories",categories);
            return "eventForm";
        }else if(uri.equals("/event/update")){
            eventDTO event = (eventDTO) e.getBindingResult().getTarget();
            Map<String, String> errors = getStringStringMap(e);
            model.addAttribute("errors",errors);
            model.addAttribute("event",event);
            model.addAttribute("iEvent",new imageEventDTO());
            return "modifyEventView";
        }else if(uri.contains("/pool")){
            poolDTO poolDTO = (poolDTO) e.getBindingResult().getTarget();
            Map<String,String> errors = getStringStringMap(e);
            assert poolDTO != null;
            List<poolDetailsDTO> pools = poolService.getPools(poolDTO.getEventName());
            System.out.println(poolDTO.getEventName());
            model.addAttribute("errors",errors);
            model.addAttribute("poolDTO", poolDTO);
            model.addAttribute("name",poolDTO.getEventName());
            model.addAttribute("pools",pools);
            return "ticketView";
        } else if (uri.equals("/image/update")) {
            imageEventDTO iEvent = (imageEventDTO) e.getBindingResult().getTarget();
            assert iEvent != null;
            Map<String,String> errors = getStringStringMap(e);
            eventDTO event = eventService.getEventDetails(iEvent.getEName());
            model.addAttribute("event",event);
            model.addAttribute("iEvent",iEvent);
            model.addAttribute("errors",errors);
            return "modifyEventView";
        }else if(uri.equals("/user/update")) {
            userDTO user = (userDTO) e.getBindingResult().getTarget();
            assert user != null;
            Map<String,String> errors = getStringStringMap(e);
            model.addAttribute("user",user);
            model.addAttribute("errors",errors);
            return "userUpdateView";
         }
        return "errorView";
    }

    private static Map<String, String> getStringStringMap(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : e.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return errors;
    }

    @ExceptionHandler(EmailException.class)
    public String handleMessagingException(EmailException e,HttpServletRequest request,Model model){
        model.addAttribute("message",e.getTarget());
        model.addAttribute("error",e.getMessage());
        String url = request.getHeader("Referer");
        System.out.println(url);
        return "redirect:"+url;
    }
    @ExceptionHandler(EventSphereMVCException.class)
    public String handleEventSphereMVC(EventSphereMVCException e, Model model){
        model.addAttribute("error",e.getMessage());
        return "errorView";
    }
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e,Model model){
        System.out.println(e.getMessage());
        model.addAttribute("error",e.getMessage());
        return "errorView";
    }
}
