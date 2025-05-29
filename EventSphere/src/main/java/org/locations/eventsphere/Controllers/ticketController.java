package org.locations.eventsphere.Controllers;

import DTOs.ticketPaymentDTO;
import org.locations.eventsphere.Repositories.eventOrganizeRepository;
import org.locations.eventsphere.Repositories.ticketRepository;
import org.locations.eventsphere.Services.ticketService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ticket")
public class ticketController {
    private final ticketService ticketService;

    public ticketController(org.locations.eventsphere.Services.ticketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/sold")
    public int countSoldTicketsByOrganizer(@RequestParam("mail") String mail){
        return ticketService.countSoldTicketsByOrganizer(mail);
    }
    @GetMapping("/all")
    public void countTicketsByOrganizer(@RequestParam("mail") String mail){
//        return ticketService.countTicketsByOrganizer(mail);
    }
    @PostMapping
    public ticketPaymentDTO createTicketPayment(@RequestBody ticketPaymentDTO ticketPaymentDTO){
        DTOs.ticketPaymentDTO ticketPayment = ticketService.createTicketPayment(ticketPaymentDTO);
        return ticketPayment;
    }
}
