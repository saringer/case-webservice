package de.agdb.views.login;

import com.vaadin.data.Binder;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import de.agdb.backend.data_model.Categories;
import de.agdb.backend.data_model.Users;
import de.agdb.backend.data_model.repositories.UsersRepository;
import de.agdb.backend.field_validators.IsAlphabeticalValidator;
import de.agdb.views.MainScreen;
import de.agdb.views.schedules.SchedulerMainView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class RegisterButtonListener implements Button.ClickListener {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsersRepository usersRepository;

    private Binder<Users> binder = new Binder<>();

    private RegistrationForm parent;
    private Users userBean = new Users();



    @Override
    public void buttonClick(Button.ClickEvent event) {


        try {
            Button source = event.getButton();
            parent = (RegistrationForm) source.getParent().getParent().getParent().getParent().getParent();
            TextField userName = parent.getTxtUsername();
            PasswordField password = parent.getTxtPassword();
            TextField email = parent.getTxtEmail();

            if (parent.getValidationBinder().isValid()) {
                System.out.println("Is valid");
            if (!userName.isEmpty() && !password.isEmpty()) {
                System.out.println(usersRepository.findByEmail(email.getValue()));
                final String encodedPassword = passwordEncoder.encode(password.getValue());


                if ((usersRepository.findByEmail(email.getValue()) == null)
                        && (usersRepository.findByUsername(userName.getValue()).isEmpty())) {
                    Users newUser = new Users();
                    newUser.setUsername(userName.getValue());
                    newUser.setPassword(encodedPassword);
                    newUser.setEmail(email.getValue());
                    usersRepository.save(newUser);

                    UI.getCurrent().setContent(new LoginForm(parent.getAccessControl(), new LoginForm.LoginListener() {
                        @Override
                        public void loginSuccessful() {
                            showMainView();
                        }
                    }, parent.getViewProvider(), parent.getUi()));
                }


            } } else {
                Notification note = new Notification("Invalid input");
                note.setStyleName(ValoTheme.NOTIFICATION_FAILURE);
                note.show(Page.getCurrent());
            }

        } catch (AuthenticationException e) {
        }

    }



    protected void showMainView() {

        UI.getCurrent().addStyleName(ValoTheme.UI_WITH_MENU);
        UI.getCurrent().setContent(new MainScreen(parent.getUi(), parent.getViewProvider()));
        UI.getCurrent().getNavigator().navigateTo(SchedulerMainView.VIEW_NAME);
    }


}