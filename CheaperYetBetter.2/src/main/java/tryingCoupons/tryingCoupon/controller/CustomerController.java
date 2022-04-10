package tryingCoupons.tryingCoupon.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tryingCoupons.tryingCoupon.beans.Coupon;
import tryingCoupons.tryingCoupon.exceptions.*;
import tryingCoupons.tryingCoupon.services.CustomerServiceMPL;

import java.util.List;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerServiceMPL customerServiceMPL;

//    @PostMapping("/customerLogin")
//    @ResponseStatus(HttpStatus.OK)
//    public void customerLogin(@RequestParam String email, @RequestParam String password) throws LoginException {
//        if(customerServiceMPL.login(email,password)){
//            System.out.println("ok");
//        }
//    }

    @PutMapping("/purchasecoupon/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void purchaseCoupon(@PathVariable int id) throws CouponOutOfAmountException, CouponException, CouponExpiredException, CustomerCouponException {
        customerServiceMPL.purchaseCoupons(id);
    }

    @GetMapping("/AllCustomerCoupon")
    @ResponseStatus(HttpStatus.OK)
    public List<Coupon> AllCustomerCoupons() throws CouponException {
        List<Coupon> coupons = customerServiceMPL.getAllCustomerCoupon();
        if(coupons.isEmpty()){
            throw new CouponException();
        }
        return coupons;
    }

    @GetMapping("/couponsCategory/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Coupon> AllCustomerCouponsByCategory(@PathVariable int categoryId) throws CouponException {
        return customerServiceMPL.getCustomerCouponByCategory(categoryId);
    }

    @GetMapping("/maxPrice/{maxPrice}")
    @ResponseStatus(HttpStatus.OK)
    public List<Coupon> couponsByMaxPrice(@PathVariable int maxPrice) throws CouponException {
        return customerServiceMPL.getCustomerCouponByMaxPriced(maxPrice);
    }

    @GetMapping("/customerDetails")
    @ResponseStatus(HttpStatus.OK)
    public String userDetails() throws CustomerException {
        return customerServiceMPL.getCustomerDetails();
    }

}
