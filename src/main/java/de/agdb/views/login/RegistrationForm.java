package de.agdb.views.login;

import com.vaadin.data.Binder;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction;

import com.vaadin.shared.ui.ContentMode;

import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import de.agdb.AppUI;
import de.agdb.backend.authentication.AccessControl;
import de.agdb.backend.data_model.Users;
import de.agdb.backend.field_validators.IsAlphabeticalValidator;
import de.agdb.views.MainScreen;
import de.agdb.views.schedules.SchedulerMainView;
import org.springframework.context.ApplicationContext;


/**
 * Created by Riva on 18.07.2017.
 */
public class RegistrationForm extends CssLayout {

    private TextField username;
    private TextField email;
    private PasswordField password;
    private Button registerButton;
    private Button cancelButton;
    private SpringViewProvider viewProvider;
    private AppUI ui;
    private AccessControl accessControl;
    private Binder<Users> binder = new Binder<>();


    public RegistrationForm(SpringViewProvider viewProvider, AppUI ui, AccessControl accessControl) {
        this.viewProvider = viewProvider;
        this.ui = ui;
        this.accessControl = accessControl;

        buildUI();
    }

    private void buildUI() {
        addStyleName("register-screen");

        Component registerForm = buildRegisterForm();
        // layout to center login form when there is sufficient screen space
        VerticalLayout centeringLayout = new VerticalLayout();
        centeringLayout.setStyleName("centering-layout");
        centeringLayout.addComponent(registerForm);
        centeringLayout.setComponentAlignment(registerForm, Alignment.MIDDLE_CENTER);
        addComponent(centeringLayout);

    }

    private Component buildRegisterForm() {

        VerticalLayout containerLayout = new VerticalLayout();
        containerLayout.setSizeUndefined();
        containerLayout.addStyleName("register-form");
        containerLayout.setWidth(472, Unit.PIXELS);


        Label registerFormCaption = new Label(String.format("<font size = \"5\" color=\"#2b78e4\"> Create Account")
                , ContentMode.HTML);
        registerFormCaption.setWidth(null);


        FormLayout registerForm = new FormLayout();

        //registerForm.addStyleName("login-form");
        registerForm.setSizeUndefined();
        // registerForm.setMargin(true);
        registerForm.addComponent(registerFormCaption);

        registerForm.addComponent(username = new TextField("Username:"));
        username.setWidth(15, Unit.EM);

        registerForm.addComponent(email = new TextField("Email:"));
        email.setWidth(15, Unit.EM);

        registerForm.addComponent(password = new PasswordField("Password:"));
        password.setWidth(15, Unit.EM);

        setUpValidationBinding();

        CssLayout buttons = new CssLayout();
        buttons.setStyleName("buttons");

        buttons.addComponent(registerButton = new Button("Create"));
        registerButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        registerButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        RegisterButtonListener registerButtonListener = getRegisterButtonListener();
        registerButton.addClickListener(registerButtonListener);


        buttons.addComponent((cancelButton = new Button("Cancel")));
        cancelButton.addStyleNames(ValoTheme.BUTTON_DANGER, "float-right");
        cancelButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                UI.getCurrent().setContent(new LoginForm(accessControl, new LoginForm.LoginListener() {
                    @Override
                    public void loginSuccessful() {
                        showMainView();
                    }
                }, viewProvider, ui));
            }
        });

        registerForm.addComponent(buttons);


        containerLayout.addComponent(registerForm);
        containerLayout.setComponentAlignment(registerForm, Alignment.MIDDLE_CENTER);
        return containerLayout;
    }

    public RegisterButtonListener getRegisterButtonListener() {
        AppUI ui = (AppUI) UI.getCurrent();
        ApplicationContext context = ui.getApplicationContext();
        return context.getBean(RegisterButtonListener.class);
    }

    public TextField getTxtUsername() {
        return username;
    }

    public PasswordField getTxtPassword() {
        return password;
    }

    public TextField getTxtEmail() {
        return email;
    }


    public Binder getValidationBinder() {
        return binder;
    }


    protected void showMainView() {
        AppUI ui = (AppUI) UI.getCurrent();

        addStyleName(ValoTheme.UI_WITH_MENU);
        UI.getCurrent().setContent(new MainScreen(ui, viewProvider));
        UI.getCurrent().getNavigator().navigateTo(SchedulerMainView.VIEW_NAME);
    }

    public AccessControl getAccessControl() {
        return this.accessControl;
    }

    public SpringViewProvider getViewProvider() {
        return this.viewProvider;
    }

    public AppUI getUi() {
        return ui;
    }

    private void setUpValidationBinding() {
        binder.forField(username)
                .withValidator(new StringLengthValidator(
                        "User name must be between 4 and 15 characters long",
                        4, 15))
                .withValidator(new IsAlphabeticalValidator())
                .bind(Users::getUsername, Users::setUsername);
        binder.forField(password)
                .withValidator(new StringLengthValidator("Password must be at least 6 characters long", 6, 20))
                .bind(Users::getPassword, Users::setPassword);
        binder.forField(email)
                .withValidator(new EmailValidator("Not a valid email address"))
                .bind(Users::getEmail, Users::setEmail);
    }

}
