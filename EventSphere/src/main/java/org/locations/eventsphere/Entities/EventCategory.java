package org.locations.eventsphere.Entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "EVENTCATEGORY")
public class EventCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_seq")
    @SequenceGenerator(name = "category_seq", sequenceName = "CATEGORY_SEQ", allocationSize = 1)
    private Long CATEGORYID;
    private String NAME;
}
