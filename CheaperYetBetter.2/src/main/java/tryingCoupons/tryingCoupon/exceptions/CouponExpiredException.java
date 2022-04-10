package tryingCoupons.tryingCoupon.exceptions;

public class CouponExpiredException extends Exception{
    public CouponExpiredException(){
        super("coupon is out of date!");
    }
}
