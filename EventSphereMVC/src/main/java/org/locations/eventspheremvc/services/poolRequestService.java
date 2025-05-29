package org.locations.eventspheremvc.services;

import DTOs.poolDTO;
import DTOs.poolDetailsDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class poolRequestService {
    private final RestTemplate restTemplate;
    private String url = "http://localhost:8080/eventSphereRest/pool";

    public poolRequestService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public poolDTO createPool(poolDTO poolDTO){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<poolDTO> request = new HttpEntity<>(poolDTO,headers);
        return restTemplate.postForObject(url,request,poolDTO.class);
    }
    public List<poolDetailsDTO> getPools(String name){
        String apiUrl = url + "/all?name={name}";
        return restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<poolDetailsDTO>>() {},
                name).getBody();
    }
    public void deletePool(Long poolID,String eventName){
        String apiUrl = url + "?poolID={poolID}&eventName={eventName}";
        restTemplate.exchange(apiUrl,
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<Void>() {},
                poolID, eventName);
    }
    public void updatePool(poolDTO poolDTO){;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<poolDTO> request = new HttpEntity<>(poolDTO,headers);
        restTemplate.exchange(
                url,
                HttpMethod.PUT,
                request,
                new ParameterizedTypeReference<Void>() {}
        );
    }
    public poolDTO getPool(Long poolID){
        String apiUrl = url + "?poolID={poolID}";
        return restTemplate.getForObject(apiUrl,poolDTO.class,poolID);
    }
}
