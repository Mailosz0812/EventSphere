package org.locations.eventspheremvc.controllers;

import DTOs.PasswordTokenDTO;
import DTOs.userRegisterDTO;
import org.locations.eventspheremvc.services.EmailService;
import org.locations.eventspheremvc.services.PasswordResetReqService;
import org.locations.eventspheremvc.services.accountsRequestService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.util.UUID;

@Controller
@RequestMapping("/reset")
public class PasswordResetController {
    private final PasswordResetReqService passRequestService;
    private final accountsRequestService accountService;
    private final PasswordEncoder encoder;
    private final EmailService emailService;

    public PasswordResetController(PasswordResetReqService passRequestService,
                                   accountsRequestService accountService,
                                   PasswordEncoder encoder, EmailService emailService) {
        this.passRequestService = passRequestService;
        this.accountService = accountService;
        this.encoder = encoder;
        this.emailService = emailService;
    }

    @GetMapping
    public String setPasswordView(Model model, @RequestParam("token") String token){
        try {
            passRequestService.getTokenByToken(token);
            model.addAttribute("token",token);
            return "setPasswordView";
        }catch (HttpClientErrorException e){
            System.out.println(e.getResponseBodyAsString());
            return "redirect:/reset/invalid-token";
        }
    }
    @PostMapping
    public String setPassword(Model model, @RequestParam("newPassword") String password, @RequestParam String token){
        try {
            userRegisterDTO user = passRequestService.getUserByToken(token);
            if(password.length() < 8){
                model.addAttribute("error","Password too short");
                model.addAttribute("token",token);
                return "setPasswordView";
            }
            user.setPASSWORD(encoder.encode(password));
            user.setNON_LOCKED(true);
            accountService.updateUser(user);
            passRequestService.deleteToken(token);
            return "redirect:/login";
        }catch (HttpClientErrorException e){
            System.out.println(e.getResponseBodyAsString());
            model.addAttribute("error",e.getResponseBodyAsString());
            model.addAttribute(token);
            return "setPasswordView";
        }
    }
    @GetMapping("/invalid-token")
    public String invalidTokenView(){
        return "invalidTokenView";
    }
    @GetMapping("/password")
    public String resetPasswordView(){
        return "resetPasswordView";
    }
    @PostMapping("/password")
    public String resetPassword(Model model,@RequestParam("email") String email){
        try{
            accountService.getUserByMail(email);
            String token = UUID.randomUUID().toString();
            PasswordTokenDTO newToken = new PasswordTokenDTO();
            newToken.setToken(token);
            newToken.setMail(email);
            newToken.setExpireDate(LocalDateTime.now().plusMinutes(30));
            passRequestService.saveToken(newToken);
            emailService.sendPasswordLink(email,token);

        }catch (HttpClientErrorException e){

        }
        model.addAttribute("response","If email is valid we will send reset link");
        return "resetPasswordView";
    }
}
