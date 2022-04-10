package tryingCoupons.tryingCoupon.services;

import tryingCoupons.tryingCoupon.beans.Coupon;
import tryingCoupons.tryingCoupon.exceptions.*;

import java.util.List;

public interface CustomerService {
    public void purchaseCoupons(int couponId) throws CustomerCouponException, CouponException, CouponOutOfAmountException, CouponExpiredException;
    public List<Coupon> getAllCustomerCoupon() throws CouponException;
    public List<Coupon> getCustomerCouponByCategory(int categoryId) throws CouponException;
    public List<Coupon> getCustomerCouponByMaxPriced(int maxPrice) throws CouponException;
    public String getCustomerDetails() throws CustomerException;
    public List<Coupon> findAllAvailableCoupons();
    public void logOut();
}
