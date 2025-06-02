package DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class userRegisterDTO {
    @NotEmpty(message = "Name must be provided")
    private String name;
    @NotEmpty(message = "Surname must be provided")
    private String surname;
    @Email(message = "Invalid Email format")
    @NotEmpty(message = "Email must be provided")
    private String mail;
    private String password;
    @NotEmpty(message = "Username must be provided")
    private String username;
    private String role;
    private boolean nonLocked;
}
