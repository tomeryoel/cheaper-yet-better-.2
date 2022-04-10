package tryingCoupons.tryingCoupon.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import tryingCoupons.tryingCoupon.beans.Customer;

import javax.transaction.Transactional;

public interface CustomerRepo extends JpaRepository<Customer,Integer> {
    Customer findByFirstNameLike(String first_name);

    Customer findByEmailLike(String email);

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 'true' ELSE 'false' END FROM customer WHERE BINARY customer.email = ? AND BINARY customer.password = ?", nativeQuery = true)
    boolean isCustomerExists(String email, String password);

    @Query(value = "SELECT * FROM customer WHERE id = ?", nativeQuery = true)
    Customer getOneCustomer(int id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM customer_vs_coupons WHERE customer_id = ?",nativeQuery = true)
    void deleteCustomerCoupons(int customerId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE customer SET email = ?, first_name =? , last_name = ?, password = ? WHERE id = ?",nativeQuery = true)
    void updateCustomer(String email,String firstName, String lastName, String password, int id);


}
