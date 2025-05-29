package org.locations.eventspheremvc.controllers;

import DTOs.poolDTO;
import DTOs.poolDetailsDTO;
import DTOs.purchaseRequestDTO;
import DTOs.ticketDTO;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.locations.eventspheremvc.services.PaypalService;
import org.locations.eventspheremvc.services.authContextProvider;
import org.locations.eventspheremvc.services.poolRequestService;
import org.locations.eventspheremvc.services.ticketRequestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping("/ticket")
public class ticketController {
    private final ticketRequestService ticketService;
    private final poolRequestService poolService;
    private final PaypalService paypalService;

    public ticketController(ticketRequestService ticketService, poolRequestService poolService, PaypalService paypalService) {
        this.ticketService = ticketService;
        this.poolService = poolService;
        this.paypalService = paypalService;
    }

    @GetMapping
    public String ticketsSummaryView(Model model) {
        String mail = authContextProvider.getMail();
//        Integer count = ticketService.countTickets(mail);
//        Integer sold = ticketService.countSoldTickets(mail);
//        Integer left = count - sold;
//        model.addAttribute("left",left);
//        model.addAttribute("sold",sold);
        return "ticketsView";
    }

    @GetMapping("/manage")
    public String ticketManageView(@RequestParam("name") String eName,
                                   @RequestParam(name = "editMode", required = false, defaultValue = "false") boolean editMode,
                                   @RequestParam(name = "poolID", required = false) Long poolID,
                                   Model model) {
        List<poolDetailsDTO> pools = poolService.getPools(eName);
        model.addAttribute("formAction", editMode ? "/pool/update" : "/pool");
        model.addAttribute("editMode", editMode);
        if (editMode) {
            poolDTO pool = poolService.getPool(poolID);
            model.addAttribute("poolDTO", pool);
        } else {
            model.addAttribute("poolDTO", new poolDTO());
        }
        model.addAttribute("name", eName);
        model.addAttribute("pools", pools);
        return "ticketView";
    }

    @PostMapping("/buy")
    public String buyTicket(@ModelAttribute purchaseRequestDTO requestDTO, RedirectAttributes attributes) {
        List<ticketDTO> tickets = requestDTO.getTickets()
                .stream().filter(t -> t.getQuantity() != null && t.getQuantity() > 0).toList();
        String eventName = requestDTO.getEventName();
        if (tickets.isEmpty()) {
            attributes.addFlashAttribute("response", "Choose at least one ticket");
            attributes.addAttribute("eName", eventName);
            return "redirect:/event/details";
        }
        double total = 0.0;
        String description = "Ticket for" + eventName;
        String cancelUrl = "http://localhost:8081/ticket/payment/cancel";
        String successUrl = "http://localhost:8081/ticket/payment/success?name="+ URLEncoder.encode(eventName, StandardCharsets.UTF_8);
        StringBuilder ticketInfo = new StringBuilder();
        for (ticketDTO ticket : tickets) {
            total += ticket.getPrice() * ticket.getQuantity();
            ticketInfo.append(ticket.getPoolID())
                    .append(":")
                    .append(ticket.getQuantity())
                    .append(";");
        }
        try {
            Payment payment = paypalService.createPayment(
                    total,
                    "PLN",
                    "paypal",
                    "sale",
                    description,
                    cancelUrl,
                    successUrl,
                    ticketInfo.toString()
            );
            for (Links links : payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    return "redirect:"+links.getHref();
                }
            }
        } catch (PayPalRESTException e) {
            System.out.println("Error occurred: " + e.getMessage());
            return "redirect:/ticket/payment/error";
        }
        return "redirect:/ticket/payment/error";
    }
    @GetMapping("/payment/success")
    public String paymentSuccess(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId,
            @RequestParam("name") String eventName,
            Model model
    ) {
        Payment payment = null;
        try {
            payment = paypalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                String total = payment.getTransactions().get(0).getAmount().getTotal();
                String userMail = authContextProvider.getMail();
                String method = payment.getPayer().getPaymentMethod();
                String customTicketInfo = payment.getTransactions().get(0).getCustom();
                ticketService.createTicket(total,userMail,method,customTicketInfo,eventName);
                model.addAttribute("transactionId",paymentId);
                model.addAttribute("eventName",eventName);
                return "paymentSuccessView";
            }
        } catch (PayPalRESTException e) {
            System.out.println("Error occurred: " + e.getMessage());
            return "redirect:/ticket/payment/error";
        }

        return "paymentSuccessView";
    }

    @GetMapping("/payment/cancel")
    public String paymentCancel(){
        return "paymentCancelView";
    }
    @GetMapping("/payment/error")
    public String paymentError(){
        return "paymentErrorView";
    }
}
