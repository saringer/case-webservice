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

        VerticalLayout content = buildContent();
        content.setWidth("70%");
        content.setHeight("70%");


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
        wrapperLayout.setSpacing(false);

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

        VerticalLayout content = createSyncButtonWrapper("google");


        wrapperLayout.addComponent(header);
        wrapperLayout.addComponent(content);
        wrapperLayout.setExpandRatio(content, 1);

        return wrapperLayout;

    }

    private VerticalLayout createSyncButtonWrapper(String flag) {
        VerticalLayout content = new VerticalLayout();
        content.addStyleName("solid-border");
        content.setSizeFull();







        HorizontalLayout horizontalWrapper = new HorizontalLayout();
        horizontalWrapper.setSpacing(false);
        horizontalWrapper.setHeight(60, Unit.PIXELS);
        horizontalWrapper.setWidth("85%");
        horizontalWrapper.addStyleNames("synccontacts-itembox");
        Label label = new Label();
        label.addStyleNames("blue-label");

        OAuthPopupConfig config = OAuthPopupConfig.getStandardOAuth20Config(microsoftClientId, microsoftClientSecret);
        config.setScope(microsoftScope);
        config.setCallbackUrl(redirectUrl);
        OAuthPopupButton google = new OAuthPopupButton(LiveApi.instance(), config);
        google.setHeight(30, Unit.PIXELS);
        google.setWidth(172, Unit.PIXELS);
        google.addStyleName("blue-button");
        google.setPopupWindowFeatures("resizable,width=800,height=600");
        google.addOAuthListener(new OAuthListener() {

            @Override
            public void authSuccessful(Token token, boolean isOAuth20) {
                // Do something useful with the OAuth token, like persist it
                if (token instanceof OAuth2AccessToken) {
                    ((OAuth2AccessToken) token).getAccessToken();
                    ((OAuth2AccessToken) token).getRefreshToken();
                    ((OAuth2AccessToken) token).getExpiresIn();
                } else {
                    ((OAuth1AccessToken) token).getToken();
                    ((OAuth1AccessToken) token).getTokenSecret();
                }
            }

            @Override
            public void authDenied(String reason) {
                Notification.show("Failed to authenticate!", Notification.Type.ERROR_MESSAGE);
            }
        });

        horizontalWrapper.addComponent(label);
        horizontalWrapper.addComponent(google);
        horizontalWrapper.setComponentAlignment(label, Alignment.MIDDLE_LEFT);
        horizontalWrapper.setComponentAlignment(google, Alignment.MIDDLE_CENTER);

        HorizontalLayout contactServiceLayout = new HorizontalLayout();
        contactServiceLayout.setWidth("100%");
        contactServiceLayout.setHeight(64, Unit.PIXELS);
        contactServiceLayout.setSpacing(false);



        switch (flag) {
            case ("google"): {

                label.setValue("Google Contacts");

                ThemeResource resource = new ThemeResource("views/img/mail_provider/social-google-box-icon.png");

                Embedded image = new Embedded(null, resource);
                image.setType(Embedded.TYPE_IMAGE);

                contactServiceLayout.addComponent(image);
                contactServiceLayout.addComponent(horizontalWrapper);
                contactServiceLayout.setComponentAlignment(horizontalWrapper, Alignment.MIDDLE_CENTER);
                contactServiceLayout.setExpandRatio(horizontalWrapper, 1);


                content.addComponent(contactServiceLayout);
                return content;

            }

            default: {
                return null;
            }
        }

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
