package org.locations.eventsphere.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TICKET")
@NoArgsConstructor
@Data
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticket_seq")
    @SequenceGenerator(name = "ticket_seq", sequenceName = "TICKET_SEQ", allocationSize = 1)
    private Long TICKETID;

    @ManyToOne
    @JoinColumn(name = "EVENTID", referencedColumnName = "EVENTID")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "USERID", referencedColumnName = "USERID")
    private LoggedUser user;

    @ManyToOne
    @JoinColumn(name = "PAYMENTID", referencedColumnName = "PAYMENTID")
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "POOLID", referencedColumnName = "POOLID")
    private Pool pool;

}
