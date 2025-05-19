package org.locations.eventspheremvc.Exceptions;

import DTOs.categoryDTO;
import DTOs.eventDTO;
import DTOs.messageDTO;
import DTOs.userRegisterDTO;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.locations.eventspheremvc.services.categoryRequestService;
import org.locations.eventspheremvc.services.eventRequestService;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    private categoryRequestService categoryService;

    public GlobalExceptionHandler(categoryRequestService categoryService) {
        this.categoryService = categoryService;
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
        if(uri.equals("/register")) {
            userRegisterDTO user = (userRegisterDTO) e.getBindingResult().getTarget();
            model.addAttribute("userRegister", user);
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : e.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            model.addAttribute("errors", errors);
            return "register";
        } else if (uri.equals("/admin/message")) {
            messageDTO messageDTO = (DTOs.messageDTO) e.getBindingResult().getTarget();
            model.addAttribute("message",messageDTO);
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : e.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            model.addAttribute("errors", errors);
            return "sendMessageView";
        } else if (uri.equals("/event")) {
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
            Map<String,String> errors = new HashMap<>();
            for (FieldError error: e.getFieldErrors()) {
                errors.put(error.getField(),error.getDefaultMessage());
            }
            model.addAttribute("errors",errors);
            model.addAttribute("event",event);
            return "modifyEventView";
        }
        return "errorView";
    }
    @ExceptionHandler(EmailException.class)
    public String handleMessagingException(EmailException e,HttpServletRequest request,Model model){
        model.addAttribute("message",e.getTarget());
        model.addAttribute("error",e.getMessage());
        String url = request.getHeader("Referer");
        return "redirect:"+url;
    }
}
