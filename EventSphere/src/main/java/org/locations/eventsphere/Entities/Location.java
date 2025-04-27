package org.locations.eventsphere.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "LOCATION")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "location_seq")
    @SequenceGenerator(name = "location_seq", sequenceName = "LOCATION_SEQ", allocationSize = 1)
    private Long LOCATIONID;

    private String STREET;
    private String POSTALCODE;
    private String CITY;

}
