package de.agdb.backend.config;


import de.agdb.backend.auth.AuthManager;
import de.agdb.backend.auth.UserService;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.vaadin.addons.Toastr;

@Configuration

@ComponentScan(basePackages = {"de.agdb.*", "de.agdb.backend.auth","de.agdb.views.categories", "de.agdb.views.contacts"})
public class AppConfig {


    @Bean
    public AuthManager authManager() {
        AuthManager manager = new AuthManager();
        return manager;
    }

    @Bean
    public Toastr toastr() {
      Toastr toastr = new Toastr();
      return toastr;
    };



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

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
        return new EmbeddedServletContainerCustomizer() {
            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {
                if (container instanceof TomcatEmbeddedServletContainerFactory) {
                    TomcatEmbeddedServletContainerFactory containerFactory =
                            (TomcatEmbeddedServletContainerFactory) container;

                    Connector connector = new Connector(TomcatEmbeddedServletContainerFactory.DEFAULT_PROTOCOL);
                    connector.setPort(8080);
                    containerFactory.addAdditionalTomcatConnectors(connector);
                }
            }
        };
    }


}
