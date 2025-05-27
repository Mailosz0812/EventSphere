package org.locations.eventsphere.Controllers;

import DTOs.imageEventDTO;
import jakarta.validation.Valid;
import org.locations.eventsphere.Services.imageService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/image")
public class imageController {
    private final imageService imageService;

    public imageController(org.locations.eventsphere.Services.imageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping
    public imageEventDTO uploadImage(@RequestBody @Valid imageEventDTO imgEvent) {
        return imageService.addImage(imgEvent);
    }
    @DeleteMapping
    public void deleteImage(@RequestParam("eName") String eName){
        System.out.println(eName);
        imageService.deleteImage(eName);
    }
    @GetMapping
    public imageEventDTO getImageDTO(@RequestParam("eName") String eName){
        System.out.println(eName);
        return imageService.getImageByEvent(eName);
    }
}
