package de.agdb.config;


import de.agdb.auth.AuthManager;
import de.agdb.auth.UserService;
import de.agdb.views.categories.DBService;
import de.agdb.views.contacts.ContactService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration

@ComponentScan(basePackages = {"de.agdb.*", "de.agdb.auth","de.agdb.views.categories", "de.agdb.views.contacts"})
public class AppConfig {

    @Bean
    public DBService dbService() {
        DBService service = new DBService();
        return service;
    }

    @Bean
    public AuthManager authManager() {
        AuthManager manager = new AuthManager();
        return manager;
    }

    @Bean
    public ContactService contactService() {
        ContactService service = new ContactService();
        return service;
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
