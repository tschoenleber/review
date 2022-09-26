package schwarz.jobs.interview.coupon.core.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import schwarz.jobs.interview.coupon.core.domain.Coupon;
import schwarz.jobs.interview.coupon.core.exception.AppException;
import schwarz.jobs.interview.coupon.core.repository.CouponRepository;
import schwarz.jobs.interview.coupon.core.services.model.Basket;
import schwarz.jobs.interview.coupon.web.dto.CouponDTO;
import schwarz.jobs.interview.coupon.web.dto.CouponRequestDTO;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponService {
   @Autowired
    private final CouponRepository couponRepository;

    public Optional<Coupon> getCoupon(final String code) {
        return couponRepository.findByCode(code);
    }

    public Optional<Basket> apply(final Basket basket, final String code) {
        /* 
                return getCoupon(code).map(coupon -> {

            if (basket.getValue().doubleValue() >= 0) {

                if (basket.getValue().doubleValue() > 0) {

                    basket.applyDiscount(coupon.getDiscount());

                } else if (basket.getValue().doubleValue() == 0) {
                    return basket;
                }

            } else {
                System.out.println("DEBUG: TRIED TO APPLY NEGATIVE DISCOUNT!");
                throw new RuntimeException("Can't apply negative discounts");
            }

            return basket;
        });
        */

        Optional<Coupon> coupon = getCoupon(code);

		if (!coupon.isPresent()) {
            log.error("Given Coupon code not found"+code);
			throw new AppException("CouponNotFound", "Given Coupon code not found");
		}

		if (basket == null) {
            
            log.error("Basket is null");
			throw new AppException("BasketNotFound", "Basket is null");
		}

		double basketValue = basket.getValue().doubleValue();

		if (basketValue <= 0) {
            log.error("Basket value should be a positive");
			throw new AppException("DiscountNegative", "Basket value should be a positive");
		}

		if (basket.getValue().compareTo(coupon.get().getMinBasketValue()) > 0) {

			throw new AppException("CouponMinValue", "Basket value should be greater  value required for Coupon");
		}
		return(coupon.map(couponObj -> {
			basket.applyDiscount(couponObj.getDiscount());            
			return basket;
		}));
    }

    public Coupon createCoupon(final CouponDTO couponDTO) {

        Coupon coupon = null;

        try {
            coupon = Coupon.builder()
                .code(couponDTO.getCode().toLowerCase())
                .discount(couponDTO.getDiscount())
                .minBasketValue(couponDTO.getMinBasketValue())
                .build();

        } catch (final NullPointerException e) {

            throw new AppException("CouponNull", "Coupon Code is null");
        }

        return couponRepository.save(coupon);
    }

    public List<Coupon> getCoupons(final CouponRequestDTO couponRequestDTO) {

        final ArrayList<Coupon> foundCoupons = new ArrayList<>();

        couponRequestDTO.getCodes().forEach(code -> foundCoupons.add(couponRepository.findByCode(code).get()));

        return foundCoupons;
    }
}
