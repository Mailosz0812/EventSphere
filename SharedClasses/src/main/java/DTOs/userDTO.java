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
    private String USERNAME;
    private String DESCRIPTION;
    private String MAIL;
    private String NAME;
    private String SURNAME;
    private boolean NON_LOCKED;
    private String ROLE;
    private LocalDateTime TIMESTAMP;
}
