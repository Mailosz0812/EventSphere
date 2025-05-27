package org.locations.eventsphere.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Table(name = "SUB_EVENT")
@Entity
@Data
@IdClass(ESubscribeID.class)
public class EventSubscribe {

    @Id
    @ManyToOne
    @JoinColumn(name = "EVENTID", referencedColumnName = "EVENTID")
    private Event event;

    @Id
    @ManyToOne
    @JoinColumn(name = "USERID", referencedColumnName = "USERID")
    private LoggedUser loggedUser;

    @Column(updatable = false,name = "SUBSCRIBED_AT")
    private LocalDateTime subscribedAt;

    @PrePersist
    protected void onCreate(){
        this.subscribedAt = LocalDateTime.now();
    }
}
