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
import tryingCoupons.tryingCoupon.beans.Coupon;
import tryingCoupons.tryingCoupon.beans.UserProp;

import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CustomerTest {


    String jwtToken;
    @Autowired
    RestTemplate restTemplate;



    @LocalServerPort
    @BeforeTestExecution
    void customerLogin(){
        String login = "http://localhost:8080/token/log";
        UserProp userProp = UserProp.builder()
                .username("just kill me already")
                .password("justKillMeAlready")
                .build();
        restTemplate.postForEntity(login,userProp,Boolean.class);

//        Map<String,String> map = new HashMap<>();
//        map.put("password","justKillMeAlready");
//        map.put("username","just kill me already");
//        ResponseEntity<String> response = restTemplate.postForEntity(login,String.class,null,map);
//        Assertions.assertEquals(202,response.getStatusCodeValue());

    }

    @BeforeEach
    void customerGetToken(){
        String getToken = "http://localhost:8080/token/getToken";
        ResponseEntity<String> response1 = restTemplate.getForEntity(getToken,String.class);
        jwtToken = response1.getBody();
        Assertions.assertEquals(202,response1.getStatusCodeValue());
    }

    @Test
    void getAllCustomerCoupons(){
        String url = "http://localhost:8080/customer/AllCustomerCoupon";
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", jwtToken);
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        ResponseEntity<Coupon[]> respEntity = restTemplate.exchange(url, HttpMethod.GET,entity,Coupon[].class);
        List<Coupon> resp = Arrays.stream(Objects.requireNonNull(respEntity.getBody())).collect(Collectors.toList());
        System.out.println(resp);
        Assertions.assertEquals(200,respEntity.getStatusCodeValue());
    }

    @Test
    void getCustomerCouponsCategory(){
        String url = "http://localhost:8080/customer/couponsCategory/{categoryID}";
        Map<String, String> map = new HashMap<>();
        map.put("categoryID","2");
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
    void getUserDetails(){
        String url = "http://localhost:8080/customer/customerDetails";
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", jwtToken);
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        ResponseEntity<String> respEntity = restTemplate.exchange(url,HttpMethod.GET,entity,String.class);
        String resp = respEntity.getBody();
        System.out.println(resp);
        Assertions.assertEquals(200,respEntity.getStatusCodeValue());
    }

    @Test
    void customerGetCouponByMaxPrice(){
        String url = "http://localhost:8080/customer/maxPrice/{maxPrice}";
        Map<String, String> map = new HashMap<>();
        map.put("maxPrice","20000");
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
    void customerPurchaseCoupon(){
        String url = "http://localhost:8080/customer/purchasecoupon/{id}";
        Map<String, String> map = new HashMap<>();
        map.put("id","2");
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", jwtToken);
        HttpEntity<Coupon> entity = new HttpEntity<Coupon>(headers);
        ResponseEntity<?> responseEntity = restTemplate.exchange(url,HttpMethod.PUT,entity,Coupon.class,map);
        System.out.println(responseEntity.getBody());
        Assertions.assertEquals(202,responseEntity.getStatusCodeValue());
    }

    @Test
    void customerLoginEx(){
        String url = "http://localhost:8080/token/log";
        UserProp userProp = UserProp.builder()
                .username("justkillmealraedy")
                .password("killme")
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
    void customerForbiddenGetAll(){
        String url = "http://localhost:8080/customer/AllCustomerCoupon";
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
    void getByCouCateEx(){
        String url = "http://localhost:8080/customer/couponsCategory/{categoryID}";
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
    void getByCouMaxP(){
        String url = "http://localhost:8080/customer/maxPrice/{maxPrice}";
        Map<String, String> map = new HashMap<>();
        map.put("maxPrice","10");
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
    void purchaseEx(){
        String url = "http://localhost:8080/customer/purchasecoupon/{id}";
        Map<String, String> map = new HashMap<>();
        map.put("id","3");
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", jwtToken);
        HttpEntity<Coupon> entity = new HttpEntity<Coupon>(headers);
        ResponseEntity<?> responseEntity = restTemplate.exchange(url,HttpMethod.PUT,entity,Coupon.class,map);
        System.out.println(responseEntity.getBody());

        Assertions.assertThrows(HttpClientErrorException.BadRequest.class,()-> {
            restTemplate.exchange(url,HttpMethod.PUT,entity,Coupon.class,map);
                }
        );

        try{
            restTemplate.exchange(url,HttpMethod.PUT,entity,Coupon.class,map);

        }catch (HttpClientErrorException.BadRequest err){
            System.out.println(err.getMessage());
        }

        Map<String, String> map2 = new HashMap<>();
        map2.put("id","5");


        Assertions.assertThrows(HttpClientErrorException.BadRequest.class,()-> {
                    restTemplate.exchange(url,HttpMethod.PUT,entity,Coupon.class,map2);
                }
        );

        try{
            restTemplate.exchange(url,HttpMethod.PUT,entity,Coupon.class,map2);

        }catch (HttpClientErrorException.BadRequest err){
            System.out.println(err.getMessage());
        }


    }



}
