package tryingCoupons.tryingCoupon.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tryingCoupons.tryingCoupon.beans.Company;
import tryingCoupons.tryingCoupon.beans.Customer;
import tryingCoupons.tryingCoupon.exceptions.CompanyException;
import tryingCoupons.tryingCoupon.exceptions.CustomerException;
import tryingCoupons.tryingCoupon.exceptions.LoginException;
import tryingCoupons.tryingCoupon.services.AdminServicesService;

import java.util.List;

//responsible for the "crud" requests

// send out the connection to the backend/ create paths between the program and the database
//building the crud

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {


    private final AdminServicesService adminServicesMPL;

//    @PostMapping("/login")
//    @ResponseStatus(HttpStatus.ACCEPTED)
//    public void login(@RequestParam String email, @RequestParam String password) throws LoginException {
//    adminServicesMPL.login(email,password);
//    }

    @PostMapping("/addCompany")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void addCompany(@RequestBody Company company) throws CompanyException {
        adminServicesMPL.addCompany(company);
    }

    @PutMapping("/updateCompany")
    @ResponseStatus(HttpStatus.OK)
    public void updateCompany(@RequestBody Company company) throws CompanyException {
        adminServicesMPL.updateCompany(company);
    }

    @DeleteMapping("/deleCompamy/{companyId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCompany(@PathVariable int companyId) throws CompanyException {
        adminServicesMPL.deleteCompany(companyId);
    }

    @GetMapping(value = "/allCompanies")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<Company> getAllCompanies() throws CompanyException {
        return adminServicesMPL.getAllCompanies();
    }

    @GetMapping("/getCompanyByID/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Company getOneCompanyById(@PathVariable int id) throws CompanyException {
        return adminServicesMPL.getCompanyByID(id);
    }

    @PostMapping("/addCustomer")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void addCustomer(@RequestBody Customer customer) throws CustomerException {
        adminServicesMPL.addCustomer(customer);
    }

    @PutMapping("/updateCustomer")
    @ResponseStatus(HttpStatus.OK)
    public void updateCustomer(@RequestBody Customer customer) throws CustomerException {
        adminServicesMPL.updateCustomer(customer);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/deleteCustomer/{customerID}")
    public void deleteCustomer(@PathVariable int customerID) throws CustomerException {
        adminServicesMPL.deleteCustomer(customerID);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/getAllCustomers")
    public List<Customer> getAllCustomer() throws CustomerException {
        return adminServicesMPL.getAllCustomer();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/get-one-customer/{id}")
    public Customer customerById(@PathVariable int id) throws CustomerException {
        return adminServicesMPL.findCustomerById(id);
    }


}
