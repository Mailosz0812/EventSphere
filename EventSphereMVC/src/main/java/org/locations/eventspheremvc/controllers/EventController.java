package org.locations.eventspheremvc.controllers;

import DTOs.*;
import org.locations.eventspheremvc.services.*;
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

    public EventController(eventRequestService eventService, imageRequestService imageService, subscribeRequestService userService, poolRequestService poolService) {
        this.eventService = eventService;
        this.imageService = imageService;
        this.subscribeService = userService;
        this.poolService = poolService;
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
        try{
            pools = poolService.getPools(event.getNAME());
            subState = subscribeService.subscribeState(event.getNAME(), authContextProvider.getMail());
            altText = imageService.getImageByEventName(eName).getAltText();
        }catch (HttpClientErrorException e){
        }
        model.addAttribute("purchaseRequestDTO",new purchaseRequestDTO());
        model.addAttribute("event",event);
        model.addAttribute("altText",altText);
        model.addAttribute("subState",subState);
        model.addAttribute("pools",pools);
        return "eventUserDetailsView";
    }
}
