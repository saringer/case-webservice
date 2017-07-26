package de.agdb.views.login;


import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.navigator.SpringViewProvider;
import de.agdb.AppUI;
import de.agdb.backend.auth.AccessControl;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.context.ApplicationContext;

import java.io.Serializable;

public class LoginForm extends CssLayout {


    private TextField username;
    private PasswordField password;
    private Button login;
    private Button forgotPassword;
    private Button register;
    private LoginListener loginListener;
    private AccessControl accessControl;
    private SpringViewProvider viewProvider;
    private AppUI ui;



    public LoginForm(AccessControl accessControl, LoginListener loginListener, SpringViewProvider viewProvider, AppUI ui) {
        this.viewProvider = viewProvider;
        this.ui = ui;
        buildUI();
        this.loginListener = loginListener;
        this.accessControl = accessControl;

    }


    private void buildUI() {
        addStyleName("login-screen");


        // login form, centered in the available part of the screen
        Component loginForm = buildLoginForm();


        // layout to center login form when there is sufficient screen space
        VerticalLayout centeringLayout = new VerticalLayout();
        centeringLayout.setStyleName("centering-layout");

        centeringLayout.addComponent(loginForm);

        centeringLayout.setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);


        addComponent(centeringLayout);


    }

    private Component buildLoginForm() {

        VerticalLayout containerLayout = new VerticalLayout();
        containerLayout.setSizeUndefined();
        containerLayout.addStyleName("login-form");
        containerLayout.setWidth(472, Unit.PIXELS);


        Label loginFormCaption = new Label(String.format("<font size = \"5\" color=\"white\"> CaSe - Categorical Scheduler")
                , ContentMode.HTML);
        loginFormCaption.setWidth(null);


        FormLayout loginForm = new FormLayout();

        //loginForm.addStyleName("login-form");
        loginForm.setSizeUndefined();
        // loginForm.setMargin(true);

        loginForm.addComponent(username = new TextField("Username:"));
        username.setWidth(15, Unit.EM);

        loginForm.addComponent(password = new PasswordField("Password:"));
        password.setWidth(15, Unit.EM);

        CssLayout buttons = new CssLayout();
        buttons.setStyleName("buttons");
        loginForm.addComponent(forgotPassword = new Button("Forgot password?"));
        forgotPassword.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                showNotification(new Notification("Hint: Try anything"));
            }
        });
        forgotPassword.addStyleName(ValoTheme.BUTTON_LINK);
        loginForm.addComponent(buttons);
        loginForm.setComponentAlignment(username, Alignment.MIDDLE_CENTER);

        buttons.addComponent(login = new Button("Login"));
        login.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        login.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        LoginButtonListener loginButtonListener = getLoginButtonListener();
        login.addClickListener(loginButtonListener);

        buttons.addComponent((register = new Button("No account yet?")));
        register.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {

                UI.getCurrent().setContent(new RegisterForm(viewProvider, ui, accessControl));
            }
        });
        register.addStyleName(ValoTheme.BUTTON_LINK);

        containerLayout.addComponent(loginFormCaption);
        containerLayout.addComponent(loginForm);

        containerLayout.setComponentAlignment(loginFormCaption, Alignment.MIDDLE_CENTER);
        containerLayout.setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);
        return containerLayout;
    }


    public TextField getTxtLogin() {
        return username;
    }

    public PasswordField getTxtPassword() {
        return password;
    }

    public LoginButtonListener getLoginButtonListener() {
        AppUI ui = (AppUI) UI.getCurrent();
        ApplicationContext context = ui.getApplicationContext();
        return context.getBean(LoginButtonListener.class);
    }


    private void showNotification(Notification notification) {
        // keep the notification visible a little while after moving the
        // mouse, or until clicked
        notification.setDelayMsec(2000);
        notification.show(Page.getCurrent());
    }

    public LoginListener getLoginListener() {
        return this.loginListener;
    }


    public interface LoginListener extends Serializable {
        void loginSuccessful();
    }

    public SpringViewProvider getViewProvider() {
        return this.viewProvider;
    }

    public AppUI getAppUI() {
        return this.ui;
    }

    public AccessControl getAccessControl() {
        return accessControl;
    }



}
