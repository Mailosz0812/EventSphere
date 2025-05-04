package org.locations.eventspheremvc.services;

import DTOs.preCreatedUserDTO;
import DTOs.userRegisterDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class accountsRequestService {
    private RestTemplate restTemplate;
    private final String userApiURL = "http://localhost:8080/eventSphereRest/user";

    public accountsRequestService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public userRegisterDTO getUserByMail(String mail){
        String url = userApiURL+"/getUser?mail={mail}";
        return restTemplate.getForObject(url, userRegisterDTO.class,mail);
    }
    public String createUser(userRegisterDTO userRegisterDTO,String role){
        userRegisterDTO.setROLE(role);
        userRegisterDTO.setPASSWORD(userRegisterDTO.getPASSWORD());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<userRegisterDTO> request = new HttpEntity<>(userRegisterDTO,headers);
        return restTemplate.postForObject(userApiURL,request,String.class);
    }
    public String adminCreateUser(preCreatedUserDTO userDTO, String role, String password){
        userRegisterDTO user = new userRegisterDTO("_","_",userDTO.getMail(),password,userDTO.getUsername(),role);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<userRegisterDTO> request = new HttpEntity<>(user,headers);
        return restTemplate.postForObject(userApiURL,request,String.class);
    }
}
