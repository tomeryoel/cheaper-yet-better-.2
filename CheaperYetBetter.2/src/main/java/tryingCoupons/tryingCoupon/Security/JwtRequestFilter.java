package tryingCoupons.tryingCoupon.Security;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

//filtering the requests that arrive to the jwt
//with the rest template that can create requests to http independently
// login, and root requests will go to the login screen
//and other requests will get filtered and authentication proses by catting the bearer from th header
//then decoding the token that is left from the header and Verifying it.
//if it got verified properly it goes on in the filters chain,
//if it doesn't verified properly an exception will be thrown.
// and if the token is fine but has got expired, then it will give the user a new token

public class JwtRequestFilter extends OncePerRequestFilter {
    RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();

    private RestTemplate restTemplate = restTemplateBuilder
            .setConnectTimeout(Duration.ofMillis(4000))
            .setReadTimeout(Duration.ofMillis(4000))
            .build();




    @Autowired
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException, java.io.IOException {


        if(request.getServletPath().equals("/")){
            response.sendRedirect("http://localhost:8080/login");
        }
            if(request.getServletPath().equals("/login")){

                filterChain.doFilter(request,response);

            }else{
                String authorizationHeader =  request.getHeader("Authorization");
                if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
                    try {
                        String token = authorizationHeader.substring("Bearer ".length());
                        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                        JWTVerifier verifier = JWT.require(algorithm).build();
                        DecodedJWT decodedJWT = verifier.verify(token);
                        String username = decodedJWT.getSubject();
                        String[] roles = decodedJWT.getClaim("authorities").asArray(String.class);
                        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                        stream(roles).forEach(role ->
                        {
                            authorities.add(new SimpleGrantedAuthority(role));
                        });
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,null, authorities);
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        filterChain.doFilter(request,response);
                    }catch (Exception err){
                        if(err.getMessage().startsWith("The Token has expired on")){
                            String newToken;
                            String url = "http://localhost:8080/token/getToken";
                            //System.out.println("ok im here");
                            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url,String.class);
                            newToken=responseEntity.getBody();
                            response.setHeader("new-token:",newToken);
                            Map<String, String> error = new HashMap<>();
                            response.setStatus(FORBIDDEN.value());
                            error.put("error message",err.getMessage());
                            response.setContentType(APPLICATION_JSON_VALUE);
                            new ObjectMapper().writeValue(response.getOutputStream(),error);
                        }else {
                            response.setHeader("error", err.getMessage());
                            response.setStatus(FORBIDDEN.value());
                            //response.sendError(FORBIDDEN.value());
                            Map<String, String> error = new HashMap<>();
                            error.put("error message", err.getMessage());
                            response.setContentType(APPLICATION_JSON_VALUE);
                            new ObjectMapper().writeValue(response.getOutputStream(), error);
                        }
                    }
                }else{
                    response.setHeader("error", " please log in");
                    response.setStatus(FORBIDDEN.value());
                    //response.sendError(FORBIDDEN.value());
                    Map<String, String> error = new HashMap<>();
                    error.put("error message", " please log in");
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(),error);
                    //response.sendRedirect("http//localhost:8080/");
                }
            }
        }
    }
