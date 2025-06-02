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
    @Column(name = "EVENTID")
    private Long eventId;
    @Column(unique = true,nullable = false, name="name")
    private String name;
    @Column(name = "EVENTDATE")
    private LocalDate eventDate;
    @Column(name = "LOCATION")
    private String location;
    @Column(length = 1000,name="DESCRIPTION")
    private String description;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CATEGORYID", referencedColumnName = "CATEGORYID")
    private EventCategory eventCategory;
    @OneToMany(mappedBy = "event", fetch = FetchType.EAGER)
    private List<EventOrganize> organizes;
    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    @OneToOne(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private EventImage eventImage;
    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
    }
    @PreUpdate
    protected  void onUpdate(){
        this.modifiedAt = LocalDateTime.now();
    }
}
