package DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class commentDTO {
    private userDTO user;
    private eventDTO event;
    private String content;
}
