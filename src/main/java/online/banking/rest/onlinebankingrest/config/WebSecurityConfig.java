package online.banking.rest.onlinebankingrest.config;

import lombok.RequiredArgsConstructor;
import online.banking.rest.onlinebankingrest.entity.enums.Role;
import online.banking.rest.onlinebankingrest.security.JWTAuthenticationTokenFilter;
import online.banking.rest.onlinebankingrest.security.JwtAuthenticationEntryPoint;
import online.banking.rest.onlinebankingrest.service.impl.CurrentUserDetailServiceImpl;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationEntryPoint unauthorizedHandler;
    private final CurrentUserDetailServiceImpl userDetailsService;
    private final JWTAuthenticationTokenFilter filter;
    private final PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/customers").hasAuthority(Role.MANAGER.name())
                .antMatchers(HttpMethod.GET, "/customers{id}").hasAuthority(Role.MANAGER.name())
                .antMatchers(HttpMethod.GET, "/customers/byEmail").hasAuthority(Role.MANAGER.name())
                .antMatchers(HttpMethod.POST, "/customers").hasAuthority(Role.MANAGER.name())
                .antMatchers(HttpMethod.PUT, "/customers/{id}").hasAuthority(Role.MANAGER.name())
                .antMatchers(HttpMethod.DELETE, "/customers/{id}").hasAuthority(Role.MANAGER.name())

                .antMatchers(HttpMethod.GET, "/accounts").hasRole(Role.MANAGER.name())
                .antMatchers(HttpMethod.GET, "/accounts/{id}").hasAuthority(Role.MANAGER.name())
                .antMatchers(HttpMethod.GET, "/accounts/byCustomerId/{id}").hasAuthority(Role.MANAGER.name())
                .antMatchers(HttpMethod.POST, "/accounts").hasAuthority(Role.MANAGER.name())
                .antMatchers(HttpMethod.PUT, "/accounts/{id}").hasAuthority(Role.MANAGER.name())
                .antMatchers(HttpMethod.DELETE, "/accounts/{id}").hasAuthority(Role.MANAGER.name())
                .antMatchers(HttpMethod.POST, "/accounts/replenish-balance/{id}").permitAll()
                .antMatchers(HttpMethod.POST, "/accounts/withdraw-balance/{id}").permitAll()
                .antMatchers(HttpMethod.POST, "/accounts/transfer-money").permitAll()
                .antMatchers(HttpMethod.GET, "/accounts/transfer-money-history").permitAll()

                .antMatchers(HttpMethod.GET, "/credit-cards").hasAnyAuthority(Role.MANAGER.name())
                .antMatchers(HttpMethod.GET, "/credit-cards/{id}").hasAnyAuthority(Role.MANAGER.name())
                .antMatchers(HttpMethod.POST, "/credit-cards").hasAnyAuthority(Role.MANAGER.name())
                .antMatchers(HttpMethod.PUT, "/credit-cards/{cardId}").hasAnyAuthority(Role.MANAGER.name())

                .antMatchers(HttpMethod.GET, "/loans").hasAnyAuthority(Role.MANAGER.name())
                .antMatchers(HttpMethod.GET, "/loans/{id}").hasAnyAuthority(Role.MANAGER.name())
                .antMatchers(HttpMethod.POST, "/loans/apply-loan").hasAnyAuthority(Role.CUSTOMER.name())
                .antMatchers(HttpMethod.POST, "/loans{id}").hasAuthority(Role.MANAGER.name())
                .anyRequest().permitAll();

        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        http.headers().cacheControl();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }
}
