package DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ticketDetailsDTO {
    private Long ticketID;
    private String ticketType;
    private String price;
    private Integer quantity;
    private LocalDate purchaseDate;
    private String ticketStatus;
    private String poolPrice;
}
