package org.locations.eventspheremvc.services;

import DTOs.eventDTO;
import DTOs.preCreatedUserDTO;
import DTOs.userDTO;
import DTOs.userRegisterDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class AdminRequestService {
    private RestTemplate restTemplate;
    private final String ApiURL = "http://localhost:8080/eventSphereRest/";
    public AdminRequestService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    public String adminCreateUser(preCreatedUserDTO userDTO, String role){
        userRegisterDTO user = userRegisterDTO.builder()
                .role(role)
                .name("_")
                .mail(userDTO.getMail())
                .surname("_")
                .nonLocked(false)
                .username(userDTO.getUsername()).build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<userRegisterDTO> request = new HttpEntity<>(user,headers);
        return restTemplate.postForObject(ApiURL+"user",request,String.class);
    }
    public List<userDTO> getUsersByRole(String role){
        String url = ApiURL + "/user/getUsers?role={role}";
        ResponseEntity<List<userDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<userDTO>>() {},
                role
        );
        return response.getBody();
    }
    public String blockUser(String mail){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(mail,headers);
        return restTemplate.exchange(ApiURL+"/user/block", HttpMethod.PUT,request,String.class).getBody();
    }
    public List<userDTO> usersOfWeek(){
        String url = ApiURL + "/user/getUsers/week";
        ResponseEntity<List<userDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<userDTO>>() {
                }
        );
        return response.getBody();
    }
    public String getUsersCount(String role){
        String url = ApiURL + "/user/getUsers/count?role={role}";
        return restTemplate.getForObject(url, String.class,role);
    }
    public List<eventDTO> getWeekEvents(){
        String apiUrl = ApiURL + "event/recent";
        ResponseEntity<List<eventDTO>> events = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<eventDTO>>() {}
        );
        return events.getBody();
    }

}
