package org.locations.eventspheremvc.controllers;

import DTOs.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.locations.eventspheremvc.services.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final AdminRequestService adminService;
    private final PasswordResetReqService passResetService;
    private final EmailService emailService;
    private final accountsRequestService userService;
    private final eventRequestService eventService;

    public AdminController(AdminRequestService adminService, PasswordResetReqService passResetService, EmailService emailService, accountsRequestService userService, eventRequestService eventService) {
        this.adminService = adminService;
        this.passResetService = passResetService;
        this.emailService = emailService;
        this.userService = userService;
        this.eventService = eventService;
    }

    @GetMapping
    public String userView(Model model){
        List<userDTO> weekUsers = adminService.usersOfWeek();
        List<eventDTO> weekEvents = adminService.getWeekEvents();
        model.addAttribute("weekUsers",weekUsers);
        model.addAttribute("weekEvents",weekEvents);
        String userNum = adminService.getUsersCount("USER");
        String organizerNum = adminService.getUsersCount("ORGANIZER");
        String eventNum = eventService.countEvent();
        model.addAttribute("usersCount",userNum);
        model.addAttribute("organizerCount",organizerNum);
        model.addAttribute("eventCount",eventNum);
        return "panelView";
    }
    @GetMapping("/sys")
    public String registerAdminView(Model model){
        model.addAttribute("admin",new preCreatedUserDTO());
        return "adminRegister";
    }
    @PostMapping("/sys")
    public String registerAdmin(@ModelAttribute preCreatedUserDTO admin, Model model){
        try {
            adminService.adminCreateUser(admin,"ADMIN");
            String token = generateToken(admin);
            emailService.sendPasswordLink(admin.getMail(),token);
            model.addAttribute("admin",new preCreatedUserDTO());
            model.addAttribute("response","Administrator created successfully");
            return "adminRegister";
        }catch(HttpClientErrorException e){
            errorMessage(model,e.getResponseBodyAsString(),"admin",admin);
            return "adminRegister";
        }
    }
    @GetMapping("/organizers")
    public String organizersView(Model model){
        List<userDTO> userDTOs = adminService.getUsersByRole("ORGANIZER");
        model.addAttribute("userList",userDTOs);
        return "usersView";
    }
    @GetMapping("/users")
    public String usersView(Model model){
        List<userDTO> userDTOs = adminService.getUsersByRole("USER");
        model.addAttribute("userList",userDTOs);
        return "usersView";
    }
    @GetMapping("/admins")
    public String adminsView(Model model){
        List<userDTO> userDTOs = adminService.getUsersByRole("ADMIN");
        model.addAttribute("userList",userDTOs);
        return "usersView";
    }
    @GetMapping("/register/organizer")
    public String registerOrganizerView(Model model){
        model.addAttribute("organizer",new preCreatedUserDTO());
        return "organizerRegister";
    }
    @PostMapping("/register/organizer")
    public String registerOrganizer(@ModelAttribute preCreatedUserDTO organizer, Model model){
        try {
            adminService.adminCreateUser(organizer, "ORGANIZER");
            String token = generateToken(organizer);
            emailService.sendPasswordLink(organizer.getMail(),token);
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
            System.out.println("end point");
            adminService.adminCreateUser(userDTO,"USER");
            String token = generateToken(userDTO);
            emailService.sendPasswordLink(userDTO.getMail(),token);
            model.addAttribute("user",new preCreatedUserDTO());
            model.addAttribute("response","User added successfully");
            return "userRegister";
        }catch (HttpClientErrorException e){
            errorMessage(model,e.getResponseBodyAsString(),"user",userDTO);
            return "userRegister";
        }

    }
    @GetMapping("/message")
    public String sendMessage(Model model,@RequestParam("username") String username){
        userDTO user = userService.getUserByUsername(username);
        messageDTO attributeValue = new messageDTO();
        attributeValue.setTo(user.getMail());
        model.addAttribute("message", attributeValue);
        return "sendMessageView";
    }
    @PostMapping("/message")
    public String sendMessage(@ModelAttribute @Valid messageDTO message,Model model){
        emailService.sendMessage(message);
        messageDTO attributeValue = new messageDTO();
        attributeValue.setTo(message.getTo());
        model.addAttribute("message", attributeValue);
        model.addAttribute("response","Message sent successfully");
        return "sendMessageView";
    }

    private String generateToken(preCreatedUserDTO userDTO) {
        String token = UUID.randomUUID().toString();
        PasswordTokenDTO tokenDTO = new PasswordTokenDTO();
        tokenDTO.setMail(userDTO.getMail());
        tokenDTO.setToken(token);
        tokenDTO.setExpireDate(LocalDateTime.now().plusMinutes(30));
        passResetService.saveToken(tokenDTO);
        return token;
    }
    @PostMapping("/block")
    public String blockUser(String mail, HttpServletRequest request){
        try {
            String url = request.getHeader("Referer");
            adminService.blockUser(mail);
            return "redirect:"+url;
        }catch (HttpClientErrorException e){
            System.out.println(e.getResponseBodyAsString());
            return "errorView";
        }
    }
    @GetMapping("/events")
    public String getEvents(Model model){
        List<eventDTO> events = eventService.getAllEvents();
        model.addAttribute("eventsList",events);
        return "eventsView";
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
