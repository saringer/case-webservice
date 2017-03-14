package de.agdb;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.WrappedSession;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import de.agdb.auth.AccessControl;
import de.agdb.auth.BasicAccessControl;
import de.agdb.views.MainScreen;
import com.vaadin.server.*;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import de.agdb.views.login.LoginForm;

import javax.servlet.annotation.WebServlet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;



@SpringUI
@Theme("main_theme")
public class AppUI extends UI {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private ApplicationContext applicationContext;
    private AccessControl accessControl = new BasicAccessControl();

    @Override
    protected void init(VaadinRequest request) {


        Responsive.makeResponsive(this);

        WrappedSession session = request.getWrappedSession();
        HttpSession httpSession = ((WrappedHttpSession) session).getHttpSession();
        ServletContext servletContext = httpSession.getServletContext();
        applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);


        getPage().setTitle("CRYSIS");
        if (!accessControl.isUserSignedIn()) {
            setContent(new LoginForm(accessControl, new LoginForm.LoginListener() {


                @Override
                public void loginSuccessful() {
                    showMainView();
                }
            }));
        } else {
            showMainView();
        }
    }

    protected void showMainView() {
        addStyleName(ValoTheme.UI_WITH_MENU);
        setContent(new MainScreen(AppUI.this));
        //getNavigator().navigateTo(getNavigator().getState());
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }


    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = AppUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
