package tryingCoupons.tryingCoupon.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tryingCoupons.tryingCoupon.beans.CategoryInjection;
import tryingCoupons.tryingCoupon.beans.Coupon;

import javax.transaction.Transactional;
import java.sql.Date;
import java.util.List;

@Repository
public interface CouponRepo extends JpaRepository<Coupon,Integer> {
    @Query(value = "SELECT * FROM customer_vs_coupons INNER JOIN coupons\n" +
            "on coupons.id = customer_vs_coupons.coupon_id where\n" +
            "customer_vs_coupons.customer_id = ?",nativeQuery = true)
    List<Coupon> findCouponsBelongToCustomer(Integer id);

    @Query(value = "SELECT * FROM coupons WHERE id = ?", nativeQuery = true)
    Coupon getOneCoupon(int id);

    @Modifying
    @Query(value = "INSERT INTO customer_vs_coupons (coupon_id, customer_id) VALUES (?, ?)",nativeQuery = true)
    @Transactional
    void addCouponPurchase(int couponID, int customerID);

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 'true' ELSE 'false' END FROM coupons WHERE coupons.company_id = ? AND coupons.title = ?",nativeQuery = true)
    boolean isCompanyOwnTheCoupon(int id, String title);

    @Query(value = "SELECT coupons FROM Coupon coupons WHERE coupons.title = ?1 AND company_id = ?2")
    Coupon companyCoupon(String title, int CompanyId);

    @Query(value = "SELECT * FROM coupons WHERE company_id = ? AND category_id= ?", nativeQuery = true)
    List<Coupon> getCouponsByCompanyAndCategory(int companyId, int category);

    @Query(value = "SELECT * FROM coupons WHERE company_id= ? AND price < ?",nativeQuery = true)
    List<Coupon> getCouponsByMaxedPrice(int companyID, int price);

    @Query(value = "SELECT c FROM Coupon c WHERE company_id =?1")
    List<Coupon> getAllCompanyCoupons(int companyId);

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 'true' ELSE 'false' END FROM customer_vs_coupons WHERE customer_vs_coupons.coupon_id = ? AND customer_id = ?",nativeQuery = true)
    boolean isCustomerOwnCoupon(int couponId, int CustomerID);

    @Query(value = "SELECT * FROM customer_vs_coupons INNER JOIN coupons\n" +
            "on coupons.id = customer_vs_coupons.coupon_id where\n" +
            "customer_vs_coupons.customer_id = ? AND coupons.category_id = ?",nativeQuery = true)
    List<Coupon> couponsByCategoryAndCustomer(int customerID, int categoryID);

    @Query(value = "SELECT * FROM customer_vs_coupons INNER JOIN coupons\n" +
            "on coupons.id = customer_vs_coupons.coupon_id where\n" +
            "customer_vs_coupons.customer_id = ? AND coupons.price < ?",nativeQuery = true)
    List<Coupon> customerCouponsMaxPrice(int customerID, int maxPrice);

    @Modifying
    @Query(value = "DELETE FROM coupons WHERE end_date <= ?",nativeQuery = true)
    @Transactional
    void deleteCouponsByDate(Date expiredDate);

    @Query(value = "SELECT * FROM coupons WHERE end_date > ? AND amount > 0", nativeQuery = true)
    List<Coupon> getAllAvailableCoupons(Date expired);




}
