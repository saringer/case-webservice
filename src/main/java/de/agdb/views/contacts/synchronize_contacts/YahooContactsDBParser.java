package de.agdb.views.contacts.synchronize_contacts;

import com.github.scribejava.apis.LiveApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.vaadin.ui.UI;
import de.agdb.AppUI;
import de.agdb.backend.data_model.Contact;
import de.agdb.backend.data_model.Users;
import de.agdb.backend.data_model.repositories.UsersRepository;
import org.json.JSONArray;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

import static de.agdb.Constants.*;

public class YahooContactsDBParser {

    public YahooContactsDBParser() {


    }

    public void storeContactsInDatabase(OAuth2AccessToken oAuth2AccessToken, UsersRepository usersRepository) {
        final OAuth20Service service = new ServiceBuilder(yahooClientId)
                .apiSecret(yahooClientSecret)
                .scope(yahooScope)
                .callback(redirectUrl)
                .build(LiveApi.instance());

        final String PROTECTED_RESOURCE_URL = "";
        final OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
        service.signRequest(oAuth2AccessToken, request);
        final Response response;

        try {
            response = service.execute(request);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(response.getStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            System.out.println(content);

            JSONObject jsonObj = new JSONObject(content.toString());
            JSONArray ja_data = jsonObj.getJSONArray("data");

            for (int i = 0; i < ja_data.length(); i++) {
                JSONObject json = ja_data.getJSONObject(i);
                Contact contact = new Contact();
                try {
                    contact.setFirstName(json.getString("first_name"));
                } catch (Exception e) {
                }
                try {
                    contact.setLastName(json.getString("last_name"));
                } catch (Exception e) {
                }
                try {
                    JSONObject email = json.getJSONObject("emails");
                    contact.setEmail(email.getString("preferred"));
                } catch (Exception e) {
                }

                AppUI app = (AppUI) UI.getCurrent();
                String currentUser = app.getAccessControl().getUsername();

                Users user = usersRepository.findByUsername(currentUser).get(0);
                user.addContact(contact);
                usersRepository.save(user);

            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
