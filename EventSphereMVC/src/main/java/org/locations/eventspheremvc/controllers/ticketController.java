package org.locations.eventspheremvc.controllers;

import DTOs.*;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.servlet.http.HttpSession;
import org.locations.eventspheremvc.Exceptions.EventSphereMVCException;
import org.locations.eventspheremvc.services.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
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
    private final EmailService emailService;

    public ticketController(ticketRequestService ticketService, poolRequestService poolService,
                            PaypalService paypalService, EmailService emailService) {
        this.ticketService = ticketService;
        this.poolService = poolService;
        this.paypalService = paypalService;
        this.emailService = emailService;
    }

    @GetMapping("/manage")
    public String ticketManageView(@RequestParam("name") String eName,
                                   @RequestParam(name = "editMode", required = false, defaultValue = "false") boolean editMode,
                                   @RequestParam(name = "poolID", required = false) Long poolID,
                                   Model model) {
        List<poolDetailsDTO> pools = null;
        try {
             pools = poolService.getPools(eName);
        }catch (HttpClientErrorException e){
            model.addAttribute("error",e.getResponseBodyAsString());
            return "errorView";
        }
        model.addAttribute("formAction", editMode ? "/pool/update" : "/pool");
        model.addAttribute("editMode", editMode);
        if (editMode) {

            poolDTO pool = null;
            try{
                pool = poolService.getPool(poolID);
            }catch (HttpClientErrorException e){
                model.addAttribute("error",e.getResponseBodyAsString());
                return "errorView";
            }
            model.addAttribute("poolDTO", pool);
        } else {
            model.addAttribute("poolDTO", new poolDTO());
        }
        model.addAttribute("name", eName);
        model.addAttribute("pools", pools);
        return "ticketView";
    }
    @PostMapping("/buy")
    public String buyTicket(@ModelAttribute purchaseRequestDTO requestDTO, RedirectAttributes attributes,Model model) {
        if(requestDTO.getEventName() == null){
            model.addAttribute("error","Something went wrong");
            return "errorView";
        }
        if(requestDTO.getTickets() == null){
            attributes.addAttribute("eName", requestDTO.getEventName());
            return "redirect:/event/details";
        }
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
        String method = "paypal";
        String tickInfoS = ticketInfo.toString();
        try {
            ticketService.validateTicketData(String.valueOf(total), authContextProvider.getMail(), method, tickInfoS, eventName);
        }catch (HttpClientErrorException e){
            System.out.println(e.getResponseBodyAsString());
            throw new EventSphereMVCException("Payment interrupted");
        }
        try {
            Payment payment = paypalService.createPayment(
                    total,
                    "PLN",
                    method,
                    "sale",
                    description,
                    cancelUrl,
                    successUrl,
                    tickInfoS
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
    @GetMapping("/code")
    public ResponseEntity<byte[]> generateQrCode(@RequestParam("ticketId") Long ticketId) throws Exception{
        ticketDetailsDTO ticketDetails = null;
        byte[] qrCode = null;
        try{
            ticketDetails = ticketService.getTicketDetailsById(ticketId);
            qrCode = QrCodeGenerator.generateQRCodeImage(ticketDetails);
        }catch (HttpClientErrorException e){
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(qrCode);
    }
    @GetMapping("/code/send")
    public String sendQrCodeViaMail(@RequestParam("ticketId") Long ticketId) throws Exception{
        String mail = authContextProvider.getMail();
        ticketDetailsDTO ticketDetails = ticketService.getTicketDetailsById(ticketId);
        byte[] qrCodeBytes = QrCodeGenerator.generateQRCodeImage(ticketDetails);
        emailService.SendQrCode(mail,qrCodeBytes);
        return "redirect:/home/tickets";
    }
}
