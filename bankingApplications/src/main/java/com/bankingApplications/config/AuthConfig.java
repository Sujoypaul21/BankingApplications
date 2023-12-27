
package com.bankingApplications.config;

import com.bankingApplications.model.PrimaryAccount;
import com.bankingApplications.model.SavingsAccount;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collection;

@Configuration
@EnableMethodSecurity
public class AuthConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.authorizeHttpRequests()
                .requestMatchers("/customer/**").hasRole("CUSTOMER")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and().formLogin().successHandler(new AuthenticationSuccessHandler() {

                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                        Authentication authentication) throws IOException, ServletException {
                        Collection<? extends GrantedAuthority> role= authentication.getAuthorities();
                        for(GrantedAuthority a:role) {
                            if(a.getAuthority().equals("ROLE_ADMIN")) {
                                response.sendRedirect("http://localhost:8080/portal");
                            }
                            if(a.getAuthority().equals("ROLE_USER")) {
                                response.sendRedirect("http://localhost:8080/userFront");
                            }
                        }

                    }
                })
                .and().logout().logoutSuccessUrl("/logout");

        return  httpSecurity.build();
    }
    @Bean
    public UserDetailsService userDetailsService() {

        UserDetails admin = User.builder().username("admin").password(passwordEncoder().encode("admin")).roles("ADMIN")
                .build();
        UserDetails user = User.builder().username("user").password(passwordEncoder().encode("user")).roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin,user);
    }


}

