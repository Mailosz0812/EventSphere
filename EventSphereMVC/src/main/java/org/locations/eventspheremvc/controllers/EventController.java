package org.locations.eventspheremvc.controllers;

import DTOs.*;
import org.locations.eventspheremvc.services.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;


@RequestMapping("/event")
@Controller
public class EventController {
    private final eventRequestService eventService;
    private final imageRequestService imageService;
    private final subscribeRequestService subscribeService;
    private final poolRequestService poolService;
    private final accountsRequestService accountService;

    public EventController(eventRequestService eventService, imageRequestService imageService, subscribeRequestService subscribeService,
                           poolRequestService poolService, accountsRequestService accountService) {
        this.eventService = eventService;
        this.imageService = imageService;
        this.subscribeService = subscribeService;
        this.poolService = poolService;
        this.accountService = accountService;
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
        List<poolDetailsDTO> pools = null;
        preCreatedUserDTO organizer = null;
        try{
            pools = poolService.getPools(event.getNAME());
            organizer = accountService.getOrganizerByEvent(eName);
            if(SecurityContextHolder.getContext().getAuthentication() != null) {
                subState = subscribeService.subscribeState(event.getNAME(), authContextProvider.getMail());
            }
            altText = imageService.getImageByEventName(eName).getAltText();
        }catch (HttpClientErrorException e){
            altText = "Sorry, organizer has not provide any image yet.";
        }
        model.addAttribute("purchaseRequestDTO",new purchaseRequestDTO());
        model.addAttribute("event",event);
        model.addAttribute("altText",altText);
        model.addAttribute("subState",subState);
        model.addAttribute("pools",pools);
        if(organizer != null) {
            System.out.println(organizer.getMail() + " " + organizer.getUsername());
        }else{
            System.out.println("jest nullem");
        }
        model.addAttribute("organizer",organizer);
        return "eventUserDetailsView";
    }
}
