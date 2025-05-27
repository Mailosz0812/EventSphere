package org.locations.eventspheremvc.services;

import DTOs.eventDTO;
import DTOs.subscribeDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class userRequestService {
    private final RestTemplate restTemplate;
    private final String url = "http://localhost:8080/eventSphereRest/subscribe";

    public userRequestService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public int countSubscribedEvents(String mail){
        String apiUrl = url + "/count?mail={mail}";
        return restTemplate.getForObject(apiUrl, Integer.class,mail);
    }
    public void subscribeEvent(String eName,String userMail){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        subscribeDTO subDTO = new subscribeDTO(userMail,eName);
        HttpEntity<subscribeDTO> request = new HttpEntity<>(subDTO,headers);
        restTemplate.postForObject(url,request,Void.class);
    }
    public void unsubscribeEvent(String eName,String userMail){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        subscribeDTO subDTO = new subscribeDTO(userMail,eName);
        HttpEntity<subscribeDTO> request = new HttpEntity<>(subDTO,headers);
        restTemplate.exchange(url, HttpMethod.DELETE,request, Void.class);
    }
    public boolean subscribeState(String name ,String mail){
        String apiUrl = url + "?name={name}&mail={mail}";
        return restTemplate.getForObject(apiUrl, Boolean.class,name,mail);
    }
    public List<eventDTO> recentlySubscribed(String mail){
        String apiUrl = url + "/recently?mail={mail}";
        ResponseEntity<List<eventDTO>> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<eventDTO>>() {},
                mail);
        return response.getBody();
    }
    public List<eventDTO> incomingEvents(String mail){
        String apiUrl = url + "/incoming?mail={mail}";
        ResponseEntity<List<eventDTO>> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<eventDTO>>() {},
                mail
        );
        return response.getBody();
    }
}
