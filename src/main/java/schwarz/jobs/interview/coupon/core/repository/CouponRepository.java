package schwarz.jobs.interview.coupon.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import schwarz.jobs.interview.coupon.core.domain.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {


}
