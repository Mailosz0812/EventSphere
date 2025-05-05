package org.locations.eventsphere.Controllers;

import DTOs.PasswordTokenDTO;
import DTOs.userRegisterDTO;
import org.locations.eventsphere.Services.passwordResetService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/password-token")
public class PasswordResetController {
    private passwordResetService passResetService;

    public PasswordResetController(passwordResetService passResetService) {
        this.passResetService = passResetService;
    }
    @PostMapping
    public PasswordTokenDTO saveToken(@RequestBody PasswordTokenDTO passTokenDTO){
        System.out.println(passTokenDTO.getToken());
        return passResetService.saveToken(passTokenDTO);
    }
    @GetMapping("/user")
    public userRegisterDTO getUserByToken(@RequestParam("token") String token){
        System.out.println(token);
        return passResetService.getUserByToken(token);
    }
    @GetMapping
    public PasswordTokenDTO getToken(@RequestParam("token") String token){
        System.out.println(token);
        return passResetService.getToken(token);
    }
    @DeleteMapping
    public void deleteToken(@RequestParam("token") String token){
        passResetService.deleteToken(token);
    }
}
