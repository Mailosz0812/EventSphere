package org.locations.eventsphere.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Payment")
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_seq")
    @SequenceGenerator(name = "payment_seq", sequenceName = "PAYMENT_SEQ", allocationSize = 1)
    private Long paymentID;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL)
    private List<Ticket> ticketsList;
    @OneToMany
    private List<Ticket> ticket;
    @Column(name = "PAYMENTMETHOD")
    private String paymentMethod;
    @Column(name = "AMOUNT")
    private Double amount;
    @Column(name = "PAYMENTDATE")
    private LocalDateTime paymentTimestamp;

    @PrePersist
    protected void onCreate(){
        this.paymentTimestamp = LocalDateTime.now();
    }
}
