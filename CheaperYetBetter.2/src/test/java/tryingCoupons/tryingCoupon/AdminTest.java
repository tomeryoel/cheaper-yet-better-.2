package tryingCoupons.tryingCoupon;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.event.annotation.BeforeTestExecution;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.*;
import java.util.stream.Collectors;
import org.junit.jupiter.api.*;
import tryingCoupons.tryingCoupon.beans.Company;
import tryingCoupons.tryingCoupon.beans.Customer;
import tryingCoupons.tryingCoupon.beans.UserProp;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AdminTest {



String jwtToken;
@Autowired
	RestTemplate restTemplate;



	@LocalServerPort
	@BeforeTestExecution
	void adminLogin(){
		String login = "http://localhost:8080/token/log";
		UserProp userProp = UserProp.builder()
				.username("admin@admin.com")
				.password("admin")
				.build();
		restTemplate.postForEntity(login,userProp,Boolean.class);


	}

	@BeforeEach
	void adminGetToken(){
		String getToken = "http://localhost:8080/token/getToken";
		ResponseEntity<String> response1 = restTemplate.getForEntity(getToken,String.class);
		jwtToken = response1.getBody();
		Assertions.assertEquals(202,response1.getStatusCodeValue());
	}

	@Test
	void getAllCompaniesCheck(){
		String url = "http://localhost:8080/admin/allCompanies";
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", jwtToken);

		HttpEntity<String> entity = new HttpEntity<String>(headers);

		ResponseEntity<Company[]> respEntity = restTemplate.exchange(url, HttpMethod.GET, entity, Company[].class);

		List<Company> resp = Arrays.stream(respEntity.getBody()).collect(Collectors.toList());
		System.out.println("im here" + resp);
		Assertions.assertEquals(202,respEntity.getStatusCodeValue());
	}

	@Test
	void adminAddCompany(){
		String url = "http://localhost:8080/admin/addCompany";
		Company company = Company.builder()
				.name("check1")
				.email("check1")
				.password("check")
				.build();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", jwtToken);
		HttpEntity<Company> entity = new HttpEntity<Company>(company,headers);

		ResponseEntity<?> responseEntity = restTemplate.exchange(url,HttpMethod.POST,entity,Company.class);
		System.out.println(responseEntity.getBody());
		Assertions.assertEquals(202,responseEntity.getStatusCodeValue());

	}

	@Test
	void adminAddCustomer(){
		String url = "http://localhost:8080/admin/addCustomer";
		Customer customer = Customer.builder()
				.firstName("checkCustomer")
				.lastName("checkCustomer")
				.password("checkCustomer")
				.email("checkCustomerFirst")
				.build();

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", jwtToken);
		HttpEntity<Customer> entity = new HttpEntity<Customer>(customer,headers);

		ResponseEntity<?> responseEntity = restTemplate.exchange(url,HttpMethod.POST,entity,Customer.class);
		System.out.println(responseEntity.getBody());
		Assertions.assertEquals(202,responseEntity.getStatusCodeValue());

	}

	@Test
	void adminDeleteCompany(){
		String url = "http://localhost:8080/admin/deleCompamy/{id}";
		Map<String,String> map = new HashMap<>();
		map.put("id","2");
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", jwtToken);
			HttpEntity<String> entity = new HttpEntity<String>(headers);
		ResponseEntity<?> responseEntity = restTemplate.exchange(url,HttpMethod.DELETE,entity,void.class,map);
		System.out.println(responseEntity.getBody());
	Assertions.assertEquals(200,responseEntity.getStatusCodeValue());


	}

	@Test
	void adminDeleteCustomer(){
		String url = "http://localhost:8080/admin/deleteCustomer/{id}";
		Map<String,String> map = new HashMap<>();
		map.put("id","2");
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", jwtToken);
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		ResponseEntity<?> responseEntity = restTemplate.exchange(url,HttpMethod.DELETE,entity,void.class,map);
		System.out.println(responseEntity.getBody());
		Assertions.assertEquals(200,responseEntity.getStatusCodeValue());


	}

	@Test
	void getOneCustomer(){
		String url = "http://localhost:8080/admin/get-one-customer/{id}";
		Map<String,String> map = new HashMap<>();
		map.put("id","1");
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", jwtToken);

		HttpEntity<String> entity = new HttpEntity<String>(headers);

		ResponseEntity<Customer> respEntity = restTemplate.exchange(url, HttpMethod.GET, entity, Customer.class,map);
		Customer resp = respEntity.getBody();
		System.out.println(resp);
		Assertions.assertEquals(200,respEntity.getStatusCodeValue());
	}

	@Test
	void adminGetAllCustomers(){
		String url = "http://localhost:8080/admin/getAllCustomers";
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", jwtToken);
		HttpEntity<String> entity = new HttpEntity<String>(headers);

		ResponseEntity<Customer[]> respEntity = restTemplate.exchange(url,HttpMethod.GET,entity,Customer[].class);
		List<Customer> resp = Arrays.stream(Objects.requireNonNull(respEntity.getBody())).collect(Collectors.toList());
		System.out.println(resp);
		Assertions.assertEquals(200,respEntity.getStatusCodeValue());
	}

	@Test
	void getOneCompany(){
		String url = "http://localhost:8080/admin/getCompanyByID/{id}";
		Map<String,String> map = new HashMap<>();
		map.put("id","1");
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", jwtToken);

		HttpEntity<String> entity = new HttpEntity<String>(headers);

		ResponseEntity<Company> respEntity = restTemplate.exchange(url, HttpMethod.GET, entity, Company.class,map);
		Company resp = respEntity.getBody();
		System.out.println(resp);
		Assertions.assertEquals(202,respEntity.getStatusCodeValue());
	}

	@Test
	void adminUpdateCompany(){
		String url = "http://localhost:8080/admin/updateCompany";
		Company company = Company.builder()
				.password("lifeIsHard")
				.email("canwebefine@dyinginsie.die")
				.id(1)
				.name("cannotbechanged")

				.build();

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", jwtToken);
		HttpEntity<Company> entity = new HttpEntity<Company>(company,headers);

		ResponseEntity<?> responseEntity = restTemplate.exchange(url,HttpMethod.PUT,entity,Company.class);
		System.out.println(responseEntity.getBody());
		Assertions.assertEquals(200,responseEntity.getStatusCodeValue());

	}

	@Test
	void adminLoginException(){

		String url = "http://localhost:8080/token/log";
		UserProp userProp = UserProp.builder()
				.username("admin@admin.co")
				.password("admin")
				.build();


		try {
			restTemplate.postForEntity(url,userProp,Boolean.class);
		}catch (HttpClientErrorException.Unauthorized err){
			System.out.println(err.getMessage());
		}


		Assertions.assertThrows(HttpClientErrorException.Unauthorized.class,()-> {
			restTemplate.postForEntity(url,userProp,Boolean.class);
				}
		);

	}

	@Test
	void getAllCompaniesExceptionIfNotLogged(){
		String url = "http://localhost:8080/admin/allCompanies";
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer kasdfasdfkadslskksss)");
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		Assertions.assertThrows(HttpClientErrorException.Forbidden.class,()-> {
			restTemplate.exchange(url, HttpMethod.GET, entity, Company[].class);
				}
		);

		try{
			restTemplate.exchange(url, HttpMethod.GET, entity, Company[].class);
		}catch (HttpClientErrorException.Forbidden err){
			System.out.println(err.getMessage());
		}

	}

	@Test
	void addCompanyExceptions(){
		String url = "http://localhost:8080/admin/addCompany";
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", jwtToken);
		Company company2 = Company.builder()
				.name("check")
				.email("lololol")
				.password("check")
				.build();
		//adding company with same name

		HttpEntity<Company> entity2 = new HttpEntity<Company>(company2,headers);
		restTemplate.exchange(url, HttpMethod.POST, entity2, Company.class);

		Assertions.assertThrows(HttpClientErrorException.BadRequest.class,()-> {
			restTemplate.exchange(url, HttpMethod.POST, entity2, Company.class);
		});

			try {
				restTemplate.exchange(url, HttpMethod.POST, entity2, Company.class);
			}catch (HttpClientErrorException.BadRequest err){
				System.out.println(err.getMessage());
			}



		//same email.
		Company company3 = Company.builder()
				.name("lololol")
				.email("check")
				.password("check")
				.build();
		HttpEntity<Company> entity3 = new HttpEntity<Company>(company2,headers);
		Assertions.assertThrows(HttpClientErrorException.BadRequest.class,()-> {
			restTemplate.exchange(url, HttpMethod.POST, entity3, Company.class);
		});


		try {
			restTemplate.exchange(url, HttpMethod.POST, entity3, Company.class);
		}catch (HttpClientErrorException.BadRequest err){
			System.out.println(err.getMessage());
		}

	}

	@Test
	void deleteCompanyException(){
		String url = "http://localhost:8080/admin/deleCompamy/{id}";
		Map<String,String> map = new HashMap<>();
		map.put("id","5");
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", jwtToken);
		HttpEntity<String> entity = new HttpEntity<String>(headers);

		Assertions.assertThrows(HttpClientErrorException.BadRequest.class,()-> {
			restTemplate.exchange(url,HttpMethod.DELETE,entity,void.class,map);
				}
		);

		try{
			restTemplate.exchange(url,HttpMethod.DELETE,entity,void.class,map);
		}catch (HttpClientErrorException.BadRequest err){
			System.out.println(err.getMessage());
		}
	}

	@Test
	void getOneCompByIDEx(){
		String url = "http://localhost:8080/admin/getCompanyByID/{id}";
		Map<String,String> map = new HashMap<>();
		map.put("id","5");
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", jwtToken);
		HttpEntity<String> entity = new HttpEntity<String>(headers);


		Assertions.assertThrows(HttpClientErrorException.BadRequest.class,()-> {
			restTemplate.exchange(url, HttpMethod.GET, entity, Company.class,map);
		}
		);

		try{
			restTemplate.exchange(url, HttpMethod.GET, entity, Company.class,map);

		}catch (HttpClientErrorException.BadRequest err){
			System.out.println(err.getMessage());
		}
	}

	@Test
	void addNewCustomerEx(){
		String url = "http://localhost:8080/admin/addCustomer";
		Customer customer = Customer.builder()
				.firstName("checkCustomer1")
				.lastName("checkCustomer")
				.password("checkCustomer")
				.email("checkCustomer")
				.build();

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", jwtToken);
		HttpEntity<Customer> entity = new HttpEntity<Customer>(customer,headers);

		ResponseEntity<?> responseEntity = restTemplate.exchange(url,HttpMethod.POST,entity,Customer.class);
		System.out.println(responseEntity.getBody());
		Assertions.assertEquals(202,responseEntity.getStatusCodeValue());

		Customer customer2= Customer.builder()
				.firstName("checkCustome2")
				.lastName("check")
				.password("checkCustomer")
				.email("checkCustomer")
				.build();

		HttpEntity<Customer> entity2 = new HttpEntity<Customer>(customer,headers);

		Assertions.assertThrows(HttpClientErrorException.BadRequest.class,()-> {
			restTemplate.exchange(url,HttpMethod.POST,entity,Customer.class);
		});

		try{
			restTemplate.exchange(url,HttpMethod.POST,entity,Customer.class);
		}catch (HttpClientErrorException.BadRequest err){
			System.out.println(err.getMessage());

		}

	}

	@Test
	void deleteCustomerEx(){
		String url = "http://localhost:8080/admin/deleCompamy/{id}";
		Map<String,String> map = new HashMap<>();
		map.put("id","5");
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", jwtToken);
		HttpEntity<String> entity = new HttpEntity<String>(headers);

		Assertions.assertThrows(HttpClientErrorException.BadRequest.class,()-> {
			restTemplate.exchange(url,HttpMethod.DELETE,entity,void.class,map);
		});

		try{
			restTemplate.exchange(url,HttpMethod.DELETE,entity,void.class,map);

		}catch (HttpClientErrorException.BadRequest err){
			System.out.println(err.getMessage());
		}


	}

	@Test
	void customerByIdEX(){

		String url = "http://localhost:8080/admin/get-one-customer/{id}";
		Map<String,String> map = new HashMap<>();
		map.put("id","99");
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", jwtToken);

		HttpEntity<String> entity = new HttpEntity<String>(headers);

		Assertions.assertThrows(HttpClientErrorException.BadRequest.class,()-> {
			restTemplate.exchange(url, HttpMethod.GET, entity, Customer.class,map);
		});

		try{
			restTemplate.exchange(url, HttpMethod.GET, entity, Customer.class,map);

		}catch (HttpClientErrorException.BadRequest err){
			System.out.println(err.getMessage());
		}
	}




}
