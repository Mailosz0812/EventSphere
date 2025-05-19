package org.locations.eventsphere.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "event_seq")
    @SequenceGenerator(name = "event_seq", sequenceName = "EVENT_SEQ", allocationSize = 1)
    private Long EVENTID;
    @Column(unique = true,nullable = false)
    private String NAME;
    private LocalDate EVENTDATE;
    private int TICKETCOUNT;
    private String LOCATION;
    @Column(length = 1000)
    private String DESCRIPTION;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CATEGORYID", referencedColumnName = "CATEGORYID")
    private EventCategory EVENTCATEGORY;
    @OneToMany(mappedBy = "event", fetch = FetchType.EAGER)
    private List<EventOrganize> organizes;
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
    }
}
