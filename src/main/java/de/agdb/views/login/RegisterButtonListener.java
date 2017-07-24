package de.agdb.views.login;

import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import de.agdb.views.MainScreen;
import de.agdb.views.scheduler.SchedulerMainView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class RegisterButtonListener implements Button.ClickListener {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JdbcTemplate jdbcTemp;

    RegisterForm parent;

    @Override
    public void buttonClick(Button.ClickEvent event) {


        try {
            Button source = event.getButton();
            parent = (RegisterForm) source.getParent().getParent().getParent().getParent().getParent();
            TextField userName = parent.getTxtUsername();
            PasswordField password = parent.getTxtPassword();


            if (!userName.isEmpty() && !password.isEmpty()) {
                final String encodedPassword = passwordEncoder.encode(password.getValue());

                jdbcTemp.update(
                        "insert into users (name, password) values (?, ?)",
                        userName.getValue(), encodedPassword);

                UI.getCurrent().setContent(new LoginForm(parent.getAccessControl(), new LoginForm.LoginListener() {
                    @Override
                    public void loginSuccessful() {
                        showMainView();
                    }
                }, parent.getViewProvider(), parent.getUi()));


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

    protected void showMainView() {
        UI.getCurrent().addStyleName(ValoTheme.UI_WITH_MENU);
        UI.getCurrent().setContent(new MainScreen(parent.getUi(),parent.getViewProvider()));
        UI.getCurrent().getNavigator().navigateTo(SchedulerMainView.VIEW_NAME);
    }


}