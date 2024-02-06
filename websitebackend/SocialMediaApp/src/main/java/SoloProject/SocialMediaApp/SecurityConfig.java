package SoloProject.SocialMediaApp;
import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.models.AppUserDTO;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import SoloProject.SocialMediaApp.service.MyUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.authentication.AuthenticationProvider;
import java.util.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        http

                .csrf().disable()
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .formLogin()
                .loginProcessingUrl("/api/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler((request, response, authentication) -> {
                    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                    String username = userDetails.getUsername();

                    AppUser appUser = appUserRepository.findByUsername(username);
                    if (appUser == null) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                    }
                    appUser.setOtp(null);
                    appUser.setOnline(true);
                    appUserRepository.save(appUser);
                    AppUserDTO appUserDTO = new AppUserDTO(appUser);
                    String token = jwtUtil.generateToken(userDetails);
                    Map<String, Object> responseBody = new HashMap<>();
                    responseBody.put("message", "Login successful");
                    responseBody.put("jwt", token);
                    responseBody.put("user", appUserDTO);
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType("application/json");
                    new ObjectMapper().writeValue(response.getOutputStream(), responseBody);
                })
                .failureHandler((request, response, exception) -> {
                    Map<String, String> responseBody = new HashMap<>();
                    responseBody.put("message", "Login failed: " + exception.getMessage());


                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    new ObjectMapper().writeValue(response.getOutputStream(), responseBody);
                })
                .and()
                .authenticationProvider(daoAuthenticationProvider(passwordEncoder))
                .authorizeRequests(auth -> auth
                        .requestMatchers(
                                "/api/account/*/getProfilePicture",
                                "/api/account/createAccount",
                                "/api/login",
                                "/api/publicFeed",
                                "/api/user/getAllWebsiteUsers",
                                "/api/*/comments",
                                "/api/post/*/comments",
                                "/api/posts/*",
                                "/api/*/logout",
                                "/api/post/*/images",
                                "/error",
                                "/api/account/ForgotPassword",
                                "/api/user/*",
                                "/api/status/getByUser/*",
                                "/api/post/*/posts/*",
                                "/api/*/followers",
                                "/api/*/following",
                                "/api/account/generateVerificationToken",
                                "/api/account/verifyEmailToken",
                                "/api/account/checkVerification",
                                "/account/recover"

                        ).permitAll()
                        .anyRequest().hasRole("USER")
                )
                .addFilterBefore(new CustomTokenAuthenticationFilter(jwtUtil, myUserDetailsService), UsernamePasswordAuthenticationFilter.class)
                .logout()
                .logoutUrl("/api/logout")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "your-other-cookies")
                .logoutSuccessHandler((request, response, authentication) -> {

                    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                    String username = userDetails.getUsername();
                    AppUser appUser = appUserRepository.findByUsername(username);
                    if (appUser != null) {
                        appUser.setOnline(false);
                        appUserRepository.save(appUser);
                    }




                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType("application/json");
                    Map<String, String> responseBody = new HashMap<>();
                    responseBody.put("message", "Logout successful");
                    new ObjectMapper().writeValue(response.getWriter(), responseBody);
                });


        return http.build();
    }


    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        CookieCsrfTokenRepository repository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        repository.setCookieName("XSRF-TOKEN");
        return repository;
    }
    @Bean
    public AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(myUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("https://natebookfrontend.onrender.com","https://localhost:3000", "http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}