package de.agdb.views.login;

import com.vaadin.event.ShortcutAction;

import com.vaadin.shared.ui.ContentMode;

import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import de.agdb.AppUI;
import de.agdb.auth.AccessControl;
import de.agdb.views.MainScreen;
import de.agdb.views.scheduler.SchedulerView;
import org.springframework.context.ApplicationContext;


/**
 * Created by Riva on 18.07.2017.
 */
public class RegisterForm extends CssLayout {

    private TextField username;
    private TextField email;
    private PasswordField password;
    private Button registerButton;
    private Button cancelButton;
    private SpringViewProvider viewProvider;
    private AppUI ui;
    private AccessControl accessControl;



    public RegisterForm(SpringViewProvider viewProvider, AppUI ui, AccessControl accessControl) {
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

        CssLayout buttons = new CssLayout();
        buttons.setStyleName("buttons");

        buttons.addComponent(registerButton = new Button("Create"));
        registerButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        registerButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        RegisterButtonListener registerButtonListener = getRegisterButtonListener();
        registerButton.addClickListener(registerButtonListener);


        buttons.addComponent((cancelButton = new Button("Cancel")));
        cancelButton.addStyleName(ValoTheme.BUTTON_DANGER);
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


    protected void showMainView() {
        addStyleName(ValoTheme.UI_WITH_MENU);
        UI.getCurrent().setContent(new MainScreen(ui, viewProvider));
        UI.getCurrent().getNavigator().navigateTo(SchedulerView.VIEW_NAME);
    }





}
