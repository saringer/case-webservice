package de.agdb.views.contacts.synchronize_contacts;

import com.github.scribejava.apis.GoogleApi20;

import com.github.scribejava.apis.LiveApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.Token;

import com.github.scribejava.core.oauth.OAuth20Service;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.ListConnectionsResponse;
import com.google.api.services.people.v1.model.Person;
import com.vaadin.event.LayoutEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.*;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import de.agdb.AppUI;
import de.agdb.backend.entities.Contact;
import de.agdb.backend.entities.Users;
import de.agdb.backend.entities.UsersRepository;
import de.agdb.test.OAuthListener;
import de.agdb.test.OAuthPopupButton;
import de.agdb.test.OAuthPopupConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static de.agdb.Constants.microsoftClientId;
import static de.agdb.Constants.microsoftClientSecret;
import static de.agdb.Constants.microsoftScope;

@UIScope
@SpringView(name = SynchronizeContactsView.VIEW_NAME)
public class SynchronizeContactsView extends VerticalLayout implements View, Button.ClickListener {

    public static final String VIEW_NAME = "SynchronizeContactsView";

    @Autowired
    JdbcTemplate jdbcTemplate;


    /**
     * File for storing user credentials.
     */
    private static final java.io.File DATA_STORE_FILE =
            new File(System.getProperty("user.home"), ".credentials/user-credentials.json");

    /**
     * Global instance of the {DataStoreFactory}. The best practice is to make it a single
     * globally shared instance across your application.
     */
    private static FileDataStoreFactory dataStoreFactory;


    // Or your redirect URL for web based applications.
    String redirectUrl = "http://localhost:8080/";

    String scope = "https://www.googleapis.com/auth/contacts.readonly";

    HttpTransport httpTransport = new NetHttpTransport();
    JacksonFactory jsonFactory = new JacksonFactory();

    private Button googleButton;

    @Autowired
    UsersRepository repository;

    @PostConstruct
    public void init() {
        setSizeFull();
        VerticalLayout formWrapper = new VerticalLayout();
        formWrapper.setWidth("80%");
        formWrapper.setHeight("75%");
        addComponent(formWrapper);
        setComponentAlignment(formWrapper, Alignment.MIDDLE_CENTER);



        VerticalLayout content = new VerticalLayout();
        content.addStyleName("solid-border");
        content.setSpacing(false);
        content.setMargin(false);
        content.setWidth("70%");
        content.setHeight("70%");
        content.addComponent(buildContent());


        formWrapper.addStyleName("solid-border");
        formWrapper.setSpacing(false);
        formWrapper.setMargin(false);
        formWrapper.addComponent(content);
        formWrapper.setComponentAlignment(content, Alignment.MIDDLE_CENTER);
        formWrapper.setExpandRatio(content, 1);
    }


    private VerticalLayout buildContent() {

        VerticalLayout wrapperLayout = new VerticalLayout();
        wrapperLayout.setSizeFull();
        //wrapperLayout.setSpacing(false);
        wrapperLayout.setMargin(false);

        CssLayout header = new CssLayout();
        header.setWidth("100%");
        header.setHeight(50, Unit.PIXELS);
        header.addStyleNames("synccontacts-header");
        Label label = new Label("Synchronized email accounts");
        //label.setWidth("100%");
        label.addStyleNames("headerLabel");
        //label.addStyleNames(ValoTheme.LABEL_H3);
        //label.addStyleNames(ValoTheme.LABEL_COLORED);
        header.addComponent(label);

        VerticalLayout innerWrapperLayout = new VerticalLayout();
        innerWrapperLayout.setSizeFull();
        innerWrapperLayout.setSpacing(false);

        HorizontalLayout googleLayout = new ContactServiceLayout("google");
        HorizontalLayout microsoftLayout = new ContactServiceLayout("microsoft");
        HorizontalLayout yahooLayout = new ContactServiceLayout("yahoo");
        innerWrapperLayout.addComponents(googleLayout, microsoftLayout, yahooLayout);


        wrapperLayout.addComponent(header);
        wrapperLayout.addComponent(innerWrapperLayout);

        wrapperLayout.setExpandRatio(innerWrapperLayout, 1);

        return wrapperLayout;

    }


    public void storeContactsInDatabase(String token, String userName) {

        GoogleCredential credential = new GoogleCredential().setAccessToken(token);

        PeopleService peopleService = new PeopleService.Builder(httpTransport, jsonFactory, credential)
                .build();

        ListConnectionsResponse contacts = null;
        try {
            contacts = peopleService.people().connections().
                    list("people/me").setPersonFields("names,emailAddresses").
                    //setRequestMaskIncludeField("person.names,person.emailAddresses,person.phoneNumbers").
                            execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*// Request 10 connections.
        ListConnectionsResponse response = service.people().connections()
                .list("people/me")
                .setPageSize(10)
                .setPersonFields("names,emailAddresses")
                .execute();*/


        List<Person> connections = contacts.getConnections();
        for (int i = 0; i < connections.size(); i++) {

            String firstname = "empty";
            String lastname = "empty";
            String email = "empty";


            if (connections.get(i).getEmailAddresses() != null) {
                email = connections.get(i).getEmailAddresses().get(0).getValue();
            }
            //if (connections.get(i).getNames() != null) {
            try {
                firstname = connections.get(i).getNames().get(0).getGivenName();
            } catch (Exception e) {

            }


            try {
                lastname = connections.get(i).getNames().get(0).getFamilyName();
            } catch (Exception e) {

            }

            Users user = repository.findByUsername(userName).get(0);
            user.addContact(new Contact(firstname, lastname, email));
            repository.save(user);


        }
    }


    private String getCallbackURL() {
        String callBackUrl = Page.getCurrent().getLocation().toString();
        if (callBackUrl.contains("#")) {
            callBackUrl = callBackUrl.substring(0, callBackUrl.indexOf("#"));
            UI.getCurrent().showNotification(callBackUrl);
        }
        return callBackUrl;
    }



    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        Button clickedButton = clickEvent.getButton();


    }

}
