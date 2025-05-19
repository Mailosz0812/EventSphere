package org.locations.eventsphere.Entities;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "EVENTORGANIZE")
@NoArgsConstructor
@Data
public class EventOrganize {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "eventorganize_seq")
    @SequenceGenerator(name = "eventorganize_seq", sequenceName = "EVENTORGANIZE_SEQ")
    private Long EORGANIZEID;

    @ManyToOne
    @JoinColumn(name = "USERID", nullable = false)
    private LoggedUser organizer;

    @ManyToOne
    @JoinColumn(name = "EVENTID", nullable = false)
    private Event event;


}
