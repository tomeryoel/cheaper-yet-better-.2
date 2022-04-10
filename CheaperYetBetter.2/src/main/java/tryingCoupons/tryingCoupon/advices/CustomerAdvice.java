package tryingCoupons.tryingCoupon.advices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import tryingCoupons.tryingCoupon.exceptions.*;

@RestController
@ControllerAdvice
public class CustomerAdvice {

    @ExceptionHandler(value = {LoginException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDetails handleLoginException(Exception err){
        return new ErrorDetails(err.getClass().getName(),err.getMessage());
    }

    @ExceptionHandler(value = {CouponException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDetails handleCouponException(Exception err){
        return new ErrorDetails(err.getClass().getName(),err.getMessage());
    }

    @ExceptionHandler(value = {CouponOutOfAmountException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDetails handleCouponAmountException(Exception err){
        return new ErrorDetails(err.getClass().getName(),err.getMessage());
    }

    @ExceptionHandler(value = {CouponExpiredException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDetails handleCouponExpiredException(Exception err){
        return new ErrorDetails(err.getClass().getName(),err.getMessage());
    }

    @ExceptionHandler(value = {CustomerCouponException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDetails handleCustomerCouponException(Exception err){
        return new ErrorDetails(err.getClass().getName(),err.getMessage());
    }

    @ExceptionHandler(value = {CustomerException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDetails handleCustomerException(Exception err){
        return new ErrorDetails(err.getClass().getName(),err.getMessage());
    }



}
