package schwarz.jobs.interview.coupon.core.services;

import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import schwarz.jobs.interview.coupon.core.domain.Coupon;
import schwarz.jobs.interview.coupon.core.repository.CouponRepository;
import schwarz.jobs.interview.coupon.core.services.model.Basket;
import schwarz.jobs.interview.coupon.web.dto.ApplicationRequestDTO;
import schwarz.jobs.interview.coupon.web.dto.CouponDTO;
import schwarz.jobs.interview.coupon.web.dto.CouponRequestDTO;

import javax.validation.Valid;
@Slf4j
@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    public Optional<Coupon> getCoupon(final String code) {
        return couponRepository.findByCode(code);
    }

    public Optional<Basket> apply(@Valid  ApplicationRequestDTO applicationRequestDTO) {
        final String code = applicationRequestDTO.getCode();
        final Basket basket = applicationRequestDTO.getBasket();
        return getCoupon(code).map(coupon -> {
            if (basket.getValue().doubleValue() >0) {
                basket.applyDiscount(coupon.getDiscount());
                basket.setApplicationSuccessful(true);
            }else if (basket.getValue().doubleValue() == 0) {
                return basket;
            }else{
                log.error("DEBUG: TRIED TO APPLY NEGATIVE DISCOUNT!");

                throw new RuntimeException("Can't apply negative discounts");
            }
/*
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
*/
            return basket;
        });
    }
    public Coupon createCoupon(@Valid final CouponDTO couponDTO) {

        Coupon  coupon = Coupon.builder()
                .code(couponDTO.getCode().toLowerCase())
                .discount(couponDTO.getDiscount())
                .minBasketValue(couponDTO.getMinBasketValue())
                .build();
        return couponRepository.save(coupon);

    }
/*
    public Coupon createCoupon(final CouponDTO couponDTO) {

        Coupon coupon = null;

        try {
            coupon = Coupon.builder()
                .code(couponDTO.getCode().toLowerCase())
                .discount(couponDTO.getDiscount())
                .minBasketValue(couponDTO.getMinBasketValue())
                .build();

        } catch (final NullPointerException e) {

            // Don't coupon when code is null
        }

        return couponRepository.save(coupon);
    }
*/

    public List<Coupon> getCoupons(@Valid final CouponRequestDTO couponRequestDTO) {

       return couponRepository.findByCodeIn(couponRequestDTO.getCodes());

    }
    /*
    public List<Coupon> getCoupons(final CouponRequestDTO couponRequestDTO) {

        final ArrayList<Coupon> foundCoupons = new ArrayList<>();

        couponRequestDTO.getCodes().forEach(code -> foundCoupons.add(couponRepository.findByCode(code).get()));

        return foundCoupons;
    }
    */
}
