package tryingCoupons.tryingCoupon.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import tryingCoupons.tryingCoupon.beans.Coupon;
import tryingCoupons.tryingCoupon.beans.Customer;
import tryingCoupons.tryingCoupon.beans.Roles;
import tryingCoupons.tryingCoupon.exceptions.*;
import tryingCoupons.tryingCoupon.repositories.CompanyRepo;
import tryingCoupons.tryingCoupon.repositories.CouponRepo;
import tryingCoupons.tryingCoupon.repositories.CustomerRepo;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Getter
public class CustomerServiceMPL extends ClientService implements CustomerService{
    //TODO do I need to throw here a Login exception to each methods or it will be done with jwt?? is CUSTOMER_ID necessary?
    private int customerId;
    private Customer thisCustomer;
    private boolean isLogged = false;
    private String token;

    public CustomerServiceMPL(CompanyRepo COMPANY_REPO, CustomerRepo CUSTOMER_REPO, CouponRepo COUPON_REPO) {
        super(COMPANY_REPO, CUSTOMER_REPO, COUPON_REPO);
    }

    @Override
    public boolean login(String email, String password) {
        if(!CUSTOMER_REPO.isCustomerExists(email,password)){
            customerId = -1;
            thisCustomer = null;
            return false;
        }else {
            isLogged = true;
            thisCustomer = CUSTOMER_REPO.findByEmailLike(email);
            customerId = thisCustomer.getId();
            System.out.println("customer successfully logged!");
            return true;
        }
    }

    @Override
    public void purchaseCoupons(int couponId) throws CustomerCouponException, CouponException, CouponOutOfAmountException, CouponExpiredException {
        Coupon couponToPurchase;
        if(COUPON_REPO.existsById(couponId)){
            couponToPurchase = COUPON_REPO.findById(couponId).get();
        }else{
            throw new CouponException("coupon does now exists!");
        }
        //does customer already own this coupon?
    if(COUPON_REPO.isCustomerOwnCoupon(couponId,customerId)){
        throw new CustomerCouponException("customer already own this coupon");
    }

    if(couponToPurchase.getAmount() < 1){
        throw new CouponOutOfAmountException();
    }

    if(couponToPurchase.getEnd_date().before(Date.valueOf(LocalDate.now())) || couponToPurchase.getEnd_date().equals(Date.valueOf(LocalDate.now()))){
        throw new CouponExpiredException();
    }

    COUPON_REPO.addCouponPurchase(couponId,customerId);
    couponToPurchase.setAmount(couponToPurchase.getAmount()-1);
    COUPON_REPO.saveAndFlush(couponToPurchase);
    System.out.println("successfully purchased");

    }

    @Override
    public List<Coupon> getAllCustomerCoupon() throws CouponException {
        if(COUPON_REPO.findCouponsBelongToCustomer(customerId).isEmpty()){
            throw new CouponException("coupons are not exists for this customer");
        }

        return COUPON_REPO.findCouponsBelongToCustomer(customerId);
    }

    @Override
    public List<Coupon> getCustomerCouponByCategory(int categoryId) throws CouponException {
        if(COUPON_REPO.couponsByCategoryAndCustomer(customerId,categoryId).isEmpty()){
            throw new CouponException("coupons are not exists for this customer");
        }
        return COUPON_REPO.couponsByCategoryAndCustomer(customerId,categoryId);
    }

    @Override
    public List<Coupon> getCustomerCouponByMaxPriced(int maxPrice) throws CouponException {
        if(COUPON_REPO.customerCouponsMaxPrice(customerId,maxPrice).isEmpty()){
            throw new CouponException("coupons are not exists for this customer");
        }
        return COUPON_REPO.customerCouponsMaxPrice(customerId,maxPrice);
    }

    @Override
    public String getCustomerDetails() throws CustomerException {
        if(thisCustomer == null){
            throw new CustomerException("customer does not exists");
        }

        try {
            thisCustomer.setCoupons(new HashSet<>(getAllCustomerCoupon()));
        } catch (CouponException e) {
            System.out.println(e.getMessage());
        }
        return thisCustomer.toString() + thisCustomer.getCoupons().toString();
    }

    @Override
    public List<Coupon> findAllAvailableCoupons() {
        return COUPON_REPO.getAllAvailableCoupons(Date.valueOf(LocalDate.now()));
    }

    @Override
    public void logOut(){
        isLogged = false;
        token = null;
    }


    public boolean isLogged() {
        return isLogged;
    }

    public String getToken() {
        if(isLogged){
            Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_"+ Roles.CUSTOMER.name()));
            User userDetails = new User(thisCustomer.getEmail(), thisCustomer.getPassword(),authorities);
            token = JWT.create().withSubject(userDetails.getUsername()).withExpiresAt(new Date(System.currentTimeMillis() +15 *60000))
                    .withClaim("authorities", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())).sign(algorithm);
        }
        return token;
    }

    public Customer getThisCustomer() {
        return thisCustomer;
    }
}
