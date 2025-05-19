package org.locations.eventspheremvc.services;

import DTOs.categoryDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class categoryRequestService {
    private final RestTemplate restTemplate;
    private final String url = "http://localhost:8080/eventSphereRest/category";

    public categoryRequestService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<categoryDTO> getCategories(){
        String apiUrl = url + "/getAll";
        ResponseEntity<List<categoryDTO>> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<categoryDTO>>() {}
        );

        return response.getBody();
    }
}
