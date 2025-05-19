package DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class userRegisterDTO {
    @NotEmpty(message = "Name must be provided")
    private String NAME;
    @NotEmpty(message = "Surname must be provided")
    private String SURNAME;
    @Email(message = "Invalid Email format")
    @NotEmpty(message = "Email must be provided")
    private String MAIL;
    private String PASSWORD;
    @NotEmpty(message = "Username must be provided")
    private String USERNAME;
    private String ROLE;
    private boolean NON_LOCKED;
}
