package DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ticketsStatsDTO {
    private String eventName;
    private int ticketsSold;
    private List<dailySalesDetails> chart;
}
