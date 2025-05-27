package org.locations.eventspheremvc.controllers;

import DTOs.categoryDTO;
import DTOs.eventDTO;
import DTOs.imageEventDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.locations.eventspheremvc.services.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final imageRequestService imageService;
    private final userRequestService userService;

    public EventController(categoryRequestService categoryService, eventRequestService eventService, imageRequestService imageService, userRequestService userService) {
        this.categoryService = categoryService;
        this.eventService = eventService;
        this.imageService = imageService;
        this.userService = userService;
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
            String organizerMail = authContextProvider.getMail();
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
        List<eventDTO> events = eventService.getEvents(authContextProvider.getMail());
        model.addAttribute("eventsList",events);
        return "eventsView";
    }
    @GetMapping
    public String getEventDetails(Model model, @RequestParam("name") String name){
        eventDTO event = eventService.getEventDetails(name);
        model.addAttribute("event",event);
        return "eventDetailsView";
    }
    @GetMapping("/details")
    public String eventDetails(@RequestParam("eName") String eName,Model model){
        eventDTO event = eventService.getEventDetails(eName);
        String altText = null;
        boolean subState = false;
        try{
            subState = userService.subscribeState(event.getNAME(), authContextProvider.getMail());
            altText = imageService.getImageByEventName(eName).getAltText();
        }catch (HttpClientErrorException e){
            System.out.println(e.getResponseBodyAsString());
        }
        model.addAttribute("event",event);
        model.addAttribute("altText",altText);
        model.addAttribute("subState",subState);
        return "eventUserDetailsView";
    }
    @PostMapping("/subscribe")
    public String subscribeEvent(@RequestParam("name") String eName, HttpServletRequest request){
        String url = request.getHeader("Referer");
        userService.subscribeEvent(eName, authContextProvider.getMail());
        return "redirect:"+url;
    }
    @PostMapping("/unsubscribe")
    public String unsubscribeEvent(@RequestParam("name") String eName,HttpServletRequest request){
        String url = request.getHeader("Referer");
        userService.unsubscribeEvent(eName,authContextProvider.getMail());
        return "redirect:" + url;
    }
    @GetMapping("/update")
    public String updateEventView(Model model,@RequestParam("name") String name){
        eventDTO event = eventService.getEventDetails(name);
        model.addAttribute("event",event);
        model.addAttribute("iEvent",new imageEventDTO());
        return "modifyEventView";
    }
    @PostMapping("/update")
    public String updateEvent(Model model,@ModelAttribute @Valid eventDTO event){
        try{
            eventService.updateEvent(event);
            return "redirect:/event?name="+event.getNAME();
        }catch(HttpClientErrorException e){
            model.addAttribute("iEvent",new imageEventDTO());
            model.addAttribute("event",event);
            model.addAttribute("response",e.getResponseBodyAsString());
            return "modifyEventView";
        }
    }



}
