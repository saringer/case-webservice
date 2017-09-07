package de.agdb.backend.authentication;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class BasicAccessControl implements AccessControl {

    @Override
    public boolean signIn(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return false;
        } else {
            CurrentUser.set(username);
            return true;
        }


    }

    @Override
    public boolean isUserSignedIn() {
        return !CurrentUser.getUsername().isEmpty();
    }



    @Override
    public String getUsername() {
        return CurrentUser.getUsername();
    }

}
