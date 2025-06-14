package org.locations.eventsphere.Controllers;

import DTOs.poolDTO;
import DTOs.poolDetailsDTO;
import jakarta.websocket.server.PathParam;
import org.locations.eventsphere.Services.poolService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pool")
public class poolController {

    private final poolService poolService;

    public poolController(org.locations.eventsphere.Services.poolService poolService) {
        this.poolService = poolService;
    }

    @PostMapping
    public poolDTO createPool(@RequestBody poolDTO poolDTO){
        return poolService.createPool(poolDTO);
    }
    @GetMapping
    public poolDTO getPool(Long poolID){
        return poolService.getPoolById(poolID);
    }
    @GetMapping("/all")
    public List<poolDetailsDTO> getPoolsByEvent(@RequestParam("name") String eName){
        return poolService.getPools(eName);
    }
    @DeleteMapping("/{poolID}")
    public void deletePool(@PathVariable("poolID") Long poolID){
        poolService.deletePool(poolID);
    }
    @PutMapping
    public void updatePool(@RequestBody poolDTO poolDTO){
        poolService.updatePool(poolDTO);
    }
}


