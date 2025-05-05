package org.locations.eventsphere.Entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "PASSWORD_RESET_TOKEN")
@Data
@NoArgsConstructor
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "token_seq")
    @SequenceGenerator(name = "token_seq", sequenceName = "TOKEN_SEQ",allocationSize = 1)
    private Long TOKENID;
    @Column(nullable = false,unique = true)
    private String TOKEN;
    @OneToOne
    @JoinColumn(name = "USERID", referencedColumnName = "USERID")
    private LoggedUser USERID;
    private LocalDateTime EXPIRE_DATE;
}
