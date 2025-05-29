package org.locations.eventspheremvc.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.locations.eventspheremvc.services.authContextProvider;
import org.locations.eventspheremvc.services.userRequestService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class subscribeController {
    private userRequestService userService;

    public subscribeController(userRequestService userService) {
        this.userService = userService;
    }

    @PostMapping("/subscribe")
    public String subscribeEvent(@RequestParam("name") String eName, HttpServletRequest request){
        String url = request.getHeader("Referer");
        userService.subscribeEvent(eName, authContextProvider.getMail());
        return "redirect:"+url;
    }
    @PostMapping("/unsubscribe")
    public String unsubscribeEvent(@RequestParam("name") String eName,HttpServletRequest request){
        String url = request.getHeader("Referer");
        userService.unsubscribeEvent(eName,authContextProvider.getMail());
        return "redirect:" + url;
    }
}
