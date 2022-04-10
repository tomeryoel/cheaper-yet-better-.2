package tryingCoupons.tryingCoupon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tryingCoupons.tryingCoupon.beans.Coupon;
import tryingCoupons.tryingCoupon.exceptions.*;
import tryingCoupons.tryingCoupon.services.CompanyServiceMPL;

import java.util.List;

@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyServiceMPL companyServiceMPL;
//
//    @PostMapping("/login/{email}/{password}")
//    public void login(@PathVariable String email, @PathVariable String password) throws LoginException {
//        companyServiceMPL.login(email,password);
//    }

    @PostMapping("addCoupon")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void addCoupon(@RequestBody Coupon coupon) throws CouponOutOfAmountException, CouponException, CouponExpiredException, CompanyException {
        companyServiceMPL.addCoupon(coupon);
    }

    @PutMapping("/updateCoupon")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateCoupon(@RequestBody Coupon coupon) throws CouponException {
        companyServiceMPL.updateCoupon(coupon);
    }

    @DeleteMapping("/deleteCoupon/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteCoupon(@PathVariable int id) throws CouponException {
        companyServiceMPL.deleteCoupon(id);
    }


    @GetMapping("/allCompanyCoupon")
    @ResponseStatus(HttpStatus.OK)
    public List<Coupon> allCompanyCoupons() throws CouponException {
        return companyServiceMPL.getAllCompanyCoupons();
    }

    @GetMapping("/companyCategory/{categoryID}")
    @ResponseStatus(HttpStatus.OK)
    public List<Coupon> companyCouponByCategory(@PathVariable int categoryID) throws CouponException {
        return companyServiceMPL.companyCouponsByCategory(categoryID);
    }

    @GetMapping("/companyCategoryMaxPrice/{price}")
    @ResponseStatus(HttpStatus.OK)
    public List<Coupon> companyCouponsByMaxPrice(@PathVariable int price) throws CouponException {
        return companyServiceMPL.companyCouponsByMaxPrice(price);
    }

    @GetMapping("/getCompanyDetails")
    @ResponseStatus(HttpStatus.OK)
    public String companyDetails() throws LoginException {
        return companyServiceMPL.companyDetails();
    }

}
