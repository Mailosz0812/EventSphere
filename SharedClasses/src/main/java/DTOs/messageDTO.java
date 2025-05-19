package DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class messageDTO {
    @Email(message = "Provide proper email")
    @NotEmpty(message = "Receiver email is empty")
    private String to;
    @NotEmpty(message = "Subject is empty")
    private String Subject;
    @NotEmpty(message = "Message is empty")
    private String message;
}
