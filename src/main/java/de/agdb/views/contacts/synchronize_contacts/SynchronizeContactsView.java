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
import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import de.agdb.backend.entities.Users;
import de.agdb.views.contacts.ContactsMainView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.vaadin.addon.oauthpopup.OAuthListener;
import org.vaadin.addon.oauthpopup.OAuthPopupButton;
import org.vaadin.addon.oauthpopup.OAuthPopupConfig;

import java.io.File;
import java.io.IOException;
import java.util.List;

@UIScope
@SpringView(name = SynchronizeContactsView.VIEW_NAME)
public class SynchronizeContactsView extends VerticalLayout implements View{

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




    public SynchronizeContactsView() {

        setSizeFull();
        VerticalLayout formWrapper = new VerticalLayout();
        formWrapper.setWidth("80%");
        formWrapper.setHeight("80%");
        addComponent(formWrapper);
        setComponentAlignment(formWrapper, Alignment.MIDDLE_CENTER);

        FormLayout content = new FormLayout();
        content.setHeight("80%");
        content.setWidth("90%");
        content.addStyleNames("overflow-auto");

        Button button = new Button("TEst");
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                //UI.getCurrent().showNotification(repository.findByName("a").getName());
            }
        });
        content.addComponent(button);


        formWrapper.addStyleName("solid-border");
        formWrapper.setSpacing(false);
        formWrapper.setMargin(false);
        formWrapper.addComponent(content);
        formWrapper.setComponentAlignment(content, Alignment.MIDDLE_CENTER);
        formWrapper.setExpandRatio(content, 1);
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

                    name =  connections.get(i).getNames().get(0).getDisplayName();
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
        if(callBackUrl.contains("#")) {
            callBackUrl = callBackUrl.substring(0, callBackUrl.indexOf("#"));
            UI.getCurrent().showNotification(callBackUrl);
        }
        return callBackUrl;
    }
}
