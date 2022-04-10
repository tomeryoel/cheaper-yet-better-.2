package tryingCoupons.tryingCoupon.exceptions;

public class CustomerCouponException extends Exception{
    public CustomerCouponException(String message){
        super(message);
    }
    public CustomerCouponException(){
        super("problem accrued while trying to purchase");
    }
}
