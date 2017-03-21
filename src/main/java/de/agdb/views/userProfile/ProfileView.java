package de.agdb.views.userProfile;


import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.people.v1.model.Name;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

import com.google.api.client.auth.oauth2.Credential;


import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;


import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.people.v1.People;
import com.google.api.services.people.v1.PeopleScopes;
import com.google.api.services.people.v1.model.ListConnectionsResponse;
import com.google.api.services.people.v1.model.Person;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;



public class ProfileView extends VerticalLayout implements View {
    public static final String VIEW_NAME = "Profile";

    /** Application name. */
    private static final String APPLICATION_NAME =
            "Crysis App";

    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
            System.getProperty("user.home"), ".credentials/people.googleapis.com-java-quickstart");

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/people.googleapis.com-java-quickstart
     */
    private static final List<String> SCOPES =
            Arrays.asList(PeopleScopes.CONTACTS_READONLY);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in =
                ProfileView.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(DATA_STORE_FACTORY)
                        .setAccessType("offline")
                        .build();
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;


    }

    /**
     * Build and return an authorized People client service.
     * @return an authorized People client service
     * @throws IOException
     */
    public static People getPeopleService() throws IOException {


        Credential credential = authorize();
        return new People.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }



    public ProfileView() throws IOException {
        setSizeFull();
        HttpTransport httpTransport = new NetHttpTransport();
        JacksonFactory jsonFactory = new JacksonFactory();

        // Go to the Google API Console, open your application's
        // credentials page, and copy the client ID and client secret.
        // Then paste them into the following code.
        String clientId = "833038328979-iglf2mm0jap840b0vaq4jvo1miqvtkb4.apps.googleusercontent.com";
        String clientSecret = "Q_Pb1hvhGs1hH1CIsqYv1S1M";

        // Or your redirect URL for web based applications.
        String redirectUrl = "http://localhost:8080/Callback";
        String scope = "https://www.googleapis.com/auth/contacts.readonly";

        // Step 1: Authorize -->
        String authorizationUrl = new GoogleBrowserClientRequestUrl(clientId,
                redirectUrl,
                Arrays.asList(scope))
                .build();

        // Point or redirect your user to the authorizationUrl.
        System.out.println("Go to the following link in your browser:");
        System.out.println(authorizationUrl);

        // Read the authorization code from the standard input stream.
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("What is the authorization code?");
        String code = in.readLine();
        // End of Step 1 <--

        // Step 2: Exchange -->
        GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                httpTransport, jsonFactory, clientId, clientSecret, code, redirectUrl).execute();
        // End of Step 2 <--

        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(jsonFactory)
                .setClientSecrets(clientId, clientSecret)
                .build()
                .setFromTokenResponse(tokenResponse);

        People peopleService = new People.Builder(httpTransport, jsonFactory, credential)
                .build();

        ListConnectionsResponse response = peopleService.people().connections().list("people/me").execute();
        List<Person> connections = response.getConnections();
        System.out.println(connections.get(0).getEmailAddresses());



        /*People service = getPeopleService();

        // Request 10 connections.
        ListConnectionsResponse response = service.people().connections()
                .list("people/me")
                .setPageSize(10)
                .execute();

        // Print display name of connections if available.
        List<Person> connections = response.getConnections();
        if (connections != null && connections.size() > 0) {
            for (Person person : connections) {
                List<Name> names = person.getNames();
                if (names != null && names.size() > 0) {
                    System.out.println("Name: " + person.getNames().get(0)
                            .getDisplayName());
                } else {
                    System.out.println("No names available for connection.");
                }
            }
        } else {
            System.out.println("No connections found.");
        }
*/




    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
