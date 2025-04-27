package DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class userRegisterDTO {
    @NotEmpty(message = "name must be provided")
    private String NAME;
    @NotEmpty(message = "surname must be provided")
    private String SURNAME;
    @Email(message = "Invalid Email format")
    @NotEmpty(message = "Email must be provided")
    private String MAIL;
    @Size(min = 8, message = "Minimum password length is 8 characters")
    private String PASSWORD;
    @NotEmpty(message = "Username must be provided")
    private String USERNAME;
    private String ROLE;
}
