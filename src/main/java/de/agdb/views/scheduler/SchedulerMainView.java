package de.agdb.views.scheduler;


import com.vaadin.event.LayoutEvents;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;

import com.vaadin.ui.themes.ValoTheme;
import de.agdb.AppUI;
import de.agdb.backend.entities.Users;
import de.agdb.backend.entities.repositories.*;
import de.agdb.backend.entities.schedule_wrapper_objects.ScheduleWrapper;
import de.agdb.views.scheduler.create_schedule.calendar_component.CalendarComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.vaadin.addon.calendar.Calendar;
import org.vaadin.addons.*;
import org.vaadin.addons.builder.ToastBuilder;

import javax.annotation.PostConstruct;
import java.util.List;

import static org.vaadin.addons.builder.ToastOptionsBuilder.having;


@UIScope
@SpringView(name = SchedulerMainView.VIEW_NAME)
public class SchedulerMainView extends VerticalLayout implements View, ToastrListener {
    // Empty view name as this will be the initially loaded view
    public static final String VIEW_NAME = "";
    private Toastr toastr;
    private CalendarComponent calendar;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    CategoriesRepository categoriesRepository;
    @Autowired
    CategoriesWrapperRepository categoriesWrapperRepository;
    @Autowired
    TimeLocationWrapperRepository timeLocationWrapperRepository;
    @Autowired
    DailyEventRepository dailyEventRepository;


    @PostConstruct
    void init() {
        AppUI app = (AppUI) UI.getCurrent();
        this.toastr = app.getGlobalToastr();

        setSizeFull();
        VerticalLayout formWrapper = new VerticalLayout();
        formWrapper.setSizeFull();
        addComponent(formWrapper);
        setComponentAlignment(formWrapper, Alignment.MIDDLE_CENTER);


        HorizontalLayout bottomNav = createBottomNav();
        bottomNav.setSizeUndefined();
        bottomNav.setWidth("100%");


        VerticalLayout form = new VerticalLayout();
        form.setWidth("80%");
        form.setHeight("80%");
        form.setMargin(false);
        form.setSpacing(false);
        //form.addStyleNames("solid-border");

        calendar = new CalendarComponent(categoriesWrapperRepository, usersRepository, dailyEventRepository);
        calendar.setSizeUndefined();
        calendar.setWidth("100%");
        calendar.setHeight("90%");
        calendar.setMargin(true);
        calendar.setSpacing(true);
        Panel panel = new Panel();
        panel.addStyleName("solid-border");
        panel.setSizeFull();
        panel.setContent(calendar);
        // calendar.setHeight(500, Unit.PIXELS);
        form.addComponent(panel);
        form.addComponent(bottomNav);
        form.setExpandRatio(panel, 1);
        //form.setExpandRatio(bottomNav, 0.2f);

//        form.setComponentAlignment(bottomNav, Alignment.TOP_CENTER);


        formWrapper.setMargin(false);
        formWrapper.setSpacing(false);
        formWrapper.addComponent(form);
        formWrapper.setComponentAlignment(form, Alignment.MIDDLE_CENTER);
        setExpandRatio(formWrapper, 1);

        toastr.registerToastrListener(this);

        form.addComponent(toastr);

        //initCalendarEvents();


    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        toastr.remove();


        if (event.getParameters().length() > 0) {
            toastr.toast(ToastBuilder.success("Event created").build());

        }
        initCalendarEvents();
        loadInvitationsNotification();


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
                        ToastBuilder.of(ToastType.valueOf("Info"), unreadInvitations + " unread invitations " + app.getCurrentUsername())
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


    private void initCalendarEvents() {
        AppUI app = (AppUI) UI.getCurrent();

        if (!usersRepository.findByUsername(app.getCurrentUsername()).isEmpty()) {
            calendar.clearEvents(true);
            Users user = usersRepository.findByUsername(app.getCurrentUsername()).get(0);
            List<ScheduleWrapper> schedules = user.getSchedules();
            for (int i = 0; i < schedules.size(); i++) {
                for (int x = 0; x < schedules.get(i).getDays().size(); x++) {
                    calendar.addEvent(schedules.get(i).getDays().get(x), schedules.get(i).getTitle());
                }
            }
        }
    }

    private HorizontalLayout createBottomNav() {


        HorizontalLayout nav = new HorizontalLayout();


        VerticalLayout createSchedulesButton = new VerticalLayout();
        //createSchedulesButton.setSizeFull();
        createSchedulesButton.setWidth("100%");
        createSchedulesButton.addStyleName("left-menu-style");
        //createSchedulesButton.addComponent(leftMenuIcon);
        //createSchedulesButton.addComponent(leftMenuCaption);
        Label buttonLeft = new Label("Create schedule");
        buttonLeft.setSizeUndefined();
        buttonLeft.addStyleName(ValoTheme.LABEL_LARGE);
        createSchedulesButton.addComponent(buttonLeft);
        createSchedulesButton.addLayoutClickListener((LayoutEvents.LayoutClickListener) layoutClickEvent -> {
            UI.getCurrent().getNavigator().navigateTo("GeneralView");
        });

        VerticalLayout manageSchedulesButton = new VerticalLayout();
        //manageSchedulesButton.setSizeFull();
        manageSchedulesButton.setWidth("100%");
        manageSchedulesButton.addStyleName("right-menu-style");
        Label buttonRight = new Label("Manage schedules");
        buttonRight.setSizeUndefined();
        buttonRight.addStyleName(ValoTheme.LABEL_LARGE);
        manageSchedulesButton.addComponent(buttonRight);
        manageSchedulesButton.addLayoutClickListener((LayoutEvents.LayoutClickListener) layoutClickEvent -> {
            UI.getCurrent().getNavigator().navigateTo("ManageSchedulesView");
        });

        nav.addComponent(createSchedulesButton);
        nav.addComponent(manageSchedulesButton);
        nav.setMargin(false);
        nav.setSpacing(false);
        //nav.setExpandRatio(createSchedulesButton, 1);
        // nav.setExpandRatio(manageSchedulesButton, 1);
        return nav;
    }


    @Override
    public void onShown() {

    }

    @Override
    public void onHidden() {

        AppUI app = (AppUI) UI.getCurrent();


    }

    @Override
    public void onClick() {
        UI.getCurrent().getNavigator().navigateTo("InvitationsView");
    }

    @Override
    public void onCloseButtonClick() {

    }
}
