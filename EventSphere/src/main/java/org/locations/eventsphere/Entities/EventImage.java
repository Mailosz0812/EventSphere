package org.locations.eventsphere.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "EVENT_IMAGE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventImage {

    @Id
    private Long id;
    @OneToOne
    @MapsId
    @JoinColumn(name = "EVENTID")
    private Event event;

    @Column(name = "ALT_TEXT")
    private String altText;

    @Lob
    @Column(name = "IMAGE", columnDefinition = "BLOB")
    private byte[] blob;

}
