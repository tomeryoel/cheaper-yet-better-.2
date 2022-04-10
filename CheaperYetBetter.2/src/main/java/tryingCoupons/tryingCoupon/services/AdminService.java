package tryingCoupons.tryingCoupon.services;

import tryingCoupons.tryingCoupon.beans.Company;
import tryingCoupons.tryingCoupon.beans.Coupon;
import tryingCoupons.tryingCoupon.beans.Customer;
import tryingCoupons.tryingCoupon.exceptions.CompanyException;
import tryingCoupons.tryingCoupon.exceptions.CustomerException;

import java.util.List;

public interface AdminService {
    public void addCompany(Company company) throws CompanyException;
    public void updateCompany(Company company) throws CompanyException;
    public void deleteCompany(int id) throws CompanyException;
    public List<Company> getAllCompanies() throws CompanyException;
    public Company getCompanyByID(int id) throws  CompanyException;
    public void addCustomer(Customer customer) throws CustomerException;
    public void updateCustomer(Customer customer) throws CustomerException;
    public void deleteCustomer(int id) throws CustomerException;
    public List<Customer> getAllCustomer() throws CustomerException;
    public Customer findCustomerById(int id) throws CustomerException;
    public List<Coupon> findAllAvailableCoupons();
    public void logOut();

}
