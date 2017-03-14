package de.agdb.config;


import de.agdb.auth.AuthManager;
import de.agdb.auth.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@ComponentScan(basePackages = {"de.agdb*"})
public class AppConfig {

    @Bean
    public AuthManager authManager() {
        AuthManager manager = new AuthManager();
        return manager;
    }

    @Bean
    public UserService userService() {
        UserService service = new UserService();
        return service;
    }

    // will use random salt and generate a string of length 60
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
