package schwarz.jobs.interview.coupon.web;


import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import schwarz.jobs.interview.coupon.core.domain.Coupon;
import schwarz.jobs.interview.coupon.core.services.CouponService;
import schwarz.jobs.interview.coupon.core.services.model.Basket;
import schwarz.jobs.interview.coupon.web.dto.ApplicationRequestDTO;
import schwarz.jobs.interview.coupon.web.dto.CouponDTO;
import schwarz.jobs.interview.coupon.web.dto.CouponRequestDTO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coupon")

public class CouponResource {
    @Autowired
    private final CouponService couponService;

    /**
     * @ApiOperation(value = "Applies currently active promotions and coupons from the request to the requested Basket - Version 1")
     * @param applicationRequestDTO
     * @return
     */
    @PostMapping(value = "/apply")
    public ResponseEntity<Basket> apply(
        //@ApiParam(value = "Provides the necessary basket and customer information required for the coupon application", required = true)
        @RequestBody @Valid final ApplicationRequestDTO applicationRequestDTO) {


        final Optional<Basket> basket =
            couponService.apply(applicationRequestDTO.getBasket(), applicationRequestDTO.getCode());

        if (basket.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (!applicationRequestDTO.getBasket().isApplicationSuccessful()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return ResponseEntity.ok().body(applicationRequestDTO.getBasket());
    }

    @PostMapping("/create")
    public ResponseEntity<Coupon> create(@RequestBody @Valid final CouponDTO couponDTO) {

        final Coupon coupon = couponService.createCoupon(couponDTO);

        return ResponseEntity.ok().build(coupon);
    }

    @GetMapping("/coupons")
    public List<Coupon> getCoupons(@RequestBody @Valid final CouponRequestDTO couponRequestDTO) {

        return couponService.getCoupons(couponRequestDTO);
    }
}
