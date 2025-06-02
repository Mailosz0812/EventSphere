package org.locations.eventspheremvc.services;

import DTOs.eventDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class eventRequestService {
    private final RestTemplate restTemplate;
    private final String url = "http://localhost:8080/eventSphereRest/event";

    public eventRequestService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String countEvent(){
        return restTemplate.getForObject(url+"/count", String.class);
    }
    public eventDTO createEvent(eventDTO event){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<eventDTO> request = new HttpEntity<>(event,headers);
        return restTemplate.postForObject(url,request, eventDTO.class);
    }
    public List<eventDTO> getEvents(String organizerMail){
        String apiUrl = url + "/events?mail={mail}";
        ResponseEntity<List<eventDTO>> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<eventDTO>>() {},
                organizerMail
        );
        return response.getBody();
    }
    public eventDTO getEventDetails(String name){
        return restTemplate.getForObject(url +"/{name}", eventDTO.class,name);
    }
    public String updateEvent(eventDTO event){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<eventDTO> request = new HttpEntity<>(event,headers);
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                request,
                String.class
        );
        return response.getBody();
    }
    public List<eventDTO> getAllEvents(){
        String apiUrl = url + "/events/all";
        ResponseEntity<List<eventDTO>> events = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<eventDTO>>() {}
        );
        return events.getBody();
    }
    public List<eventDTO> getEventsByCategory(String category){
        String apiUrl = url + "/events/all?category={category}";
        ResponseEntity<List<eventDTO>> events = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<eventDTO>>() {},
                category
        );
        return events.getBody();
    }
    public List<eventDTO> getEventsByName(String name){
        String apiUrl = url + "/events/{name}";
        ResponseEntity<List<eventDTO>> events = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<eventDTO>>() {},
                name
        );
                return events.getBody();

    }

}
