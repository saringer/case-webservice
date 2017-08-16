package de.agdb;

import com.vaadin.annotations.*;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.WrappedSession;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.spring.server.SpringVaadinServlet;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import de.agdb.backend.auth.AccessControl;
import de.agdb.backend.auth.BasicAccessControl;
import de.agdb.views.MainScreen;
import com.vaadin.server.*;
import de.agdb.views.scheduler.SchedulerMainView;
import de.agdb.backend.entities.schedule_wrapper_objects.ScheduleWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.annotation.WebServlet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import de.agdb.views.login.LoginForm;


@SpringUI
@Theme("main_theme")
@Widgetset("WidgetSet")
@PreserveOnRefresh
@Push
public class AppUI extends UI {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private ApplicationContext applicationContext;
    private AccessControl accessControl = new BasicAccessControl();
    private ScheduleWrapper globalScheduleWrapper = new ScheduleWrapper();

    // we can use either constructor autowiring or field autowiring
    @Autowired
    private SpringViewProvider viewProvider;


    @Override
    protected void init(VaadinRequest request) {


        Responsive.makeResponsive(this);

        WrappedSession session = request.getWrappedSession();
        HttpSession httpSession = ((WrappedHttpSession) session).getHttpSession();
        ServletContext servletContext = httpSession.getServletContext();
        applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);


        getPage().setTitle("CaSe");
       if (!accessControl.isUserSignedIn()) {
            setContent(new LoginForm(accessControl, new LoginForm.LoginListener() {
                @Override
                public void loginSuccessful() {
                    showMainView();
                }
            }, viewProvider, AppUI.this));
        } else {
            showMainView();
        }

        //showMainView();


    }

    protected void showMainView() {
        addStyleName(ValoTheme.UI_WITH_MENU);
        setContent(new MainScreen(AppUI.this, viewProvider));
        getNavigator().navigateTo(SchedulerMainView.VIEW_NAME);
    }

    public AccessControl getAccessControl() {
        return accessControl;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public ScheduleWrapper getGlobalScheduleWrapper() {
        return globalScheduleWrapper;
    }

    public void resetGlobalScheduleWrapper() {
        this.globalScheduleWrapper = new ScheduleWrapper();
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = AppUI.class, productionMode = false)
    public static class MyUIServlet extends SpringVaadinServlet {
    }
}
