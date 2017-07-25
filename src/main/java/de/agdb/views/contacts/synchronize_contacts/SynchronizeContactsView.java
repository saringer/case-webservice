package de.agdb.views.contacts.synchronize_contacts;

import com.github.scribejava.apis.GoogleApi20;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.Token;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.people.v1.People;
import com.google.api.services.people.v1.model.ListConnectionsResponse;
import com.google.api.services.people.v1.model.Person;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import de.agdb.backend.entities.Contact;
import de.agdb.backend.entities.Users;
import de.agdb.backend.entities.UsersRepository;
import de.agdb.views.contacts.ContactsMainView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.vaadin.addon.oauthpopup.OAuthListener;
import org.vaadin.addon.oauthpopup.OAuthPopupButton;
import org.vaadin.addon.oauthpopup.OAuthPopupConfig;

import javax.swing.text.html.CSS;
import java.io.File;
import java.io.IOException;
import java.util.List;

@UIScope
@SpringView(name = SynchronizeContactsView.VIEW_NAME)
public class SynchronizeContactsView extends VerticalLayout implements View {

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


    // Go to the Google API Console, open your application's
    // credentials page, and copy the client ID and client secret.
    // Then paste them into the following code.
    String clientId = "833038328979-iglf2mm0jap840b0vaq4jvo1miqvtkb4.apps.googleusercontent.com";
    String clientSecret = "Q_Pb1hvhGs1hH1CIsqYv1S1M";

    // Or your redirect URL for web based applications.
    String redirectUrl = "http://localhost:8080/Callback";

    String scope = "https://www.googleapis.com/auth/contacts.readonly";

    HttpTransport httpTransport = new NetHttpTransport();
    JacksonFactory jsonFactory = new JacksonFactory();

    @Autowired
    UsersRepository repository;


    public SynchronizeContactsView() {

        setSizeFull();
        VerticalLayout formWrapper = new VerticalLayout();
        formWrapper.setWidth("80%");
        formWrapper.setHeight("75%");
        addComponent(formWrapper);
        setComponentAlignment(formWrapper, Alignment.MIDDLE_CENTER);

        /*FormLayout content = new FormLayout();
        content.setHeight("80%");
        content.setWidth("90%");
        content.addStyleNames("overflow-auto");

        Button button = new Button("TEst");
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
               // UI.getCurrent().showNotification(repository.findByUsername("a").get(0).getUsername());
                Users users = repository.findByUsername("a").get(0);
                users.setUsername("YOOOOOO");
                users.addContact(new Contact("Riva1", "Riva2"));
                repository.save(users);
            }
        });
        content.addComponent(button);*/
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

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.addStyleName("solid-border");

        ThemeResource resource = new ThemeResource("views/img/mail_provider/social-google-box-icon.png");
        HorizontalLayout googleLayout = new HorizontalLayout();
        googleLayout.setWidth("100%");
        googleLayout.setHeight(64, Unit.PIXELS);
        //googleLayout.addStyleNames("solid-border");

        Embedded image = new Embedded(null,resource);
        image.setType(Embedded.TYPE_IMAGE);

        googleLayout.addComponent(image);
        googleLayout.setSpacing(false);
        HorizontalLayout horizontalWrapper = new HorizontalLayout();
        horizontalWrapper.setSpacing(false);
        horizontalWrapper.setHeight(60, Unit.PIXELS);
        horizontalWrapper.setWidth("85%");
        horizontalWrapper.addStyleNames("synccontacts-itembox");
        label = new Label("Google Mail:");
        TextField emailField = new TextField();
        //emailField.setWidth(200, Unit.PIXELS);
        horizontalWrapper.addComponent(label);
        horizontalWrapper.addComponent(emailField);
        Label emailPrefix = new Label("@gmail.com");
        horizontalWrapper.addComponent(emailPrefix);
        Button syncButton = new Button("Synchronize");
        syncButton.setStyleName("red");
        syncButton.setHeight(30,Unit.PIXELS);
        horizontalWrapper.addComponent(syncButton);
        horizontalWrapper.setComponentAlignment(label, Alignment.MIDDLE_LEFT);
        horizontalWrapper.setComponentAlignment(emailField, Alignment.MIDDLE_LEFT);
        horizontalWrapper.setComponentAlignment(emailPrefix, Alignment.MIDDLE_LEFT);
        horizontalWrapper.setComponentAlignment(syncButton, Alignment.MIDDLE_LEFT);
        horizontalWrapper.setExpandRatio(emailField, 0.7f);
        horizontalWrapper.setExpandRatio(emailPrefix, 0.3f);

        //horizontalWrapper.addComponent(new Button("sds"));



        googleLayout.addComponent(horizontalWrapper);
        googleLayout.setComponentAlignment(horizontalWrapper, Alignment.MIDDLE_CENTER);
        googleLayout.setExpandRatio(horizontalWrapper, 1);

        content.addComponent(googleLayout);

        wrapperLayout.addComponent(header);
        wrapperLayout.addComponent(content);
        wrapperLayout.setExpandRatio(content, 1);

        return wrapperLayout;

    }

    public void storeContactsInDatabase(String token) {
        GoogleCredential credential = new GoogleCredential().setAccessToken(token);

        People peopleService = new People.Builder(httpTransport, jsonFactory, credential)
                .build();

        ListConnectionsResponse contacts = null;
        try {
            contacts = peopleService.people().connections().
                    list("people/me").
                    setRequestMaskIncludeField("person.names,person.emailAddresses,person.phoneNumbers").
                    execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Person> connections = contacts.getConnections();
        for (int i = 0; i < connections.size(); i++) {

            String name = "empty";
            String email = "empty";
            if (connections.get(i).getEmailAddresses() != null) {
                email = connections.get(i).getEmailAddresses().get(0).getValue();
                System.out.println(email);
                if (connections.get(i).getNames() != null) {

                    name = connections.get(i).getNames().get(0).getDisplayName();
                    System.out.println(name);

                }

            }


            jdbcTemplate.update(
                    "insert into contacts (first_name, email) values (?, ?)",
                    name, email);


        }
    }

    public OAuthPopupButton testOauth() {


        String callBackUrl = getCallbackURL();

        OAuthPopupConfig config = OAuthPopupConfig.getStandardOAuth20Config(clientId, clientSecret);
        //config.setGrantType("authorization_code");
        config.setScope("https://www.googleapis.com/auth/contacts.readonly");
        //config.setCallbackUrl("urn:ietf:wg:oauth:2.0:oob");
        config.setCallbackUrl(callBackUrl);


        OAuthPopupButton twitter = new OAuthPopupButton(
                GoogleApi20.instance(), config);
        twitter.addOAuthListener(new OAuthListener() {

            @Override
            public void authSuccessful(Token token, boolean isOAuth20) {
                // Do something useful with the OAuth token, like persist it
                if (token instanceof OAuth2AccessToken) {
                    ((OAuth2AccessToken) token).getAccessToken();
                    ((OAuth2AccessToken) token).getRefreshToken();
                    ((OAuth2AccessToken) token).getExpiresIn();
                    storeContactsInDatabase(((OAuth2AccessToken) token).getAccessToken());


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
        //layout.addComponent(twitter);

        return twitter;

    }

    private String getCallbackURL() {
        String callBackUrl = Page.getCurrent().getLocation().toString();
        if (callBackUrl.contains("#")) {
            callBackUrl = callBackUrl.substring(0, callBackUrl.indexOf("#"));
            UI.getCurrent().showNotification(callBackUrl);
        }
        return callBackUrl;
    }
}
