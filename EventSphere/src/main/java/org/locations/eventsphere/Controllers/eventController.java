package org.locations.eventsphere.Controllers;

import DTOs.eventDTO;
import DTOs.imageEventDTO;
import DTOs.subscribeDTO;
import jakarta.validation.Valid;

import org.locations.eventsphere.Repositories.subscribeRepository;
import org.locations.eventsphere.Services.eventService;
import org.locations.eventsphere.Services.subscribeService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/event")
public class eventController {
    private final eventService eventService;

    public eventController(eventService eventService) {
        this.eventService = eventService;
    }
    @PostMapping
    public eventDTO organizeEvent(@RequestBody @Valid eventDTO eventDTO){
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
    @GetMapping("/{name}")
    public eventDTO getEventByName(@PathVariable(name = "name") String name){
        System.out.println(name);
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
    public List<eventDTO> getAllEvents(@RequestParam(value = "category",required = false) String category){
        return eventService.getEventsByCategory(category);
    }
    @GetMapping("/events/{name}")
    public List<eventDTO> getEventsByName(@PathVariable("name") String name){
        return eventService.getEventsByName(name);
    }

}
