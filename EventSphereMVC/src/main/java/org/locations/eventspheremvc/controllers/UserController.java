package org.locations.eventspheremvc.controllers;

import DTOs.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import org.locations.eventspheremvc.services.*;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {
    private final accountsRequestService userRequestService;
    private final PasswordValidator passValid;
    private final eventRequestService eventService;
    private final imageRequestService imageService;
    private final categoryRequestService categoryService;
    private final userRequestService userService;
    private final ticketRequestService ticketService;

    public UserController(accountsRequestService userRequestService, PasswordValidator passValid, eventRequestService eventService,
                          imageRequestService imageService, categoryRequestService categoryService,
                          userRequestService userService, ticketRequestService ticketService) {
        this.userRequestService = userRequestService;
        this.passValid = passValid;
        this.eventService = eventService;
        this.imageService = imageService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.ticketService = ticketService;
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
            userRegister.setPassword(passValid.validatePassword(userRegister,userRegister.getPassword()));
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
    public String guestView(Model model, @RequestParam(value = "category", required = false, defaultValue = "ALL") String category,@RequestParam(name="name",required = false) String name){
        List<categoryDTO> categories = null;
        List<eventDTO> events = null;
        try{
            categories = categoryService.getCategories();
        }catch (HttpClientErrorException ignored){
        }
        if(name != null && !name.isEmpty()){
            events = eventService.getEventsByName(name);
        }else{
            events = eventService.getEventsByCategory(category);
        }
        Map<String, String> altTextMap = new HashMap<>();
        Map<String,Boolean> subStateMap = new HashMap<>();
        for (eventDTO event : events) {
            try {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                if(auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
                    boolean subState = userService.subscribeState(event.getNAME(), auth.getName());
                    subStateMap.put(event.getNAME(),subState);
                }
                imageEventDTO iEvent = imageService.getImageByEventName(event.getNAME());
                altTextMap.put(event.getNAME(),iEvent.getAltText());
            }catch (HttpClientErrorException e){
                System.out.println(e.getResponseBodyAsString());
            }
        }
        model.addAttribute("subStateMap",subStateMap);
        model.addAttribute("selectedCategory",category);
        model.addAttribute("categories",categories);
        model.addAttribute("altTextMap",altTextMap);
        model.addAttribute("eventList",events);
        return "index";
    }
    @GetMapping("/home/tickets")
    public String ticketsView(Model model, @RequestParam(name="eventPattern", required = false) String pattern,@RequestParam(name="status", required = false) String sortStatus,@RequestParam(name="date", required = false) String sortDate) throws JsonProcessingException {
        List<EventTicketsWrapper> tickets = null;
        if("all".equals(sortStatus)){
            sortStatus = null;
        }
        if("all".equals(sortDate)){
            sortDate = null;
        }
        try {
            tickets = ticketService.getTicketsByMail(authContextProvider.getMail(),sortStatus,sortDate,pattern);
        }catch (HttpClientErrorException e){
            System.out.println("ingored");
        }
        model.addAttribute("tickets",tickets);
        model.addAttribute("sortStatus",sortStatus);
        model.addAttribute("sortDate",sortDate);
         return "ticketsUserView";
    }
    @GetMapping("/home")
    public String userView(Model model){
        String mail = authContextProvider.getMail();
        int countEvent = userService.countSubscribedEvents(mail);
        List<eventDTO> eventsFeed = userService.subscribedEventsFeed(mail);
        List<eventDTO> incomingEvents = userService.incomingEvents(mail);
        int countTickets = ticketService.countUserTickets(mail);
        model.addAttribute("countSub",countEvent);
        model.addAttribute("incomingEvents",incomingEvents);
        model.addAttribute("eventsFeed",eventsFeed);
        model.addAttribute("ticketCount",countTickets);
        return "panelView";
    }
}
