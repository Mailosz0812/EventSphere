package org.locations.eventspheremvc.services;

import DTOs.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ticketRequestService {
    private final RestTemplate restTemplate;
    private final String url = "http://localhost:8080/eventSphereRest/ticket";

    public ticketRequestService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ticketPaymentDTO createTicket(String amount, String mail, String method, String customTicketInfo, String eventName){
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

    public List<EventTicketsWrapper> getTicketsByMail(String userMail,String sortStatus,String sortDate,String eventPattern) {
        String apiUrl = url + "/user?mail={mail}&status={sortStatus}&date={sortDate}&eventPattern={eventPattern}";
        return restTemplate.exchange(apiUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<EventTicketsWrapper>>() {},
                userMail,sortStatus,sortDate,eventPattern).getBody();
    }
    public Integer countSoldTickets(String mail){
        String apiUrl = url + "/sold?mail={mail}";
        return restTemplate.getForObject(apiUrl, Integer.class,mail);
    }
    public Integer countTickets(String mail){
        String apiUrl = url + "/all?mail={mail}";
        return restTemplate.getForObject(apiUrl,Integer.class,mail);
    }
    public ticketDetailsDTO getTicketDetailsById(Long ticketId){
        String apiUrl = url + "/details?ticketId={ticketId}";
        return restTemplate.getForObject(apiUrl, ticketDetailsDTO.class,ticketId);
    }
    public Integer countUserTickets(String mail){
        String apiUrl = url + "/user/count/{mail}";
        return restTemplate.getForObject(apiUrl,Integer.class,mail);
    }
    public void validateTicketData(String amount, String mail, String method, String customTicketInfo, String eventName){
        ticketPaymentDTO ticketPaymentDTO = new ticketPaymentDTO(amount,
                mail,
                method,
                customTicketInfo,
                eventName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ticketPaymentDTO> request = new HttpEntity<>(ticketPaymentDTO,headers);
        restTemplate.postForObject(url+"/check",request,DTOs.ticketPaymentDTO.class);
    }
}