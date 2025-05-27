package org.locations.eventspheremvc.controllers;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.locations.eventspheremvc.services.PaypalService;
import org.springframework.stereotype.Controller;

@Controller
public class PaymentController {
    private final PaypalService paypalService;

    public PaymentController(PaypalService paypalService) {
        this.paypalService = paypalService;
    }
}
