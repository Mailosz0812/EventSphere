package DTOs;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class eventDTO {
    @NotEmpty(message = "Name of event must be provided")
    private String NAME;
    @NotNull(message = "Date of event must be provided")
    @FutureOrPresent(message = "Date must be in the future or present")
    private LocalDate EVENTDATE;
    @NotNull(message = "Number of tickets must be provided")
    @Positive(message = "Number of tickets must be positive")
    private int TICKETCOUNT;
    @NotEmpty(message = "Location must be provided")
    private String LOCATION;
    private String DESCRIPTION;
    @NotEmpty(message = "Category must be provided")
    private String CATEGORY;
}
