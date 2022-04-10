package tryingCoupons.tryingCoupon.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "companies")
/**
 * ALL THE PARAMETERS FOR COMPANY
 * @param id  - Company id
 * @param name - company name
 * @param email -  company email
 * @param password - company password
 * @param coupons - company coupons
 */
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Setter(AccessLevel.PRIVATE)
    private Integer id;
    @Column(name = "name",nullable = false,length = 40)
    private String name;
    @Column(name = "email",nullable = false,length = 40)
    private String email;
    @Column(name = "password",nullable = false,length = 40)
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "company_id_sql",orphanRemoval = true,fetch = FetchType.EAGER)
    private Set<Coupon> coupons = new HashSet<>();

    /**
     * this is the constructor of company
     * @param name - company name
     * @param email - company email
     * @param password - company password
     * @param coupons -  company coupons
     */
     public Company(String name, String email, String password, Set<Coupon> coupons){
        setName(name);
        setEmail(email);
        setPassword(password);
        setCoupons(coupons);
     }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", password='" + getPassword() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Company company = (Company) o;
        return id != null && Objects.equals(id, company.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


}

