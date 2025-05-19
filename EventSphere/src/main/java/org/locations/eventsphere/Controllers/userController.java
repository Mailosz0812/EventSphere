package org.locations.eventsphere.Controllers;
import DTOs.userDTO;
import DTOs.userRegisterDTO;
import jakarta.validation.Valid;

import org.locations.eventsphere.Services.userService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/user")
public class userController {
    private userService userService;
    public userController(org.locations.eventsphere.Services.userService userService) {
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

    @GetMapping("/getUser/mail")
    public userRegisterDTO getUserByMail(@RequestParam("mail") String mail){
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
    @GetMapping("/getUser/username")
    public userDTO getUserByUsername(@RequestParam("username") String username){
        return userService.getUserByUsername(username);
    }
    @GetMapping("/getUsers/week")
    public List<userDTO> getUsersOfWeek(){
        return userService.usersRegistered();
    }
    @GetMapping("/getUsers/count")
    public String usersCount(@RequestParam("role") String role){
        return userService.usersCount(role);
    }
}
