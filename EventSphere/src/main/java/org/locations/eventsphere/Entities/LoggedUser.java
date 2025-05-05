package org.locations.eventsphere.Entities;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "LOGGEDUSER")
@NoArgsConstructor
@Data
public class LoggedUser {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "USER_SEQ", allocationSize = 1)
    private Long USERID;

    @Column(unique = true,nullable = false)
    private String NAME;
    private String SURNAME;

    @Column(unique = true,nullable = false)
    private String MAIL;

    @ManyToOne
    @JoinColumn(name = "ROLEID", referencedColumnName = "ROLEID")
    private Role ROLE;
    private String PASSWORD;
    private String USERNAME;
    private String DESCRIPTION;
    private boolean NON_LOCKED;

    public LoggedUser(String NAME, String SURNAME, String MAIL,String PASSWORD, String USERNAME) {
        this.NAME = NAME;
        this.SURNAME = SURNAME;
        this.MAIL = MAIL;
        this.PASSWORD = PASSWORD;
        this.USERNAME = USERNAME;
    }
}
