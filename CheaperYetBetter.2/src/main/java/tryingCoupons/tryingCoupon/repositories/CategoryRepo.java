package tryingCoupons.tryingCoupon.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tryingCoupons.tryingCoupon.beans.CategoryInjection;

public interface CategoryRepo extends JpaRepository<CategoryInjection,Integer> {
}
