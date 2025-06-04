package org.locations.eventspheremvc.controllers;

import DTOs.poolDTO;
import DTOs.poolDetailsDTO;
import jakarta.validation.Valid;
import org.locations.eventspheremvc.services.eventRequestService;
import org.locations.eventspheremvc.services.poolRequestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
@RequestMapping("/pool")
public class poolController {
    private final poolRequestService poolService;
    private final eventRequestService eventService;

    public poolController(eventRequestService eventService, poolRequestService poolService) {
        this.eventService = eventService;
        this.poolService = poolService;
    }

    @PostMapping
    public String createPool(@ModelAttribute @Valid poolDTO poolDTO, Model model){
        List<poolDetailsDTO> pools = null;
        try{
            pools = poolService.getPools(poolDTO.getEventName());
        }catch (HttpClientErrorException e){
            model.addAttribute("response",e.getResponseBodyAsString());
            return "errorView";
        }
        try {
            poolService.createPool(poolDTO);
            return "redirect:/ticket/manage?name="+poolDTO.getEventName();
        }catch (HttpClientErrorException e){
            model.addAttribute(pools);
            model.addAttribute("poolDTO",poolDTO);
            model.addAttribute("response",e.getResponseBodyAsString());
            return "ticketView";
        }
    }
    @PostMapping("/delete")
    public String deletePool(@RequestParam("poolID") Long poolID, @RequestParam("name") String name, RedirectAttributes attributes,Model model){
        try{
            eventService.getEventDetails(name);
        }catch (HttpClientErrorException e){
            model.addAttribute("error",e.getResponseBodyAsString());
            return "errorView";
        }
        try {
            poolService.deletePool(poolID);
        }catch (HttpClientErrorException e){
            System.out.println("cos");
            attributes.addFlashAttribute("response",e.getResponseBodyAsString());
        }
        return "redirect:/ticket/manage?name="+name;
    }
    @PostMapping("/update")
    public String updatePool(@ModelAttribute @Valid poolDTO poolDTO){
        poolService.updatePool(poolDTO);
        return "redirect:/ticket/manage?name="+ poolDTO.getEventName();
    }
}
