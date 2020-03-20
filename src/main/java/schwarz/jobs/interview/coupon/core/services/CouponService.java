package schwarz.jobs.interview.coupon.core.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import schwarz.jobs.interview.coupon.core.domain.Coupon;
import schwarz.jobs.interview.coupon.core.repository.CouponRepository;
import schwarz.jobs.interview.coupon.core.services.model.Basket;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    public Optional<Coupon> getCoupon(final String code) {
        return couponRepository.findByCode(code);
    }

    public Optional<Basket> apply(final Basket basket, final String code) {

        return getCoupon(code).map(coupon -> {

            if (basket.getValue().compareTo(coupon.getMinBasketValue()) > 0) {

                basket.applyDiscount(coupon.getDiscount());
            }
            return basket;
        });
    }

}
