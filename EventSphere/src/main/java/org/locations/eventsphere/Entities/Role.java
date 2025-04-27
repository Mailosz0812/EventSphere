package org.locations.eventsphere.Entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "ROLE_T")
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_seq")
    @SequenceGenerator(name = "role_seq", sequenceName = "ROLE_SEQ", allocationSize = 1)
    private Long ROLEID;
    private String NAME;
}
