package tryingCoupons.tryingCoupon.configurations;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.client.RestTemplate;
import tryingCoupons.tryingCoupon.beans.Roles;

import java.sql.Date;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class RestTemplateConfig {
    String token;

    private void setToken(){
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_"+ Roles.ADMIN.name()));
        User userDetails = new User("admin@admin.com", "admin",authorities);

        token = JWT.create().withSubject(userDetails.getUsername()).withExpiresAt(new Date(System.currentTimeMillis() +  30 * 1000))
                .withClaim("authorities", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())).sign(algorithm);
    }


    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder){
        return builder
                .setConnectTimeout(Duration.ofMillis(3000))
                .setReadTimeout(Duration.ofMillis(3000))
                .build();
    }

}
