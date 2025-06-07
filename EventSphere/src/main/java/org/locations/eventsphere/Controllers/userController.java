package org.locations.eventsphere.Controllers;
import DTOs.preCreatedUserDTO;
import DTOs.userDTO;
import DTOs.userRegisterDTO;
import jakarta.validation.Valid;

import org.locations.eventsphere.Services.eventService;
import org.locations.eventsphere.Services.userService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/user")
public class userController {
    private final userService userService;
    private final eventService eventService;

    public userController(org.locations.eventsphere.Services.eventService eventService, org.locations.eventsphere.Services.userService userService) {
        this.eventService = eventService;
        this.userService = userService;
    }

    @PostMapping()
    public ResponseEntity<String> createUser(@RequestBody userRegisterDTO userDTO){
        userService.createUser(userDTO);
        return ResponseEntity.ok("User registered successfully");
    }
    @GetMapping("/getUsers")
    public List<userDTO> getUsers(@RequestParam("role") String role){
        return userService.getUsersByRole(role);
    }
    @PutMapping
    public userDTO updateUser(@RequestBody userDTO userDTO){
        return userService.updateUser(userDTO);
    }

    @GetMapping("/{mail}")
    public userRegisterDTO getUserByMail(@PathVariable("mail") String mail){
        return userService.getUserByMail(mail);
    }
    @PutMapping("/registry")
    public userRegisterDTO updateRegisterUser(@RequestBody userRegisterDTO user){
        return userService.updateUserRegister(user);
    }
    @PutMapping("/block")
    public userDTO blockUser(@RequestBody String mail){
        return userService.setBlockUser(mail);
    }
    @GetMapping("/login/{username}")
    public userDTO getUserByUsername(@PathVariable("username") String username){
        return userService.getUserByUsername(username);
    }
    @GetMapping("/details/{mail}")
    public userDTO getUserDetailsByMail(@PathVariable("mail") String mail){
        return userService.getUserDetailsByMail(mail);
    }
    @GetMapping("/getUsers/week")
    public List<userDTO> getUsersOfWeek(){
        return userService.usersRegistered();
    }
    @GetMapping("/getUsers/count")
    public String usersCount(@RequestParam("role") String role){
        return userService.usersCount(role);
    }
    @GetMapping("/organizer/{eventName}")
    public preCreatedUserDTO getOrganizerByEventName(@PathVariable("eventName") String eventName){
        System.out.println(eventName);
        return eventService.getOrganizerByEvent(eventName);
    }
}
