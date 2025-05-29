package org.locations.eventspheremvc.services;

import DTOs.ticketDTO;
import DTOs.ticketPaymentDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ticketRequestService {
    private final RestTemplate restTemplate;
    private final String url = "http://localhost:8080/eventSphereRest/ticket";

    public ticketRequestService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ticketPaymentDTO createTicket(String amount,String mail,String method,String customTicketInfo,String eventName){
        ticketPaymentDTO ticketPaymentDTO = new ticketPaymentDTO(amount,
                mail,
                method,
                customTicketInfo,
                eventName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ticketPaymentDTO> request = new HttpEntity<>(ticketPaymentDTO,headers);
        return restTemplate.postForObject(url,request,DTOs.ticketPaymentDTO.class);
    }

    public Integer countSoldTickets(String mail){
        String apiUrl = url + "/sold?mail={mail}";
        return restTemplate.getForObject(apiUrl, Integer.class,mail);
    }
    public Integer countTickets(String mail){
        String apiUrl = url + "/all?mail={mail}";
        return restTemplate.getForObject(apiUrl,Integer.class,mail);
    }
}