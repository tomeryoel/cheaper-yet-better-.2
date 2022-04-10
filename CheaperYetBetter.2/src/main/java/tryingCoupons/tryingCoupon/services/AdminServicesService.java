package tryingCoupons.tryingCoupon.services;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import tryingCoupons.tryingCoupon.beans.Company;
import tryingCoupons.tryingCoupon.beans.Coupon;
import tryingCoupons.tryingCoupon.beans.Customer;
import tryingCoupons.tryingCoupon.beans.Roles;
import tryingCoupons.tryingCoupon.exceptions.CompanyException;
import tryingCoupons.tryingCoupon.exceptions.CustomerException;
import tryingCoupons.tryingCoupon.exceptions.LoginException;
import tryingCoupons.tryingCoupon.repositories.CompanyRepo;
import tryingCoupons.tryingCoupon.repositories.CouponRepo;
import tryingCoupons.tryingCoupon.repositories.CustomerRepo;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class AdminServicesService extends ClientService implements AdminService {
    private final String EMAIL = "admin@admin.com";
    private final String PASSWORD = "admin";
    private String token;
    boolean isLogged = false;


    public AdminServicesService(CompanyRepo COMPANY_REPO, CustomerRepo CUSTOMER_REPO, CouponRepo COUPON_REPO) {
        super(COMPANY_REPO, CUSTOMER_REPO, COUPON_REPO);
    }

    @Override
    public boolean login(String email, String password)  {
        if(!(this.EMAIL.equals(email) && this.PASSWORD.equals(password))){
            isLogged = false;
            return false;
        }
        else{
            isLogged = true;
            System.out.println("successful login");
            return true;
        }
    }

    @Override
    public void addCompany(Company company) throws CompanyException {
        if(COMPANY_REPO.isCompanyExistsByName(company.getName()) || COMPANY_REPO.isCompanyExistsByEmail(company.getEmail())){
            throw new CompanyException();
        }
        else{
            COMPANY_REPO.save(company);
            //company.setId(COMPANY_REPO.findByNameLike(company.getName()).getId());
            System.out.println("company is successfully added id: " + company.getId() );

        }
    }

    @Override
    public void updateCompany(Company company) throws CompanyException {
        if(COMPANY_REPO.existsById(company.getId())){
            COMPANY_REPO.updateCompany(company.getEmail(), company.getPassword(), company.getId());
            System.out.println("successfully updated company id: " + company.getId());
        }
        else{
            throw new CompanyException("Company does not exists!");
        }
    }

    @Override
    public void deleteCompany(int id) throws CompanyException {
        if(COMPANY_REPO.existsById(id)){
            COMPANY_REPO.deleteById(id);
            System.out.printf("Company %d is successfully deleted", id );
        }
        else{
            System.out.println();
            throw new CompanyException("Company does not exist");
        }
    }

    @Override
    public List<Company> getAllCompanies() throws CompanyException {
        if(COMPANY_REPO.findAll().isEmpty()){
            throw new CompanyException("There are now companies exits!");
        }
        return COMPANY_REPO.findAll();
    }

    @Override
    public Company getCompanyByID(int id) throws CompanyException {
        Company company = COMPANY_REPO.findOneCompany(id);
        if(company == null){
            throw new CompanyException("company does not exists");
        }
        return company;
    }

    @Override
    public void addCustomer(Customer customer) throws CustomerException {
        if(CUSTOMER_REPO.isCustomerExists(customer.getEmail(), customer.getPassword())){
            throw new CustomerException("customer is already exists");
        }
        else if(CUSTOMER_REPO.findByEmailLike(customer.getEmail()) != null){
            throw new CustomerException("Email is already exists in the system!");
        }
        else{
            CUSTOMER_REPO.save(customer);
            System.out.println("Customer is successfully saved! id: " + customer.getId());
        }
    }

    @Override
    public void updateCustomer(Customer customer) throws CustomerException {
        if(CUSTOMER_REPO.existsById(customer.getId())){
            CUSTOMER_REPO.updateCustomer(customer.getEmail(), customer.getFirstName(), customer.getLastName(), customer.getPassword(), customer.getId());
            System.out.println("customer is successfully updated id: " + customer.getId());
        }
        else{
            throw new CustomerException("customer does not exists!");
        }
    }

    @Override
    public void deleteCustomer(int id) throws CustomerException {
        if(CUSTOMER_REPO.existsById(id)){
            CUSTOMER_REPO.deleteCustomerCoupons(id);
            CUSTOMER_REPO.deleteById(id);
            System.out.println("successfully deleted customer : " + id);
        }
        else{
            throw new CustomerException("customer does not exist");
        }
    }

    @Override
    public List<Customer> getAllCustomer() throws CustomerException {
        if(CUSTOMER_REPO.findAll().isEmpty()){
            throw new CustomerException("there are no customers!");
        }
        return CUSTOMER_REPO.findAll();
    }

    @Override
    public Customer findCustomerById(int id) throws CustomerException {
        Optional<Customer> customer = CUSTOMER_REPO.findById(id);
        if(customer.isEmpty()){
            throw new CustomerException("customer does not exists");
        }
        else{
            return customer.get();
        }
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


    public String getToken() {
        return token;
    }

    public boolean isLogged() {
        if(isLogged){
            Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());// security protocol level "256"
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();//The list of our authorities in the projects
            authorities.add(new SimpleGrantedAuthority("ROLE_"+ Roles.ADMIN.name()));
            User userDetails = new User("admin@admin.com", "admin",authorities);

            token = JWT.create().withSubject(userDetails.getUsername()).withExpiresAt(new Date(System.currentTimeMillis() +  30 * 1000))
                    .withClaim("authorities", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())).sign(algorithm);
        }
        return isLogged;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }
}
