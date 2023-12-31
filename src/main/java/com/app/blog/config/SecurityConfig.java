package com.app.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.app.blog.security.CustomUserDetailsService;
import com.app.blog.security.JWTAuthenticationEntryPoint;
import com.app.blog.security.JwtAuthenticationFilter;


@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@EnableWebMvc
public class SecurityConfig {

    public static final String[] PUBLIC_URLS = {
        "/v3/api-docs",
        "/v2/api-docs",
        "/swagger-resources/**",
        "/swagger-ui/**",
        "/webjars/**"
    };

    @Autowired
    private JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Lazy
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(){

        // UserDetails normalUser = User.withUsername("Shalu").password(passwordEncoder().encode("password")).roles("NORMAL").build();

        // UserDetails adminUser = User.withUsername("Neha").password(passwordEncoder().encode("password")).roles("ADMIN").build();

        // InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager(normalUser, adminUser);

        // return inMemoryUserDetailsManager;

        return new CustomUserDetailsService();

    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.cors(cors -> cors.disable()).csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .requestMatchers( "/api/v1/auth/**").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/api/users/").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/users/").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/categories/").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/categories/").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/categories/").hasRole("ADMIN")
                //.requestMatchers(HttpMethod.POST, "/api/categories/").hasRole("ADMIN")
                //.requestMatchers(HttpMethod.POST, "/api/categories/").hasRole("ADMIN")
                //.requestMatchers(HttpMethod.POST, "/api/categories/").hasRole("ADMIN")
                //.requestMatchers(HttpMethod.POST, "/api/categories/").hasRole("ADMIN")
                .requestMatchers(PUBLIC_URLS).permitAll()
                .requestMatchers(HttpMethod.GET, "/**").permitAll()
                .anyRequest()
                .authenticated()).exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider()) ;
                

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);        

        return http.build();        
    }

    @Bean
    public AuthenticationProvider authenticationProvider() { 
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(); 
        authenticationProvider.setUserDetailsService(userDetailsService()); 
        authenticationProvider.setPasswordEncoder(passwordEncoder()); 
        return authenticationProvider; 
    } 

     @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception { 
        return config.getAuthenticationManager(); 
    } 
  

}
