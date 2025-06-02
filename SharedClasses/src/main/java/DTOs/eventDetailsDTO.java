package DTOs;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class eventDetailsDTO {
    private String name;
    private String location;
    private LocalDate eventDate;


}
