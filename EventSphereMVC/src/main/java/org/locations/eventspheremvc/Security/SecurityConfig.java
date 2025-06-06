package org.locations.eventspheremvc.Security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Value("${security.rememberme.key}")
    private String secureKey;

    private CustomUserDetailsService userDetailsService;
    private CustomAccessDeniedHandler customAccessDeniedHandler;


    public SecurityConfig(CustomUserDetailsService userDetailsService, CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.userDetailsService = userDetailsService;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/sys","/admin/admins").hasRole("SYS_ADMIN")
                        .requestMatchers("/admin","/admin/**").hasAnyRole("ADMIN","SYS_ADMIN")
                        .requestMatchers("/register").permitAll()
                        .requestMatchers("/reset","/reset/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/image" ).permitAll()
                        .requestMatchers(HttpMethod.GET,"/event/details").permitAll()
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/organizer").hasRole("ORGANIZER")
                        .requestMatchers("/event").hasAnyRole("ORGANIZER","SYS_ADMIN","ADMIN")
                        .requestMatchers("/event/events").hasRole("ORGANIZER")
                        .requestMatchers("/home","/home/**").hasRole("USER")
                        .requestMatchers("/ticket/manage").hasRole("ORGANIZER")
                        .requestMatchers("/ticket/**").hasRole("USER")
                        .requestMatchers("/pool","/pool/**").hasRole("ORGANIZER")
                        .requestMatchers("/subscribe","/unsubscribe").hasRole("USER")
                        .requestMatchers("/image/**").hasRole("ORGANIZER")
                        .requestMatchers("/user/update").hasAnyRole("USER","ORGANIZER","ADMIN")
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/")
                        .permitAll()
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(customAccessDeniedHandler))
                .rememberMe( remember -> remember
                        .key(secureKey)
                        .tokenValiditySeconds(604800)
                        .rememberMeParameter("remember"))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll())
                .build();
    }
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
