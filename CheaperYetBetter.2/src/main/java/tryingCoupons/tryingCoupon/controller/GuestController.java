package tryingCoupons.tryingCoupon.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import tryingCoupons.tryingCoupon.beans.Coupon;
import tryingCoupons.tryingCoupon.services.AdminService;

import java.util.List;



@RestController
@RequestMapping("/guest")
@RequiredArgsConstructor
public class GuestController {

    private final AdminService adminService;

    @GetMapping("/allAvailableCoupons")
    @ResponseStatus(HttpStatus.OK)
    public List<Coupon> getAllCoupons(){
        return adminService.findAllAvailableCoupons();
    }
}
