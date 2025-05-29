package DTOs;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class poolDTO {
    private Long poolID;
    private String eventName;
    @NotEmpty(message = "Pool name must be provided")
    private String poolType;
    @NotNull(message = "Price must be provided")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private Double price;
    @NotNull(message = "Amount of tickets must be provided")
    @DecimalMin(value = "0.0", message = "Amount of tickets must be greater than 0")
    private int ticketCount;
}
