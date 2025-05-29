package org.locations.eventsphere.Controllers;

import DTOs.eventDTO;
import DTOs.subscribeDTO;
import org.locations.eventsphere.Services.subscribeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscribe")
public class subscribeController {
    private final subscribeService subService;

    public subscribeController(subscribeService subService) {
        this.subService = subService;
    }
    @PostMapping
    public void subscribeEvent(@RequestBody subscribeDTO subDTO){
        subService.subscribeEvent(subDTO);
    }
    @DeleteMapping
    public void unsubscribeEvent(@RequestBody subscribeDTO subDTO){
        System.out.println(subDTO.getEventName() + subDTO.getUserMail());
        subService.unsubscribeEvent(subDTO);
    }
    @GetMapping
    public boolean subscribeState(@RequestParam("name") String name, @RequestParam("mail") String mail){
        return subService.getSubscribeState(name,mail);
    }
    @GetMapping("/count")
    public int countSubscribe(@RequestParam("mail") String mail){
        return subService.countSubscribeEvent(mail);
    }
    @GetMapping("/feed")
    public List<eventDTO> subscribedEventsFeed(@RequestParam("mail") String mail){
        return subService.getEventsFeed(mail);
    }
    @GetMapping("/incoming")
    public List<eventDTO> incomingEvents(@RequestParam("mail") String mail){
        return subService.getIncomingEvents(mail);
    }
}
