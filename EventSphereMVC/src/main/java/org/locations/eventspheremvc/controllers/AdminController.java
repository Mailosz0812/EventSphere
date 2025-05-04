package org.locations.eventspheremvc.controllers;

import DTOs.preCreatedUserDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.locations.eventspheremvc.services.accountsRequestService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final accountsRequestService accountService;
    private PasswordEncoder encoder;

    public AdminController(accountsRequestService accountService, PasswordEncoder encoder) {
        this.accountService = accountService;
        this.encoder = encoder;
    }

    @GetMapping
    public String adminView(){
        return "adminView";
    }

    @GetMapping("/sys")
    public String registerAdminView(Model model){
        model.addAttribute("admin",new preCreatedUserDTO());
        return "adminRegister";
    }
    @PostMapping("/sys")
    public String registerAdmin(@ModelAttribute preCreatedUserDTO admin, Model model){
        try {
            String pass = encoder.encode("admin1");
            accountService.adminCreateUser(admin,"ADMIN",pass);
            model.addAttribute("admin",new preCreatedUserDTO());
            model.addAttribute("response","Administrator created successfully");
            return"adminRegister";
        }catch(HttpClientErrorException e){
            errorMessage(model,e.getResponseBodyAsString(),"admin",admin);
            return "adminRegister";
        }
    }
    @GetMapping("/organizers")
    public String organizersView(){
        return "organizersView";
    }
    @GetMapping("/register/organizer")
    public String registerOrganizerView(Model model){
        model.addAttribute("organizer",new preCreatedUserDTO());
        return "organizerRegister";
    }
    @PostMapping("register/organizer")
    public String registerOrganizer(@ModelAttribute preCreatedUserDTO organizer, Model model){
        try {
            String pass = encoder.encode("organizer1");
            accountService.adminCreateUser(organizer, "ORGANIZER",pass);
            model.addAttribute("organizer",new preCreatedUserDTO());
            model.addAttribute("response", "Organizer added successfully");
            return "organizerRegister";
        }catch (HttpClientErrorException e){
            errorMessage(model,e.getResponseBodyAsString(),"organizer", organizer);
            return "organizerRegister";
        }

    }
    @GetMapping("/register/user")
    public String registerUserView(Model model){
        model.addAttribute("user",new preCreatedUserDTO());
        return "userRegister";
    }

    @PostMapping("/register/user")
    public String registerUser(@ModelAttribute preCreatedUserDTO userDTO,Model model){
        try{
            String pass = encoder.encode("user1");
            accountService.adminCreateUser(userDTO,"USER",pass);
            model.addAttribute("user",new preCreatedUserDTO());
            model.addAttribute("response","User added successfully");
            return "userRegister";

        }catch (HttpClientErrorException e){
            errorMessage(model,e.getResponseBodyAsString(),"user",userDTO);
            return "userRegister";
        }

    }
    static void errorMessage(Model model, String s, String attribName, preCreatedUserDTO preCreatedUserDTO) {
        Map<String, String> errors = new HashMap<>();
        System.out.println(s);
        model.addAttribute(attribName, preCreatedUserDTO);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(s);
            if (root.isObject() && root.has("MAIL") || root.has("USERNAME") ) {
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
            model.addAttribute("response", s);
        }
    }
}
