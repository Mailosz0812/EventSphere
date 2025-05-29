package org.locations.eventsphere.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TICKET_POOL")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pool {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "pool_seq")
    @SequenceGenerator(name = "pool_seq", sequenceName = "POOL_SEQ", allocationSize = 1)
    @Column(name = "POOLID")
    private Long poolID;
    @Column(name = "POOL_TYPE")
    private String poolName;
    @Column(name = "POOL_STATUS")
    private String poolStatus;
    @Column(name = "PRICE")
    private Double price;
    @Column(name = "TICKET_COUNT")
    private Integer ticketCount;
    @ManyToOne
    @JoinColumn(name = "EVENTID")
    private Event event;
}
