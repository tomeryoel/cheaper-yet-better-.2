package tryingCoupons.tryingCoupon;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.event.annotation.BeforeTestExecution;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import tryingCoupons.tryingCoupon.beans.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CompanyTest {


    String jwtToken;
    @Autowired
    RestTemplate restTemplate;



    @LocalServerPort
    @BeforeTestExecution
    void companyLogin(){
        String login = "http://localhost:8080/token/log";
//        Map<String,String> map = new HashMap<>();
//        map.put("password","DrewHouse");
//        map.put("username","justin bieber");
//        ResponseEntity<String> response = restTemplate.postForEntity(login,String.class,null,map);
//        System.out.println("im here");
//        Assertions.assertEquals(202,response.getStatusCodeValue());

        UserProp userProp = UserProp.builder()
                .username("justin bieber")
                .password("DrewHouse")
                .build();
        restTemplate.postForEntity(login,userProp,Boolean.class);

    }

    @BeforeEach
    void companyGetToken(){
        String getToken = "http://localhost:8080/token/getToken";
        ResponseEntity<String> response1 = restTemplate.getForEntity(getToken,String.class);
        jwtToken = response1.getBody();
        Assertions.assertEquals(202,response1.getStatusCodeValue());
    }

    @Test
    void addCoupon(){
        String url = "http://localhost:8080/company/addCoupon";
        Coupon coupon = Coupon.builder()
                .amount(100)
                .category_id_bynum(2)
                .description("this is a check coupon")
                .end_date(Date.valueOf(LocalDate.now().plusDays(2)))
                .start_date(Date.valueOf(LocalDate.now()))
                .price(200)
                .image("kksssss")
                .title("could be better")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", jwtToken);
        HttpEntity<Coupon> entity = new HttpEntity<Coupon>(coupon,headers);
        ResponseEntity<?> responseEntity = restTemplate.exchange(url, HttpMethod.POST,entity,Coupon.class);
        System.out.println(responseEntity.getBody());
        Assertions.assertEquals(202,responseEntity.getStatusCodeValue());

        //Exception
        Assertions.assertThrows(HttpClientErrorException.BadRequest.class,()-> {
            restTemplate.exchange(url, HttpMethod.POST, entity, Coupon.class);
                }
            );
    }

    @Test
    void companyGetAllCoupons(){
        String url = "http://localhost:8080/company/allCompanyCoupon";
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", jwtToken);
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        ResponseEntity<Coupon[]> respEntity = restTemplate.exchange(url,HttpMethod.GET,entity,Coupon[].class);
        List<Coupon> resp = Arrays.stream(Objects.requireNonNull(respEntity.getBody())).collect(Collectors.toList());
        System.out.println(resp);
        Assertions.assertEquals(200,respEntity.getStatusCodeValue());
    }

    @Test
    void companyGetCouponByCategory(){
        String url = "http://localhost:8080/company/companyCategory/{categoryID}";
        Map<String, String> map = new HashMap<>();
        map.put("categoryID","1");
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", jwtToken);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<Coupon[]> respEntity = restTemplate.exchange(url,HttpMethod.GET,entity,Coupon[].class,map);
        List<Coupon> resp = Arrays.asList(respEntity.getBody());
        System.out.println(resp);
        Assertions.assertEquals(200,respEntity.getStatusCodeValue());
    }

    @Test
    void companyGetCouponByMaxPrice(){
        String url = "http://localhost:8080/company/companyCategoryMaxPrice/{price}";
        Map<String, String> map = new HashMap<>();
        map.put("price","20000");
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", jwtToken);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<Coupon[]> respEntity = restTemplate.exchange(url,HttpMethod.GET,entity,Coupon[].class,map);
        List<Coupon> resp = Arrays.asList(respEntity.getBody());
        System.out.println(resp);
        Assertions.assertEquals(200,respEntity.getStatusCodeValue());
    }

    @Test
    void companyDeleteCoupon(){
        String url = "http://localhost:8080/company/deleteCoupon/{id}";
        Map<String,String> map = new HashMap<>();
        map.put("id","2");
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", jwtToken);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<?> responseEntity = restTemplate.exchange(url,HttpMethod.DELETE,entity,void.class,map);
        System.out.println(responseEntity.getBody());
        Assertions.assertEquals(202,responseEntity.getStatusCodeValue());


    }


    @Test
    void companyUpdateCoupon(){
        String url = "http://localhost:8080/company/updateCoupon";
        Coupon coupon = Coupon.builder()
                .amount(100)
                .coupon_id(1)
                .companyId(1)
                .category(CategoryInjection.builder()
                        .id(1)
                        .category(Category.CARS)
                        .build())
                .category_id_bynum(2)
                .company_id_sql(Company.builder()
                        .id(1)
                        .name("asda")
                        .password("DrewHouse")
                        .email("justin bieber")
                        .build())
                .description("this is a check coupon")
                .end_date(Date.valueOf(LocalDate.now().plusDays(2)))
                .start_date(Date.valueOf(LocalDate.now()))
                .price(200)
                .image("kksssss")
                .title("could be better")
                .build();

        Customer customer = Customer.builder()
                .email("check")
                .password("check")
                .lastName("check")
                .coupon(coupon)
                .firstName("sdfsdf")
                .id(1)
                .build();

        coupon.setCustomers(Set.of(customer));
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", jwtToken);
        HttpEntity<Coupon> entity = new HttpEntity<Coupon>(coupon,headers);
        ResponseEntity<?> responseEntity = restTemplate.exchange(url,HttpMethod.PUT,entity,Coupon.class);
        System.out.println(responseEntity.getBody());
        Assertions.assertEquals(202,responseEntity.getStatusCodeValue());

    }

    @Test
    void companyDetails(){
        String url = "http://localhost:8080/company/getCompanyDetails";
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", jwtToken);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<?> respEntity = restTemplate.exchange(url,HttpMethod.GET,entity,String.class);
        System.out.println(respEntity.getBody());
        Assertions.assertEquals(200,respEntity.getStatusCodeValue());

    }



    @Test
    void companyLoginException(){
        String url = "http://localhost:8080/token/log";
        UserProp userProp = UserProp.builder()
                .username("justi")
                .password("bib")
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
    void addCouponEX(){
        String url = "http://localhost:8080/company/addCoupon";
        Coupon coupon = Coupon.builder()
                .amount(100)
                .category_id_bynum(2)
                .description("this is a check coupon")
                .end_date(Date.valueOf(LocalDate.now().plusDays(2)))
                .start_date(Date.valueOf(LocalDate.now()))
                .price(200)
                .image("kksssss")
                .title("life could be better for sure")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", jwtToken);
        HttpEntity<Coupon> entity = new HttpEntity<Coupon>(coupon,headers);
        ResponseEntity<?> responseEntity = restTemplate.exchange(url, HttpMethod.POST,entity,Coupon.class);
        System.out.println(responseEntity.getBody());
        Assertions.assertEquals(202,responseEntity.getStatusCodeValue());

        //Exception
        Assertions.assertThrows(HttpClientErrorException.BadRequest.class,()-> {
                    restTemplate.exchange(url, HttpMethod.POST, entity, Coupon.class);
                }
        );

        try{
            restTemplate.exchange(url, HttpMethod.POST, entity, Coupon.class);

        }catch (HttpClientErrorException.BadRequest err){
            System.out.println(err.getMessage());
        }

    }

    @Test
    void forBiddenRequestGetAllCo(){
        String url = "http://localhost:8080/company/allCompanyCoupon";
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer lsdfsdfsdkfsdfsdfksdfk");
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        Assertions.assertThrows(HttpClientErrorException.Forbidden.class,()-> {
            restTemplate.exchange(url,HttpMethod.GET,entity,Coupon[].class);
                }
        );

        try{
            restTemplate.exchange(url,HttpMethod.GET,entity,Coupon[].class);

        }catch (HttpClientErrorException.Forbidden err){
            System.out.println(err.getMessage());
        }

    }

    @Test
    void couponByCaEx(){
        String url = "http://localhost:8080/company/companyCategory/{categoryID}";
        Map<String, String> map = new HashMap<>();
        map.put("categoryID","5");
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", jwtToken);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        Assertions.assertThrows(HttpClientErrorException.BadRequest.class,()-> {
            restTemplate.exchange(url,HttpMethod.GET,entity,Coupon[].class,map);
                }
        );

        try{
            restTemplate.exchange(url,HttpMethod.GET,entity,Coupon[].class,map);

        }catch (HttpClientErrorException.BadRequest err){
            System.out.println(err.getMessage());
        }

    }

    @Test
    void getByMaxEx(){
        String url = "http://localhost:8080/company/companyCategoryMaxPrice/{price}";
        Map<String, String> map = new HashMap<>();
        map.put("price","10");
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", jwtToken);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        Assertions.assertThrows(HttpClientErrorException.BadRequest.class,()-> {
                    restTemplate.exchange(url,HttpMethod.GET,entity,Coupon[].class,map);
                }
        );

        try{
            restTemplate.exchange(url,HttpMethod.GET,entity,Coupon[].class,map);

        }catch (HttpClientErrorException.BadRequest err){
            System.out.println(err.getMessage());
        }
    }

    @Test
    void deleteCouponEx(){
        String url = "http://localhost:8080/company/deleteCoupon/{id}";
        Map<String,String> map = new HashMap<>();
        map.put("id","8");
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
    void updateEx(){
        String url = "http://localhost:8080/company/updateCoupon";
        Coupon coupon = Coupon.builder()
                .amount(100)
                .coupon_id(87)
                .companyId(1)
                .category(CategoryInjection.builder()
                        .id(1)
                        .category(Category.CARS)
                        .build())
                .category_id_bynum(2)
                .company_id_sql(Company.builder()
                        .id(1)
                        .name("asda")
                        .password("DrewHouse")
                        .email("justin bieber")
                        .build())
                .description("this kjrfg a check coupon")
                .end_date(Date.valueOf(LocalDate.now().plusDays(2)))
                .start_date(Date.valueOf(LocalDate.now()))
                .price(200)
                .image("kksssss")
                .title("kefgkfg be better")
                .build();


        Customer customer = Customer.builder()
                .email("check2")
                .password("check2")
                .lastName("check2")
                .coupon(coupon)
                .firstName("sdfsdf2")
                .id(1)
                .build();

        coupon.setCustomers(Set.of(customer));
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", jwtToken);
        HttpEntity<Coupon> entity = new HttpEntity<Coupon>(coupon,headers);

        Assertions.assertThrows(HttpClientErrorException.BadRequest.class,()-> {
            restTemplate.exchange(url,HttpMethod.PUT,entity,Coupon.class);
                }
        );

        try{
            restTemplate.exchange(url,HttpMethod.PUT,entity,Coupon.class);
        }catch (HttpClientErrorException.BadRequest err){
            System.out.println(err.getMessage());
        }
    }



}
