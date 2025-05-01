package org.locations.eventspheremvc.controllers;

import DTOs.authDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/login")
public class AuthController {

    @GetMapping
    public String getLogin() {
        return "login";
    }

}
