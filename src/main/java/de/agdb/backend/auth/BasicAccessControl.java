package de.agdb.backend.auth;

public class BasicAccessControl implements AccessControl {

    @Override
    public boolean signIn(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty())
            return false;

        CurrentUser.set(username);
        return true;
    }

    @Override
    public boolean isUserSignedIn() {
        return !CurrentUser.getUsername().isEmpty();
    }

    @Override
    public boolean isUserInRole(String role) {
        if ("admin".equals(role)) {
            // Only the "admin" user is in the "admin" role
            return getUsername().equals("admin");
        }

        // All users are in all non-admin roles
        return true;
    }

    @Override
    public String getUsername() {
        return CurrentUser.getUsername();
    }

}
