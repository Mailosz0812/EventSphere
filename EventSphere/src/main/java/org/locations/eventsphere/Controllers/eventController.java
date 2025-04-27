package org.locations.eventsphere.Controllers;

import DTOs.eventDTO;
import jakarta.validation.Valid;
import org.locations.eventsphere.Services.eventService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/event")
public class eventController {
    private eventService eventService;
    public eventController(org.locations.eventsphere.Services.eventService eventService) {
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
}
