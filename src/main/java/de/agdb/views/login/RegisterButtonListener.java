package de.agdb.views.login;

import com.vaadin.server.Page;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class RegisterButtonListener implements Button.ClickListener {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JdbcTemplate jdbcTemp;


    @Override
    public void buttonClick(Button.ClickEvent event) {


        try {
            Button source = event.getButton();
            RegisterForm parent = (RegisterForm) source.getParent().getParent().getParent().getParent().getParent();
            TextField userName = parent.getTxtUsername();
            PasswordField password = parent.getTxtPassword();


            if (!userName.isEmpty() && !password.isEmpty()) {
                final String encodedPassword = passwordEncoder.encode(password.getValue());

                jdbcTemp.update(
                        "insert into users (name, password) values (?, ?)",
                        userName.getValue(), encodedPassword);

            } else {
                Notification note = new Notification("username and password must not be empty");
                note.setStyleName(ValoTheme.NOTIFICATION_FAILURE);
                note.show(Page.getCurrent());


                // Open it in the UI
                //UI.getCurrent().addWindow(subWindow);


            }

        } catch (AuthenticationException e) {
        }

    }
}