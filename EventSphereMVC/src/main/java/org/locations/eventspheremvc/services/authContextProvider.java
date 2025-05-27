package org.locations.eventspheremvc.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class authContextProvider {
    public static String getMail(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null && auth.isAuthenticated()){
            return auth.getName();
        }
        return null;
    }
}
