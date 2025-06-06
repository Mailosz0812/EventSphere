package org.locations.eventspheremvc.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.locations.eventspheremvc.services.authContextProvider;
import org.locations.eventspheremvc.services.subscribeRequestService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class subscribeController {
    private subscribeRequestService subscribeService;

    public subscribeController(subscribeRequestService userService) {
        this.subscribeService = userService;
    }

    @PostMapping("/subscribe")
    public String subscribeEvent(@RequestParam("name") String eName, HttpServletRequest request){
        String url = request.getHeader("Referer");
        subscribeService.subscribeEvent(eName, authContextProvider.getMail());
        return "redirect:"+url;
    }
    @PostMapping("/unsubscribe")
    public String unsubscribeEvent(@RequestParam("name") String eName,HttpServletRequest request){
        String url = request.getHeader("Referer");
        subscribeService.unsubscribeEvent(eName,authContextProvider.getMail());
        return "redirect:" + url;
    }
}
