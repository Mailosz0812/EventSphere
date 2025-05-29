package org.locations.eventspheremvc.controllers;

import DTOs.poolDTO;
import DTOs.poolDetailsDTO;
import jakarta.validation.Valid;
import org.locations.eventspheremvc.services.poolRequestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;


@Controller
@RequestMapping("/pool")
public class poolController {
    private poolRequestService poolService;

    public poolController(poolRequestService poolService) {
        this.poolService = poolService;
    }

    @PostMapping
    public String createPool(@ModelAttribute @Valid poolDTO poolDTO, Model model){
        List<poolDetailsDTO> pools = null;
        System.out.println("cos");
        try{
            pools = poolService.getPools(poolDTO.getEventName());
        }catch (HttpClientErrorException e){
            System.out.println(e.getResponseBodyAsString());
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
    public String deletePool(@RequestParam("poolID") Long poolID, @RequestParam("name") String name){
        poolService.deletePool(poolID,name);
        return "redirect:/ticket/manage?name="+name;
    }
    @PostMapping("/update")
    public String updatePool(@ModelAttribute @Valid poolDTO poolDTO){
        poolService.updatePool(poolDTO);
        return "redirect:/ticket/manage?name="+ poolDTO.getEventName();
    }
}
