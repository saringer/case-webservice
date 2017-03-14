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
            LoginForm parent = (LoginForm) source.getParent().getParent().getParent().getParent();


            // Create a sub-window and set the content
            final Window subWindow = new Window("Registration");
            subWindow.addStyleName("subWindowsStyle");
            subWindow.setResizable(false);

            VerticalLayout subContent = new VerticalLayout();
            subWindow.setContent(subContent);
            // Configure the windows layout; VerticalLayout by default
            subContent.setMargin(true);
            subContent.setSpacing(true);
            subContent.setSizeUndefined();

            final TextField newUserName = new TextField("User name");
            final TextField newUserPassword = new TextField("Password");
            Button create = new Button("create");
            create.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {


                    if (!newUserName.isEmpty() && !newUserPassword.isEmpty()) {
                        final String encodedPassword = passwordEncoder.encode(newUserPassword.getValue());

                        jdbcTemp.update(
                                "insert into users (name, password) values (?, ?)",
                                newUserName.getValue(), encodedPassword);
                        subWindow.close();
                    } else {
                        Notification note = new Notification("username and password must not be empty");
                        note.setStyleName(ValoTheme.NOTIFICATION_FAILURE);
                        note.show(Page.getCurrent());
                    }
                }
            });
            subContent.addComponent(newUserName);
            subContent.addComponent(newUserPassword);

            subContent.addComponent(create);

            // Center it in the browser window
            subWindow.center();
            // Set modal
            subWindow.setModal(true);

            // Open it in the UI
            UI.getCurrent().addWindow(subWindow);


        } catch (AuthenticationException e) {
        }
    }

}
