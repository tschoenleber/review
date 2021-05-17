package schwarz.jobs.interview.coupon.web.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class CouponDTO {

    private BigDecimal discount;
    @NonNull
    private String code;

    private BigDecimal minBasketValue;

}
