package tryingCoupons.tryingCoupon.exceptions;

public class CouponOutOfAmountException extends Exception{
    public CouponOutOfAmountException(){
        super("Coupon is out of amount!");
    }
}
