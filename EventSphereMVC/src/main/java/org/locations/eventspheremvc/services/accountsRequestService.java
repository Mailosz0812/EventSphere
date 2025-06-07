package org.locations.eventspheremvc.services;

import DTOs.eventDTO;
import DTOs.preCreatedUserDTO;
import DTOs.userDTO;
import DTOs.userRegisterDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class accountsRequestService {
    private RestTemplate restTemplate;
    private final String userApiURL = "http://localhost:8080/eventSphereRest/user";

    public accountsRequestService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public userRegisterDTO getUserByMail(String mail){
        String url = userApiURL+"/{mail}";
        return restTemplate.getForObject(url, userRegisterDTO.class,mail);
    }
    public String createUser(userRegisterDTO userRegisterDTO,String role){
        userRegisterDTO.setPassword(userRegisterDTO.getPassword());
        userRegisterDTO.setRole(role);
        userRegisterDTO.setNonLocked(true);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<userRegisterDTO> request = new HttpEntity<>(userRegisterDTO,headers);
        return restTemplate.postForObject(userApiURL,request,String.class);
    }
    public void updateUser(userRegisterDTO user){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<userRegisterDTO> request = new HttpEntity<>(user,headers);
        restTemplate.exchange(userApiURL+"/registry", HttpMethod.PUT,request,String.class).getBody();
    }
    public void updateUserDetails(userDTO user){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<userDTO> request = new HttpEntity<>(user);
        restTemplate.exchange(userApiURL,
                HttpMethod.PUT,
                request,
                userDTO.class);
    }
    public preCreatedUserDTO getOrganizerByEvent(String eventName){
        String apiUrl = userApiURL + "/organizer/{eventName}";
        return restTemplate.exchange(apiUrl,
                HttpMethod.GET,
                null,
                preCreatedUserDTO.class,
                eventName).getBody();
    }
    public userDTO getUserByUsername(String username){
        return restTemplate.getForObject(userApiURL+"/login/{username}",userDTO.class,username);
    }
    public userDTO findUserByMail(String mail){
        return restTemplate.getForObject(userApiURL + "/details/{mail}"
        , userDTO.class
        ,mail);
    }
}
