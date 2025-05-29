package DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ticketPaymentDTO {
    private String amount;
    private String mail;
    private String method;
    private String customTicketInfo;
    private String eventName;
}
