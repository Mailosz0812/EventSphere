package org.locations.eventsphere.Controllers;

import DTOs.EventTicketsWrapper;
import DTOs.ticketDetailsDTO;
import DTOs.ticketPaymentDTO;
import DTOs.ticketsStatsDTO;
import org.locations.eventsphere.Services.ticketService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ticket")
public class ticketController {
    private final ticketService ticketService;

    public ticketController(org.locations.eventsphere.Services.ticketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/sold/{mail}")
    public int countSoldTicketsByOrganizer(@PathVariable("mail") String mail){
        return ticketService.countSoldTicketsByOrganizer(mail);
    }
    @GetMapping("/all/{mail}")
    public Integer countTicketsByOrganizer(@PathVariable("mail") String mail){
        return ticketService.countTicketsByOrganizer(mail);
    }
    @GetMapping("/user")
    public List<EventTicketsWrapper> getTicketDetailsByUser(
            @RequestParam("mail") String mail,
            @RequestParam(name = "status",required = false ) String status,
            @RequestParam(name = "date", required = false) String date,
            @RequestParam(name = "eventPattern", required = false) String eventPattern){
        return ticketService.getTicketsByMail(mail, status, date,eventPattern);
    }
    @PostMapping
    public ticketPaymentDTO createTicketPayment(@RequestBody ticketPaymentDTO ticketPaymentDTO){
        return ticketService.createTicketPayment(ticketPaymentDTO);
    }
    @GetMapping("/details")
    public ticketDetailsDTO getTicketDetails(@RequestParam("ticketId") Long ticketId){
        return ticketService.getTicketDetails(ticketId);
    }
    @GetMapping("/user/count/{mail}")
    public int countUserTickets(@PathVariable("mail") String mail){
        return ticketService.countTicketsByUser(mail);
    }
    @PostMapping("/check")
    public void checkTicketData(@RequestBody ticketPaymentDTO ticketPaymentDTO){
        ticketService.validateTicketPaymentData(ticketPaymentDTO);
    }

//    Dopisz metode w serwisie do generowania statystyk
    @GetMapping("/stats/{mail}")
    public List<ticketsStatsDTO> getTicketsStats(@PathVariable("mail")String mail){
        return ticketService.getTicketsStats(mail);
    }
}
