package org.locations.eventspheremvc.services;

import DTOs.userRegisterDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AdminRequestService {
    private RestTemplate restTemplate;
    private final String ApiURL = "http://localhost:8080/eventSphereRest/";
}
