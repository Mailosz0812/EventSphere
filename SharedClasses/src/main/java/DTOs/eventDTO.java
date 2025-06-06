package DTOs;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class eventDTO {

    @NotEmpty(message = "Name of event must be provided")
    @Pattern(regexp = "^[a-zA-Z0-9\\s.,!?:;'-]*$", message = "Don't use special characters here")
    private String NAME;
    @NotNull(message = "Date of event must be provided")
    @FutureOrPresent(message = "Date must be in the future or present")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate EVENTDATE;
    @NotEmpty(message = "Location must be provided")
    private String LOCATION;
    @Size(max = 1000, message = "Try shorter description")
    private String DESCRIPTION;
    @NotEmpty(message = "Category must be provided")
    private String CATEGORY;

    private String organizerMail;

}
