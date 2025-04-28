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
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private userRequestService userRequestService;

    public CustomUserDetailsService(userRequestService userRequestService) {
        this.userRequestService = userRequestService;
    }
    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        userRegisterDTO userRegisterDTO = userRequestService.getUserByMail(mail);
        return new User(userRegisterDTO.getMAIL(),userRegisterDTO.getPASSWORD(), Collections.singletonList(mapRoleToAuthorities(userRegisterDTO.getROLE())));
    }

    private GrantedAuthority mapRoleToAuthorities(String role){
        return new SimpleGrantedAuthority("ROLE_"+role);
    }
}
