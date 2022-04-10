package tryingCoupons.tryingCoupon.exceptions;

public class LoginException extends Exception{
    public LoginException(String describeERR){
        super(describeERR);
    }

    public LoginException(){
        super("User name or password are wrong");
    }
}
