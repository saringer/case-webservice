package de.agdb.views.login;



import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.UI;
import de.agdb.AppUI;
import de.agdb.backend.authentication.AuthManager;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringComponent
public class LoginButtonListener implements Button.ClickListener {

    @Autowired
    private AuthManager authManager;


    @Override
    public void buttonClick(Button.ClickEvent event) {
        try {
            Button source = event.getButton();
            LoginForm parent = (LoginForm) source.getParent().getParent().getParent().getParent().getParent();


            String username = parent.getTxtLogin().getValue();
            String password = parent.getTxtPassword().getValue();

            if (!username.isEmpty() && !password.isEmpty()) {
                UsernamePasswordAuthenticationToken request = new UsernamePasswordAuthenticationToken(username, password);
                Authentication result = authManager.authenticate(request);
                SecurityContextHolder.getContext().setAuthentication(result);

                SecurityContext context = SecurityContextHolder.getContext();
                Authentication authentication = context.getAuthentication();
                if (authentication != null && authentication.isAuthenticated()) {
                    AppUI app = (AppUI) UI.getCurrent();
                    app.setCurrentUsername(username);
                    parent.getLoginListener().loginSuccessful();

                    parent.getAccessControl().signIn(username, password);

                } else {
                    showNotification(new Notification("Login failed",
                            "Please check your username and password and try again.",
                            Notification.Type.HUMANIZED_MESSAGE));

                }
            } else {
                showNotification(new Notification("Login failed",
                        "Please enter your username and password and try again.",
                        Notification.Type.HUMANIZED_MESSAGE));
            }


        } catch (AuthenticationException e) {
            Notification.show("Authentication failed: " + e.getMessage());
        }
    }

    private void showNotification(Notification notification) {
        // keep the notification visible a little while after moving the
        // mouse, or until clicked
        notification.setDelayMsec(2000);
        notification.show(Page.getCurrent());
    }





}
