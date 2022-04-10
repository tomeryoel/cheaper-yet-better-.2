package tryingCoupons.tryingCoupon.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "first_name",nullable = false,length = 40)
    private String firstName;
    @Column(name = "last_name",nullable = false,length = 40)
    private String lastName;
    @Column(name = "email",nullable = false,length = 40)
    private String email;
    @Column(name = "password",nullable = false,length = 40)
    private String password;

    @JsonIgnore
    @ManyToMany(mappedBy = "customers",fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Singular
    private Set<Coupon> coupons = new HashSet<>();

    public Customer(String firstName, String lastName, String email,String password){
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setPassword(password);
    }

    public void removeCoupon(Coupon coupon){
        coupons.remove(coupon);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Customer customer = (Customer) o;
        return id != null && Objects.equals(id, customer.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", coupons=" + coupons +
                '}';
    }
}
