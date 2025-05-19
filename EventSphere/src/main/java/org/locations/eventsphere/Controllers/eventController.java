package org.locations.eventsphere.Controllers;

import DTOs.eventDTO;
import jakarta.validation.Valid;
import org.locations.eventsphere.Services.eventService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event")
public class eventController {
    private eventService eventService;
    public eventController(org.locations.eventsphere.Services.eventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public eventDTO organizeEvent(@RequestBody @Valid eventDTO eventDTO){
        System.out.println(eventDTO.getOrganizerMail());
        return eventService.organizeEvent(eventDTO);
    }
    @PutMapping
    public eventDTO updateEvent(@RequestBody @Valid eventDTO eventDTO){
        return eventService.updateEvent(eventDTO);
    }
    @GetMapping("/events")
    public List<eventDTO> getEvents(@RequestParam("mail") String mail){
        return eventService.getEventsByOrganizer(mail);
    }
    @GetMapping
    public eventDTO getEventByName(@RequestParam("name") String name){
        return eventService.eventDetails(name);
    }
    @GetMapping("/count")
    public String countEvents(){
        return eventService.countEvents();
    }
    @GetMapping("/recent")
    public List<eventDTO> recentEvents(){
        return eventService.getRecentEvents();
    }
    @GetMapping("/events/all")
    public List<eventDTO> getAllEvents(){
        return eventService.getEvents();
    }
}
