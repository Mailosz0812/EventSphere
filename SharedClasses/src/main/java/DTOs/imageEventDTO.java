package DTOs;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class imageEventDTO {
    private String eName;
    @NotEmpty(message = "Alternative text should be provided")
    @Size(max=100, message = "Text is too long")
    private String altText;
    private byte[] imageBytes;
}
