//package SoloProject.SocialMediaApp;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfiguration {
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests(configurer -> {
//                    configurer
//
//                            .requestMatchers("/admin/**").hasRole("ADMIN")
//                            .requestMatchers("/home").permitAll()
//                            .requestMatchers("/createAccount").permitAll()
//                            .requestMatchers("/createAccount").permitAll()
//                            .anyRequest().authenticated();
//                })
//                .formLogin(form -> {
//                    form
//                            .loginPage("/login")
//                            .loginProcessingUrl("/authenticateTheUser")
//                            .permitAll();
//                })
//                .logout(logout -> logout.permitAll())
//                .exceptionHandling(configurer -> {
//                    configurer.accessDeniedPage("/access-denied");
//                });
//
//        return http.build();
//    }
//}