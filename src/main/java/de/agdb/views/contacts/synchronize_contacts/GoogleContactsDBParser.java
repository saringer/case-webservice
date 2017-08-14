package de.agdb.views.contacts.synchronize_contacts;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.ListConnectionsResponse;
import com.google.api.services.people.v1.model.Person;
import de.agdb.backend.entities.Contact;
import de.agdb.backend.entities.Users;
import de.agdb.backend.entities.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;


import java.io.IOException;
import java.util.List;

public class GoogleContactsDBParser {

    HttpTransport httpTransport = new NetHttpTransport();
    JacksonFactory jsonFactory = new JacksonFactory();


    public GoogleContactsDBParser() {

    }

    public void storeContactsInDatabase(String token, String userName, UsersRepository usersRepository) {

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

            Users user = usersRepository.findByUsername(userName).get(0);
            user.addContact(new Contact(firstname, lastname, email));
            usersRepository.save(user);


        }
    }
}
