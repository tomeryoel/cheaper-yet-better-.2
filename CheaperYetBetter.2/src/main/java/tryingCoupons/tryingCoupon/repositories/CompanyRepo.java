package tryingCoupons.tryingCoupon.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tryingCoupons.tryingCoupon.beans.Company;

import javax.transaction.Transactional;

@Repository
public interface CompanyRepo extends JpaRepository<Company,Integer> {
    Company findByEmailLike(String email);

    @Query(value = "SELECT * FROM companies WHERE BINARY email = ? AND password = ?", nativeQuery = true)
    Company findByEmailLikeAndPassword(String email,String password);

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 'true' ELSE 'false' END FROM companies WHERE BINARY companies.email = ? AND BINARY companies.password = ?", nativeQuery = true)
    boolean isCompanyExistsByEmailAndPassWord(String email,String password);
    @Query(value = "SELECT * FROM companies WHERE companies.id =?",nativeQuery = true)
    Company findOneCompany(int id);

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 'true' ELSE 'false' END FROM companies WHERE companies.name = ?" , nativeQuery = true)
    boolean isCompanyExistsByName(String name);

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 'true' ELSE 'false' END FROM companies WHERE companies.email = ?" , nativeQuery = true)
    boolean isCompanyExistsByEmail(String email);

    @Modifying
    @Query(value = "UPDATE companies SET email = ? , password = ? WHERE id = ?" ,nativeQuery = true)
    @Transactional
    void updateCompany(String email, String password,int id);
}
