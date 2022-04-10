package tryingCoupons.tryingCoupon.Security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tryingCoupons.tryingCoupon.beans.Roles;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private final BCryptPasswordEncoder passwordEncoder;
    private final UserDetailsService usd;
    private final JwtRequestFilter jwtRequestFilter = new JwtRequestFilter();






    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usd).passwordEncoder(passwordEncoder);
    }




    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/","index","/css","/js","media","img","/login/**")
                .permitAll()
                .antMatchers("/v2/api-docs",
                        "/configuration/ui",
                        "/swagger-resources/**",
                        "/configuration/security",
                        "/swagger-ui.html",
                        "/webjars/**").permitAll()
                .antMatchers("/token/getToken").permitAll()
                .antMatchers("/login/**").permitAll()
                .antMatchers("/admin/**").hasRole(Roles.ADMIN.name())
                .antMatchers("/company/**").hasRole(Roles.COMPANY.name())
                .antMatchers("/customer/**").hasRole(Roles.CUSTOMER.name())
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                .defaultSuccessUrl("http://localhost:8080/swagger-ui.html#/");
//                .and()
//                .logout()
//                .logoutSuccessUrl("http://localhost:8080/token/lognout");
                //.logoutSuccessUrl("http://localhost:8080/login");


    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**")
                .antMatchers("/token/**")
                .antMatchers("/guest/**");
    }



}
