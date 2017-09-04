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
import de.agdb.backend.broadcaster.Broadcaster;
import de.agdb.backend.entities.Users;
import de.agdb.backend.entities.repositories.UsersRepository;
import de.agdb.views.MainScreen;
import com.vaadin.server.*;
import de.agdb.views.scheduler.SchedulerMainView;
import de.agdb.backend.entities.schedule_wrapper_objects.ScheduleWrapper;

import de.agdb.views.scheduler.modal_windows.SetParticipantsWindow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.annotation.WebServlet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import de.agdb.views.login.LoginForm;
import org.vaadin.addons.*;
import org.vaadin.addons.builder.ToastBuilder;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.vaadin.addons.builder.ToastOptionsBuilder.having;

/*
Add @UIScope. That will ensure you get one instance of this class per session
 */
@SpringUI
@Theme("main_theme")
@Widgetset("WidgetSet")
@PreserveOnRefresh
@Push
public class AppUI extends UI implements Broadcaster.BroadcastListener, ToastrListener {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private ApplicationContext applicationContext;

    private AccessControl accessControl = new BasicAccessControl();
    private ScheduleWrapper globalScheduleWrapper = new ScheduleWrapper();
    private Toastr toastr = new Toastr();
    private String currentUsername;
    private ConnectorTracker tracker;
    private SetParticipantsWindow window;

    // we can use either constructor autowiring or field autowiring
    @Autowired
    private SpringViewProvider viewProvider;
    @Autowired
    private UsersRepository usersRepository;


    @Override
    protected void init(VaadinRequest request) {
        Broadcaster.register(this);
        Responsive.makeResponsive(this);

        WrappedSession session = request.getWrappedSession();
        HttpSession httpSession = ((WrappedHttpSession) session).getHttpSession();
        ServletContext servletContext = httpSession.getServletContext();
        applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);


        getPage().setTitle("CaSe");
        if (!accessControl.isUserSignedIn()) {
            setContent(new LoginForm(accessControl, (LoginForm.LoginListener) () -> showMainView(), viewProvider, AppUI.this));
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

    public Toastr getGlobalToastr() {
        return toastr;
    }

    public String getCurrentUsername() {
        return this.currentUsername;
    }

    public void setCurrentUsername(String userName) {
        this.currentUsername = userName;
    }


    public void resetGlobalScheduleWrapper() {
        this.globalScheduleWrapper = new ScheduleWrapper();
    }

    @Override
    public void onShown() {

    }

    @Override
    public void onHidden() {

    }

    @Override
    public void onClick() {
        getNavigator().navigateTo("InvitationsView");
    }

    @Override
    public void onCloseButtonClick() {

    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = AppUI.class, productionMode = false)
    public static class MyUIServlet extends SpringVaadinServlet {
    }

    @Override
    public ConnectorTracker getConnectorTracker() {
        if (this.tracker == null) {
            this.tracker = new ConnectorTracker(this) {

                @Override
                public void registerConnector(ClientConnector connector) {
                    try {
                        super.registerConnector(connector);
                    } catch (RuntimeException e) {
                        System.out.println("Failed Connector" + connector.getClass().getSimpleName());
                        Logger.getLogger("").log(Level.SEVERE, "Failed connector: {0}", connector.getClass().getSimpleName());
                        throw e;
                    }
                }

            };
        }

        return tracker;
    }

    private void loadInvitationsNotification() {
        AppUI app = (AppUI) UI.getCurrent();
        if (app.getCurrentUsername() != null) {
            Users thisUser = usersRepository.findByUsername(app.getCurrentUsername()).get(0);
            int unreadInvitations = 0;
            for (int i = 0; i < thisUser.getEvents().size(); i++) {
                if (thisUser.getEvents().get(i).isHasBeenReadOnce() == false) {
                    unreadInvitations++;
                }
            }
            if (unreadInvitations > 0) {
                toastr.toast(
                        ToastBuilder.of(ToastType.valueOf("Info"), unreadInvitations + " unread invitations ")
                                //.caption("Title")
                                .options(having()
                                        .closeButton(false)
                                        .newestOnTop(true)
                                        .tapToDismiss(true)
                                        .position(ToastPosition.Top_Right)
                                        .rightToLeft(false)
                                        .timeOut(0)
                                        .extendedTimeOut(0)
                                        .hideDuration(0)
                                        .showEasing(ToastEasing.Swing)
                                        .build())
                                .build());
            }
        }

    }

    @Override
    public void detach() {
        Broadcaster.unregister(this);
        super.detach();
    }


    @Override
    public void receiveBroadcast(String message) {
        // Must lock the session to execute logic safely
        access(new Runnable() {
            @Override
            public void run() {
               toastr.remove();
               loadInvitationsNotification();
            }
        });
    }
}

