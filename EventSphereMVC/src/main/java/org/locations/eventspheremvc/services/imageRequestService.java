package org.locations.eventspheremvc.services;

import DTOs.imageEventDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class imageRequestService {
    private final RestTemplate restTemplate;
    private final String url = "http://localhost:8080/eventSphereRest/image";

    public imageRequestService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public imageEventDTO uploadImage(MultipartFile file, imageEventDTO iEventDTO) throws IOException {
        iEventDTO.setImageBytes(file.getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<imageEventDTO> request = new HttpEntity<>(iEventDTO,headers);
        return restTemplate.postForObject(url,request, imageEventDTO.class);
    }
    public void deleteImage(String eName){
        String apiUrl = url + "?eName={eName}";
        restTemplate.exchange(apiUrl, HttpMethod.DELETE,null,Void.class,eName);
    }
    public imageEventDTO getImageByEventName(String eName){
        String apiUrl = url+"?eName={eName}";
        return restTemplate.getForObject(apiUrl, imageEventDTO.class,eName);
    }
}
