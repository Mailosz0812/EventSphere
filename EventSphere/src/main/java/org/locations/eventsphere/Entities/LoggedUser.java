package org.locations.eventsphere.Entities;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "LOGGEDUSER")
@NoArgsConstructor
@Data
public class LoggedUser {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "USER_SEQ", allocationSize = 1)
    @Column(name="USERID")
    private Long userId;

    @Column(unique = true,nullable = false,name="NAME")
    private String name;
    @Column(name="SURNAME")
    private String surname;

    @Column(unique = true,nullable = false,name = "MAIL")
    private String mail;

    @ManyToOne
    @JoinColumn(name = "ROLEID", referencedColumnName = "ROLEID")
    private Role role;

    @Column(name = "PASSWORD")
    private String password;
    @Column(name="USERNAME")
    private String username;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "NON_LOCKED")
    private boolean nonLocked;
    @Column(updatable = false)
    private LocalDateTime userTimestamp;
    @PrePersist
    protected void onCreate(){
        this.userTimestamp = LocalDateTime.now();
    }
    public LoggedUser(String name, String surname, String mail,String password, String username) {
        this.name = name;
        this.surname = surname;
        this.mail = mail;
        this.password = password;
        this.username = username;
    }
}
