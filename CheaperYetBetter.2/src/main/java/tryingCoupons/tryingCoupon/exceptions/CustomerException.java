package tryingCoupons.tryingCoupon.exceptions;

public class CustomerException extends Exception {
    public CustomerException(){
        super("customer is already exists");
    }

    public CustomerException(String message){
        super(message);
    }
}
