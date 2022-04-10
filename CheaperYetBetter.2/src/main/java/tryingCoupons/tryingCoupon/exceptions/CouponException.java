package tryingCoupons.tryingCoupon.exceptions;

public class CouponException extends Exception{
    public CouponException(){
        super("There is a problem with the coupon");
    }

    public CouponException(String someMassage){
        super(someMassage);
    }

}
