package org.locations.eventspheremvc.controllers;

import DTOs.eventDTO;
import DTOs.imageEventDTO;
import jakarta.validation.Valid;
import org.locations.eventspheremvc.services.eventRequestService;
import org.locations.eventspheremvc.services.imageRequestService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/image")
public class ImageController {
    private final imageRequestService imageService;
    private final eventRequestService eventService;

    public ImageController(imageRequestService imageService, eventRequestService eventService) {
        this.imageService = imageService;
        this.eventService = eventService;
    }

    @PostMapping("/update")
    public String uploadImage(Model model, @ModelAttribute @Valid imageEventDTO iEventDTO, @RequestParam("file") MultipartFile multipartFile) throws IOException {
        try {
            imageService.uploadImage(multipartFile, iEventDTO);
            return "redirect:/event?name="+iEventDTO.getEName();
        }catch(HttpClientErrorException e){
            eventDTO event = eventService.getEventDetails(iEventDTO.getEName());
            model.addAttribute("event",event);
            model.addAttribute("iEvent",iEventDTO);
            model.addAttribute("iResponse",e.getResponseBodyAsString());
            return "modifyEventView";
        }
    }
    @PostMapping("/delete")
    public String deleteImage(@RequestParam("eName") String eName,Model model){
        try {
            System.out.println(eName);
            imageService.deleteImage(eName);
            return "redirect:/event/update?name=" + eName;
        }catch (HttpClientErrorException e){
            eventDTO event = eventService.getEventDetails(eName);
            model.addAttribute("event",event);
            model.addAttribute("iEvent",new imageEventDTO());
            model.addAttribute("iResponse",e.getResponseBodyAsString());
            return "modifyEventView";
        }
    }
    @GetMapping
    public ResponseEntity<byte[]> getImageBytes(@RequestParam("eName") String eName){
        try{
            imageEventDTO imageDTO = imageService.getImageByEventName(eName);
            if(imageDTO.getImageBytes() == null){
                System.out.println("no cos sie zjebalo");
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageDTO.getImageBytes());
        }catch(HttpClientErrorException e){
            System.out.println(e.getResponseBodyAsString());
            return ResponseEntity.ok(null);
        }
    }
}
