package tryingCoupons.tryingCoupon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import tryingCoupons.tryingCoupon.beans.UserProp;
import tryingCoupons.tryingCoupon.exceptions.LoginException;
import tryingCoupons.tryingCoupon.services.AdminServicesService;
import tryingCoupons.tryingCoupon.services.CompanyServiceMPL;
import tryingCoupons.tryingCoupon.services.CustomerServiceMPL;

@RestController
@RequestMapping("/token")
//@CrossOrigin
@RequiredArgsConstructor
public class GetTokenController {
    private final CompanyServiceMPL companyServiceMPL;
    private final CustomerServiceMPL customerServiceMPL;
    private final AdminServicesService adminServicesMPL;

    @GetMapping("/getToken")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String getToken() throws LoginException {
        if(adminServicesMPL.isLogged()){
            return "Bearer " + adminServicesMPL.getToken();
        }

        if(companyServiceMPL.isLogged()){
            return "Bearer " + companyServiceMPL.getToken();
        }

        if(customerServiceMPL.isLogged()){
            return "Bearer " + customerServiceMPL.getToken();
        }

        else{
            throw new LoginException("Please log in first!");
        }
    }


    @RequestMapping(value = "/lognout", method = RequestMethod.GET)
    public ModelAndView logOut() throws LoginException {
        if(adminServicesMPL.isLogged()){
            adminServicesMPL.logOut();
            System.out.println("in admin logout");
            return new ModelAndView("redirect:" + "http://localhost:8080/logout");
        }

        if(companyServiceMPL.isLogged()){
            companyServiceMPL.logOut();
            System.out.println("in company log out");
            return new ModelAndView("redirect:" + "http://localhost:8080/logout");
        }

        if(customerServiceMPL.isLogged()){
            customerServiceMPL.logOut();
            System.out.println("in customer log out");
            return new ModelAndView("redirect:" + "http://localhost:8080/logout");
        }

        else{
            throw new LoginException("Please log in first!");

        }
    }

    @RequestMapping(value = "/log", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public boolean log(@RequestBody UserProp user) throws LoginException {
        System.out.println(user);
        if(adminServicesMPL.login(user.getUsername(),user.getPassword())){
            System.out.println("im here :)");
            return true;
        }
        else if(companyServiceMPL.login(user.getUsername(), user.getPassword())){
            return true;
        }
        else if(customerServiceMPL.login(user.getUsername(), user.getPassword())){
            return true;
        }else{
            throw  new LoginException("wrong username or password");
        }

    }

}
