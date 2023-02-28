package com.example.demo.security;

import com.example.demo.auth.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.concurrent.TimeUnit;

import static com.example.demo.security.UserRole.STUDENT;

@Configuration
@EnableWebSecurity
@EnableGlobalAuthentication
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;
    private final AppUserService appUserService;

    @Autowired
    public SecurityConfig(PasswordEncoder passwordEncoder, AppUserService appUserService) {
        this.passwordEncoder = passwordEncoder;
        this.appUserService = appUserService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                /*.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()*/
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/**").hasRole(STUDENT.name())
                        /*.requestMatchers(HttpMethod.DELETE, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
                        .requestMatchers(HttpMethod.PUT, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
                        .requestMatchers(HttpMethod.POST, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
                        .requestMatchers(HttpMethod.GET, "/management/api/**").hasAnyRole(ADMINTRAINEE.name(), ADMIN.name())*/
                        .anyRequest()
                        .authenticated())
                //.httpBasic(Customizer.withDefaults());
                .authenticationProvider(daoAuthenticationProvider())
                .formLogin()
                .loginPage("/login").permitAll()
                .defaultSuccessUrl("/courses", true)
                .and()
                .rememberMe()
                    .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))
                    .key("somesecuredkey")
                .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID", "remember-me")
                    .logoutSuccessUrl("/login");

        return http.build();
    }

    /**
     * InMemoryUserDetailService
     */
    /*@Bean
    public UserDetailsService userDetailsService() {
        UserDetails arturUser = User.builder()
                .username("artur")
                .password(passwordEncoder.encode("password"))
                //.roles(ADMIN.name())
                .authorities(ADMIN.getGrantedAuthorities())
                .build();

        UserDetails tomUser = User.builder()
                .username("tom")
                .password(passwordEncoder.encode("password"))
                //.roles(ADMINTRAINEE.name())
                .authorities(ADMINTRAINEE.getGrantedAuthorities())
                .build();

        UserDetails smithUser = User.builder()
                .username("smith")
                .password(passwordEncoder.encode("password"))
                //.roles(STUDENT.name())
                .authorities(STUDENT.getGrantedAuthorities())
                .build();

        return new InMemoryUserDetailsManager(
                arturUser,
                smithUser,
                tomUser
        );
    }*/

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(appUserService);
        return provider;
    }
}
