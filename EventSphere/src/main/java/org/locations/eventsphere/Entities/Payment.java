package org.locations.eventsphere.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Payment")
@Data
public class Payment {

    @Id
    private Long PAYMENTID;

    @OneToMany
    private List<Ticket> ticket;

    private String PAYMENTMETHOD;
    private Double AMOUNT;
    private LocalDate PAYMENTDATE;



}
