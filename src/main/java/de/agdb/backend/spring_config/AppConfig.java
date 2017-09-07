package de.agdb.backend.spring_config;


import de.agdb.backend.authentication.AuthManager;
import de.agdb.backend.authentication.UserService;
import de.agdb.views.schedules.SchedulerMainViewPresenter;
import de.agdb.views.schedules.create_schedule.CategoriesViewPresenter;
import de.agdb.views.schedules.create_schedule.DateViewPresenter;
import de.agdb.views.schedules.create_schedule.GeneralViewPresenter;
import de.agdb.views.schedules.create_schedule.TimeLocationViewPresenter;
import org.apache.catalina.connector.Connector;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.vaadin.addons.Toastr;

@Configuration

@ComponentScan(basePackages = {"de.agdb.*", "de.agdb.backend.authentication","de.agdb.views.categories", "de.agdb.views.contacts"})
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

    @Bean
    public SchedulerMainViewPresenter schedulerMainViewPresenter() {
        SchedulerMainViewPresenter presenter = new SchedulerMainViewPresenter();
        return presenter;
    }

    @Bean
    public CategoriesViewPresenter setCategoriesViewPresenter() {
        CategoriesViewPresenter presenter = new CategoriesViewPresenter();
        return presenter;
    }

    @Bean
    public DateViewPresenter setDateViewPresenter() {
        DateViewPresenter presenter = new DateViewPresenter();
        return presenter;
    }

    @Bean
    public GeneralViewPresenter setGeneralViewPresenter() {
        GeneralViewPresenter presenter = new GeneralViewPresenter();
        return presenter;
    }

    @Bean
    public TimeLocationViewPresenter setTimeLocationViewPresenter() {
        TimeLocationViewPresenter presenter = new TimeLocationViewPresenter();
        return presenter;
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
