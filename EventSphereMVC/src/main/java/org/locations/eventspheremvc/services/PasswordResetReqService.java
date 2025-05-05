package org.locations.eventspheremvc.services;

import DTOs.PasswordTokenDTO;
import DTOs.userRegisterDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PasswordResetReqService {
    private RestTemplate restTemplate;
    private String url = "http://localhost:8080/eventSphereRest/";


    public PasswordResetReqService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public userRegisterDTO getUserByToken(String token){
        return restTemplate.getForObject(url+"password-token/user?token={token}", userRegisterDTO.class,token);
    }
    public PasswordTokenDTO getTokenByToken(String token){
         return restTemplate.getForObject(url+"password-token?token={token}",PasswordTokenDTO.class,token);
    }
    public PasswordTokenDTO saveToken(PasswordTokenDTO tokenDTO){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PasswordTokenDTO> request = new HttpEntity<>(tokenDTO,headers);
        return restTemplate.postForObject(url+"password-token",request,PasswordTokenDTO.class);
    }
    public void deleteToken(String token){
        restTemplate.delete(url + "password-token?token={token}", token);
    }
}
