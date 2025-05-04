package DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class preCreatedUserDTO {
    @Email
    @NotEmpty(message = "Email must be provided")
    private String mail;
    @NotEmpty(message = "Username must be provided")
    private String username;
}
