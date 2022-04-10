package tryingCoupons.tryingCoupon.exceptions;

public class CompanyException extends Exception {
    public CompanyException(){
        super("company is already exists");
    }
    public CompanyException(String message){
        super(message);
    }
}
