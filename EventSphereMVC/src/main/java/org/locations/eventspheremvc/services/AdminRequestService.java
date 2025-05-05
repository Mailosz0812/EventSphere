package org.locations.eventspheremvc.services;

import DTOs.preCreatedUserDTO;
import DTOs.userRegisterDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AdminRequestService {
    private RestTemplate restTemplate;
    private final String ApiURL = "http://localhost:8080/eventSphereRest/";

    public AdminRequestService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String adminCreateUser(preCreatedUserDTO userDTO, String role, String password){
        userRegisterDTO user = new userRegisterDTO("_","_",userDTO.getMail(),password,userDTO.getUsername(),role,false);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<userRegisterDTO> request = new HttpEntity<>(user,headers);
        return restTemplate.postForObject(ApiURL+"user",request,String.class);
    }
}
