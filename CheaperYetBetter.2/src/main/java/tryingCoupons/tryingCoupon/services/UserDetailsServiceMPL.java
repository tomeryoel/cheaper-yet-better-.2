package tryingCoupons.tryingCoupon.services;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import tryingCoupons.tryingCoupon.beans.Company;
import tryingCoupons.tryingCoupon.beans.Customer;
import tryingCoupons.tryingCoupon.beans.Roles;
import tryingCoupons.tryingCoupon.repositories.CompanyRepo;
import tryingCoupons.tryingCoupon.repositories.CustomerRepo;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
// this class takes username and translate it to user details


@Service
@RequiredArgsConstructor
public class UserDetailsServiceMPL implements UserDetailsService {
    private final CompanyRepo companyRepo;
    private final CompanyServiceMPL companyServiceMPL;
    private final AdminServicesService adminServicesMPL;
    private final CustomerServiceMPL customerServiceMPL;
    private final CustomerRepo customerRepo;
    private final BCryptPasswordEncoder passwordEncoder;
// the service takes a username and translate it to the fitting user details object
    // when it runs oll over the repositories of the users

    @Override
    @Transactional
    public UserDetails loadUserByUsername (String userName) throws UsernameNotFoundException {
        UserDetails userDetails;

        if (companyRepo.isCompanyExistsByEmail(userName)) {
            Company comapny = companyRepo.findByEmailLike(userName);
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_"+ Roles.COMPANY.name()));
            userDetails = new User(comapny.getEmail(), passwordEncoder.encode(comapny.getPassword()), authorities);
                companyServiceMPL.login(comapny.getEmail(), comapny.getPassword());




            return userDetails;
        }

        Customer customer = customerRepo.findByEmailLike(userName);
        if (customer != null) {
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_"+Roles.CUSTOMER.name()));
            userDetails = new User(customer.getEmail(), passwordEncoder.encode(customer.getPassword()), authorities);
            customerServiceMPL.login(customer.getEmail(),customer.getPassword());
            return userDetails;
        }




        if (userName.equals("admin@admin.com")) {
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_"+Roles.ADMIN.name()));
            userDetails = new User(userName ,passwordEncoder.encode("admin"),authorities);
            adminServicesMPL.login("admin@admin.com","admin");

            return userDetails;


        }

        else {
            throw new UsernameNotFoundException("not found");
        }
    }
}
