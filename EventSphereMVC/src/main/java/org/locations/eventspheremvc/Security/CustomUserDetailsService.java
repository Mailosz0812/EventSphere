package org.locations.eventspheremvc.Security;

import DTOs.userRegisterDTO;
import org.locations.eventspheremvc.services.accountsRequestService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;


@Service
public class CustomUserDetailsService implements UserDetailsService {
    private accountsRequestService userReqService;

    public CustomUserDetailsService(accountsRequestService userReqService) {
        this.userReqService = userReqService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try{
        userRegisterDTO userDTO = userReqService.getUserByMail(username);
        return new CustomUserDetails(userDTO);
        }catch (HttpClientErrorException e){
            throw new UsernameNotFoundException("Invalid credentials");
        }
    }
}
