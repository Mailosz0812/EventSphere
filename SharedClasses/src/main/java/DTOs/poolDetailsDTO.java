package DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class poolDetailsDTO {
    private Long poolID;
    private String poolType;
    private Double price;
    private Integer sold;
    private Integer ticketCount;
}
