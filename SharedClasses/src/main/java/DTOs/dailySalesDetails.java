package DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class dailySalesDetails {
    private String date;
    private int count;

    public dailySalesDetails(String date, Long count) {
        this.date = date;
        this.count = count.intValue();
    }
}
