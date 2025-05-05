package org.locations.eventspheremvc.controllers;

import DTOs.userRegisterDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.locations.eventspheremvc.services.accountsRequestService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {
    private accountsRequestService userRequestService;
    private PasswordEncoder passwordEncoder;

    public UserController(accountsRequestService userRequestService, PasswordEncoder passwordEncoder) {
        this.userRequestService = userRequestService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String register(Model model){
        userRegisterDTO registerDTO = new userRegisterDTO();
        model.addAttribute("userRegister",registerDTO);
        return "register";
    }
    @PostMapping("/register")
    public String createUser(@ModelAttribute  userRegisterDTO userRegister,Model model){
        try {
            if(userRegister.getPASSWORD().length() > 7) {
                userRegister.setPASSWORD(passwordEncoder.encode(userRegister.getPASSWORD()));
            }
            else{
                userRegister.setPASSWORD(" ");
            }
                model.addAttribute("Response", userRequestService.createUser(userRegister,"USER"));
                return "redirect:/login";
        }catch(HttpClientErrorException e){
            errorMessage(model, e,userRegister);
            return "register";
        }
    }

    static void errorMessage(Model model, HttpClientErrorException e, userRegisterDTO user) {
        Map<String, String> errors = new HashMap<>();
        String s = e.getResponseBodyAsString();
        System.out.println(s);
        model.addAttribute("userRegister",user);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(s);
            if (root.isObject() && root.has("MAIL") || root.has("NAME") || root.has("SURNAME") || root.has("USERNAME") || root.has("PASSWORD")) {
                root.fields().forEachRemaining(field -> {
                    String fieldName = field.getKey();
                    System.out.println(fieldName);
                    String errorMessage = field.getValue().asText();
                    System.out.println(errorMessage);
                    errors.put(fieldName,errorMessage);
                });
                model.addAttribute("errors",errors);
            }
        } catch (Exception jsonException) {
            model.addAttribute("Response", s);
        }
    }

    @GetMapping("/")
    public String guestView(){
        return "index";
    }
    @GetMapping("/home")
    public String userView(){
        return "userView";
    }
}
