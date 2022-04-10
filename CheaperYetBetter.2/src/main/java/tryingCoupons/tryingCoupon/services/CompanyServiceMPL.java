package tryingCoupons.tryingCoupon.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import tryingCoupons.tryingCoupon.beans.*;
import tryingCoupons.tryingCoupon.exceptions.*;
import tryingCoupons.tryingCoupon.repositories.CategoryRepo;
import tryingCoupons.tryingCoupon.repositories.CompanyRepo;
import tryingCoupons.tryingCoupon.repositories.CouponRepo;
import tryingCoupons.tryingCoupon.repositories.CustomerRepo;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

//TODO Do we need to throw an exception for not logged or it will be done in the jwt? i'll just leave this option open i guess.
//TODO Do I really need to throw the exception here


@Service
public class CompanyServiceMPL extends ClientService implements CompanyService{
    private int companyID;
    private Company thisCompany;
    private boolean isLogged = false;
    private String token;

    @Autowired
    private CategoryRepo categoryRepo;

    public CompanyServiceMPL(CompanyRepo COMPANY_REPO, CustomerRepo CUSTOMER_REPO, CouponRepo COUPON_REPO) {
        super(COMPANY_REPO, CUSTOMER_REPO, COUPON_REPO);
    }

    @Override
    public boolean login(String email, String password)  {


        boolean isExists = COMPANY_REPO.isCompanyExistsByEmailAndPassWord(email,password);
        if(!isExists){
            companyID = -1;
            isLogged = false;
            return false;

        }else {
            isLogged = true;
            thisCompany = COMPANY_REPO.findByEmailLikeAndPassword(email,password);
            companyID = thisCompany.getId();
            System.out.println("login successfully");
            return true;
        }
    }

    @Override
    public void addCoupon(Coupon coupon) throws CompanyException, CouponException, CouponOutOfAmountException, CouponExpiredException {
        if(companyID == -1){
            throw new CompanyException("company is not logged!");
        }
        coupon.setCompany_id_sql(thisCompany);
        if(coupon.getEnd_date().before(Date.valueOf(LocalDate.now()))||coupon.getEnd_date().equals(Date.valueOf(LocalDate.now()))){
            throw new CouponExpiredException();
        }
        if(coupon.getAmount()<1){
            throw new CouponOutOfAmountException();
        }
        if(COUPON_REPO.isCompanyOwnTheCoupon(companyID, coupon.getTitle())){
            throw new CouponException("coupon is already exists!");
        }
        Optional<CategoryInjection> categoryInjection = categoryRepo.findById(coupon.getCategory_id_bynum());
        if(categoryInjection.isPresent()){
            coupon.setCategory(categoryInjection.get());
        }else{
            throw new CouponException("coupon category does not exists!");
        }
        coupon.setCompanyId(companyID);
        COUPON_REPO.save(coupon);
        //coupon.setCouponID(COUPON_REPO.companyCoupon(coupon.getTitle(), companyID).getCoupon_id());
        System.out.println("Successfully added coupon");
    }

    @Override
    public void updateCoupon(Coupon coupon) throws CouponException {
        coupon.setCompanyId(companyID);
        Optional<Coupon> couponCheck = COUPON_REPO.findById(coupon.getCoupon_id());

        if(couponCheck.isPresent()){
            Coupon couponToCheck = couponCheck.get();
            System.out.println(couponToCheck);
            System.out.println(couponToCheck.getCompanyId());
            System.out.println(coupon.getCompanyId());
            if(couponToCheck.getCompanyId() == (coupon.getCompanyId())){
                COUPON_REPO.saveAndFlush(coupon);
                System.out.println("successfully updated");
            }
        }else{
            throw new CouponException("company does not own this coupon!");
        }
    }

    @Override
    public void deleteCoupon(int couponId) throws CouponException {
        Optional<Coupon> optional = COUPON_REPO.findById(couponId);
        Coupon coupon = null;
        if(optional.isPresent()){
            coupon = optional.get();
        }else{
                throw new CouponException("coupon does not exists!");
            }


        if(coupon.getCompany_id_sql().getId() == companyID){
            if(COUPON_REPO.existsById(coupon.getCoupon_id())){
                COUPON_REPO.deleteById(coupon.getCoupon_id());
                System.out.println("Deleted successfully");
            }else{
                throw new CouponException("Coupon does not belong to the company");
            }
        }
    }

    @Override
    public List<Coupon> getAllCompanyCoupons() throws CouponException {
        if(COUPON_REPO.getAllCompanyCoupons(companyID).isEmpty()){
            throw new CouponException("company does not own any coupons");
        }
        List<Coupon> coupons = COUPON_REPO.getAllCompanyCoupons(companyID);
        return COUPON_REPO.getAllCompanyCoupons(companyID);
    }

    @Override
    public List<Coupon> companyCouponsByCategory(int category) throws CouponException {
        List<Coupon> coupons = COUPON_REPO.getCouponsByCompanyAndCategory(companyID,category);
        if(coupons.isEmpty()){
            throw new CouponException("requested coupons are not exists");
        }
        return coupons;
    }

    @Override
    public List<Coupon> companyCouponsByMaxPrice(int maxPrice) throws CouponException {
        List<Coupon> coupons = COUPON_REPO.getCouponsByMaxedPrice(companyID,maxPrice);
        if(coupons.isEmpty()){
            throw new CouponException("requested coupons are not exists");
        }
        return coupons;
    }

    @Override
    public String companyDetails() throws LoginException {
        if(thisCompany == null){
            throw new LoginException();
        }

        thisCompany.setCoupons(new HashSet<>(COUPON_REPO.getAllCompanyCoupons(companyID)));
        return thisCompany.toString() +"  "+  thisCompany.getCoupons().toString();
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
            authorities.add(new SimpleGrantedAuthority("ROLE_"+ Roles.COMPANY.name()));
            User userDetails = new User(thisCompany.getEmail(), thisCompany.getPassword(), authorities);
            token = JWT.create().withSubject(userDetails.getUsername()).withExpiresAt(new Date(System.currentTimeMillis() + 30*1000))
                    .withClaim("authorities", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())).sign(algorithm);
        }
        return token;
    }

    public Company getThisCompany() {
        return thisCompany;
    }
}
