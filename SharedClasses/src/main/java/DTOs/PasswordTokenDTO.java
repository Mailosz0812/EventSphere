package DTOs;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PasswordTokenDTO {
    private String token;
    private LocalDateTime expireDate;
    private String mail;
}
