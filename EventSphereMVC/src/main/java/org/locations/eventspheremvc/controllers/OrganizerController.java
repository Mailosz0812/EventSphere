package org.locations.eventspheremvc.controllers;

import DTOs.*;
import jakarta.validation.Valid;
import org.locations.eventspheremvc.services.authContextProvider;
import org.locations.eventspheremvc.services.categoryRequestService;
import org.locations.eventspheremvc.services.eventRequestService;
import org.locations.eventspheremvc.services.ticketRequestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Controller
@RequestMapping("/organizer")
public class OrganizerController {
    private final categoryRequestService categoryService;
    private final eventRequestService eventService;
    private final ticketRequestService ticketService;

    public OrganizerController(categoryRequestService categoryService, eventRequestService eventService, ticketRequestService ticketService) {
        this.categoryService = categoryService;
        this.eventService = eventService;
        this.ticketService = ticketService;
    }

    @GetMapping
    public String ticketsSummaryView(Model model) {
        String mail = authContextProvider.getMail();
        Integer count = ticketService.countTickets(mail);
        Integer sold = ticketService.countSoldTickets(mail);
        List<ticketsStatsDTO> ticketsStats = ticketService.getTicketsStats(mail);
        Integer left = count - sold;
        model.addAttribute("left",left);
        model.addAttribute("sold",sold);
        model.addAttribute("events",ticketsStats);
        return "panelView";
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
