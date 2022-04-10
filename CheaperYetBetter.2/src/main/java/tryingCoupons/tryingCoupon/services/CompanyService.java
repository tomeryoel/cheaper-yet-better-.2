package tryingCoupons.tryingCoupon.services;

import tryingCoupons.tryingCoupon.beans.Coupon;
import tryingCoupons.tryingCoupon.exceptions.*;

import java.util.List;

public interface CompanyService {
    public void addCoupon(Coupon coupon) throws CompanyException, CouponException, CouponOutOfAmountException, CouponExpiredException;
    public void updateCoupon(Coupon coupon) throws CouponException;
    public void deleteCoupon(int couponId) throws CouponException;
    public List<Coupon> getAllCompanyCoupons() throws CouponException;
    public List<Coupon> companyCouponsByCategory(int category) throws CouponException;
    public List<Coupon> companyCouponsByMaxPrice(int maxPrice) throws CouponException;
    public String companyDetails() throws LoginException;
    public void logOut();
}
