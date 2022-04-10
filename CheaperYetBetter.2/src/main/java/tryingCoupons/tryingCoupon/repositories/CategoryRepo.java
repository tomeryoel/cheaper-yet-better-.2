package tryingCoupons.tryingCoupon.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tryingCoupons.tryingCoupon.beans.CategoryInjection;
//transferring the JAVA kode to SQL database language.
public interface CategoryRepo extends JpaRepository<CategoryInjection,Integer> {
}
