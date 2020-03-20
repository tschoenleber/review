package schwarz.jobs.interview.coupon.web;


import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.RequiredArgsConstructor;
import schwarz.jobs.interview.coupon.core.services.CouponService;
import schwarz.jobs.interview.coupon.core.services.model.Basket;
import schwarz.jobs.interview.coupon.web.dto.ApplicationRequestDTO;

@Controller
@RequiredArgsConstructor
public class CouponResource {

    private final CouponService couponService;

    //@ApiOperation(value = "Applies currently active promotions and coupons from the request to the requested Basket - Version 1")
    @PostMapping(value = "/apply")
    public ResponseEntity<Basket> apply(
    //@ApiParam(value = "Provides the necessary basket and customer information required for the coupon application", required = true)
    @RequestBody @Valid final ApplicationRequestDTO applicationRequestDTO) {

        final Optional<Basket> basket =
        couponService.apply(applicationRequestDTO.getBasket(), applicationRequestDTO.getCode());

        if (!basket.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        if (!applicationRequestDTO.getBasket().isApplicationSuccessful()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return ResponseEntity.ok().body(applicationRequestDTO.getBasket());
    }
}
