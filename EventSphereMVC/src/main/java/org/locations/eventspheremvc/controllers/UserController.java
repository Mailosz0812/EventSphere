package org.locations.eventspheremvc.controllers;

import DTOs.userDTO;
import DTOs.userRegisterDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.locations.eventspheremvc.services.PasswordValidator;
import org.locations.eventspheremvc.services.accountsRequestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {
    private accountsRequestService userRequestService;
    private PasswordValidator passValid;

    public UserController(accountsRequestService userRequestService, PasswordValidator passValid) {
        this.userRequestService = userRequestService;
        this.passValid = passValid;
    }

    @GetMapping("/register")
    public String register(Model model){
        userRegisterDTO registerDTO = new userRegisterDTO();
        model.addAttribute("userRegister",registerDTO);
        return "register";
    }
    @PostMapping("/register")
    public String createUser(@ModelAttribute @Valid userRegisterDTO userRegister, Model model){
        try {
            userRegister.setPASSWORD(passValid.validatePassword(userRegister,userRegister.getPASSWORD()));
            userRequestService.createUser(userRegister, "USER");
            return "redirect:/login";
        }catch (HttpClientErrorException e){

            model.addAttribute("userRegister",userRegister);
            model.addAttribute("Response",e.getResponseBodyAsString());
            return "register";
        }
    }
    @GetMapping("/user")
    public String userDetails(@RequestParam("username") String username,Model model){
        try {
            userDTO user = userRequestService.getUserByUsername(username);
            model.addAttribute("user", user);
            return "userDetailsView";
        }catch (HttpClientErrorException e){
            return "errorView";
        }
    }
    @GetMapping("/")
    public String guestView(){
        return "index";
    }
    @GetMapping("/home")
    public String userView(){
        return "panelView";
    }
}
