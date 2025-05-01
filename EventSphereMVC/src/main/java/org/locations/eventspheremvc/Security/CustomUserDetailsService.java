package org.locations.eventspheremvc.Security;

import DTOs.userRegisterDTO;
import org.locations.eventspheremvc.services.userRequestService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collection;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private userRequestService userReqService;

    public CustomUserDetailsService(userRequestService userReqService) {
        this.userReqService = userReqService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try{
        userRegisterDTO userDTO = userReqService.getUserByMail(username);
        UserDetails user = new User(userDTO.getMAIL(),userDTO.getPASSWORD(),extractRole(userDTO));
        return user;
        }catch (HttpClientErrorException e){
            throw new UsernameNotFoundException("Invalid credentials");
        }
    }
    private Collection<GrantedAuthority> extractRole(userRegisterDTO userDTO){
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_"+userDTO.getROLE()));
    }
}
