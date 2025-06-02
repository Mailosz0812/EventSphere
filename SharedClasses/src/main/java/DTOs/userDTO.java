package DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class userDTO {
    private String username;
    private String description;
    private String mail;
    private String name;
    private String surname;
    private boolean nonLocked;
    private String role;
    private LocalDateTime timestamp;
}
