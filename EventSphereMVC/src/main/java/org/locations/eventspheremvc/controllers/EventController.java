package org.locations.eventspheremvc.controllers;

import DTOs.categoryDTO;
import DTOs.eventDTO;
import jakarta.validation.Valid;
import org.locations.eventspheremvc.Security.CustomUserDetails;
import org.locations.eventspheremvc.services.categoryRequestService;
import org.locations.eventspheremvc.services.eventRequestService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;


@RequestMapping("/event")
@Controller
public class EventController {
    private final categoryRequestService categoryService;
    private final eventRequestService eventService;

    public EventController(categoryRequestService categoryService, eventRequestService eventService) {
        this.categoryService = categoryService;
        this.eventService = eventService;
    }

    @GetMapping("/organize")
    public String organizeEventView(Model model){
        List<categoryDTO> categories = categoryService.getCategories();
        model.addAttribute("categories",categories);
        model.addAttribute("event",new eventDTO());
        return "eventForm";
    }
    @PostMapping
    public String organizeEvent(Model model, @ModelAttribute @Valid eventDTO event){

        List<categoryDTO> categories = categoryService.getCategories();
        try{
            String organizerMail = getOrganizerMail();
            if(organizerMail == null){
                return "errorView";
            }
            event.setOrganizerMail(organizerMail);
            eventService.createEvent(event);
            model.addAttribute("event",new eventDTO());
            model.addAttribute("categories",categories);
            return "eventForm";
        }catch (HttpClientErrorException e){
            System.out.println(e.getResponseBodyAsString());
            model.addAttribute("event",event);
            model.addAttribute("categories",categories);
            model.addAttribute("response",e.getResponseBodyAsString());
            return "eventForm";
        }

    }
    @GetMapping("/events")
    public String getEventsView(Model model){
        List<eventDTO> events = eventService.getEvents(getOrganizerMail());
        model.addAttribute("eventsList",events);
        return "eventsView";
    }
    @GetMapping
    public String getEventDetails(Model model, @RequestParam("name") String name){
        eventDTO event = eventService.getEventDetails(name);
        model.addAttribute("event",event);
        return "eventDetailsView";
    }
    @GetMapping("/update")
    public String updateEventView(Model model,@RequestParam("name") String name){
        eventDTO event = eventService.getEventDetails(name);
        model.addAttribute("event",event);
        return "modifyEventView";
    }
    @PostMapping("/update")
    public String updateEvent(Model model,@ModelAttribute @Valid eventDTO event){
        try{
            eventService.updateEvent(event);
            return "redirect:/event?name="+event.getNAME();
        }catch(HttpClientErrorException e){
            model.addAttribute("event",event);
            model.addAttribute("response",e.getResponseBodyAsString());
            return "modifyEventView";
        }
    }
    private String getOrganizerMail(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null && auth.isAuthenticated()){
            return auth.getName();
        }
        return null;
    }

}
