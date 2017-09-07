package de.agdb.views.schedules.manage_schedules;

import com.vaadin.event.LayoutEvents;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Sizeable;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import de.agdb.AppUI;
import de.agdb.backend.data_model.repositories.*;
import de.agdb.backend.data_model.Users;
import de.agdb.backend.data_model.schedule_wrapper_objects.ScheduleWrapper;
import de.agdb.custom_components.calendar_component.CalendarComponent;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@UIScope
@SpringView(name = ManageSchedulesView.VIEW_NAME)
public class ManageSchedulesView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "ManageSchedulesView";
    private VerticalLayout schedulesList = new VerticalLayout();

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private CategoriesWrapperRepository categoriesWrapperRepository;
    @Autowired
    private DailyEventRepository dailyEventRepository;
    @Autowired
    private TimeLocationWrapperRepository timeLocationWrapperRepository;
    private List<HorizontalLayout> scheduleItemsList = new ArrayList<>();
    private CalendarComponent calendar;


    @PostConstruct
    void init() {
        calendar = new CalendarComponent(categoriesWrapperRepository, usersRepository, dailyEventRepository, timeLocationWrapperRepository);
        AppUI app = (AppUI) UI.getCurrent();
        setSizeFull();
        VerticalLayout formWrapper = new VerticalLayout();
        formWrapper.setWidth("90%");
        formWrapper.setHeight("80%");
        addComponent(formWrapper);
        setComponentAlignment(formWrapper, Alignment.MIDDLE_CENTER);



        HorizontalLayout content = buildContent(app);
        content.setSpacing(false);
        content.setMargin(false);
        content.setHeight("80%");
        content.setWidth("90%");





        HorizontalLayout topNavBar = createTopNavBar();



        formWrapper.addComponent(topNavBar);
        formWrapper.addStyleName("solid-border");
        formWrapper.setSpacing(false);
        formWrapper.setMargin(false);
        formWrapper.addComponent(content);
        formWrapper.setComponentAlignment(content, Alignment.MIDDLE_CENTER);
        formWrapper.setExpandRatio(content, 1);
    }

    private HorizontalLayout buildContent(AppUI app) {

        HorizontalLayout content = new HorizontalLayout();
        content.setSizeFull();


        calendar.setMargin(true);



        Panel scheduleListPanel = new Panel();
        scheduleListPanel.setSizeFull();
        scheduleListPanel.addStyleNames("schedule-list", "solid-border");


        schedulesList.setSizeUndefined();
        schedulesList.setWidth("100%");

        schedulesList.addStyleNames("overflow-auto", "schedule-list");

        initSchedules(schedulesList, app);

        scheduleListPanel.setContent(schedulesList);


        calendar.setSizeUndefined();
        calendar.setWidth("100%");
        calendar.setHeight("90%");
        calendar.setMargin(true);
        calendar.setSpacing(true);
        Panel calendarPanel = new Panel();
        calendarPanel.addStyleName("solid-border");
        calendarPanel.setSizeFull();
        calendarPanel.setContent(calendar);


        content.addComponent(scheduleListPanel);
        content.addComponent(calendarPanel);
        content.setExpandRatio(scheduleListPanel, 0.4f);
        content.setExpandRatio(calendarPanel, 0.6f);



        return  content;
    }


    private void initSchedules(VerticalLayout schedulesListLayout, AppUI app) {
        schedulesListLayout.removeAllComponents();
        String userName = app.getAccessControl().getUsername();
        if (!usersRepository.findByUsername(userName).isEmpty()) {
            Users thisUser = usersRepository.findByUsername(userName).get(0);

            List<ScheduleWrapper> schedules = thisUser.getSchedules();
            for (int i=0;i<schedules.size();i++) {
                schedulesListLayout.addComponent(buildItem(schedules.get(i)));
            }
        }

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        AppUI app = (AppUI) UI.getCurrent();
        calendar.clearEvents(true);
        initSchedules(schedulesList, app);

    }

    private HorizontalLayout buildItem(ScheduleWrapper schedule) {
        HorizontalLayout wrapperLayout = new HorizontalLayout();
        wrapperLayout.setSpacing(false);
        wrapperLayout.setMargin(false);
        wrapperLayout.addStyleNames("schedule-list-item");
        wrapperLayout.setSizeUndefined();
        wrapperLayout.setWidth("100%");

        VerticalLayout textLayout = new VerticalLayout();
        textLayout.setSizeFull();
        textLayout.setMargin(false);
        textLayout.setSpacing(false);

        Label title = new Label(schedule.getTitle());
        //title.addStyleNames(ValoTheme.LABEL_H3);

        Label description = new Label();
        description.setValue(schedule.getDescription());
        description.setWidth("100%");

        textLayout.addComponent(title);
        textLayout.addComponent(description);
        wrapperLayout.addComponent(textLayout);

        wrapperLayout.addLayoutClickListener((LayoutEvents.LayoutClickListener) layoutClickEvent -> {
            calendar.clearEvents(true);
            for (int i=0;i<schedule.getDays().size(); i++) {
                calendar.addEvent(schedule.getDays().get(i), schedule.getTitle(), schedule.getDescription());
            }
            setActiveView(wrapperLayout );

        });

        scheduleItemsList.add(wrapperLayout);

        return wrapperLayout;



    }


    private HorizontalLayout createTopNavBar() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setWidth("100%");
        horizontalLayout.setHeight(30, Sizeable.Unit.PIXELS);

        CssLayout mySchedulesTab = new CssLayout();
        mySchedulesTab.setWidth("100%");
        mySchedulesTab.setHeight(30, Sizeable.Unit.PIXELS);
        Label header = new Label("My schedules");
        header.setSizeUndefined();
        mySchedulesTab.addComponent(header);
        mySchedulesTab.setStyleName("nav-top-active");

        CssLayout invitationsTab = new CssLayout();
        invitationsTab.setWidth("100%");
        invitationsTab.setHeight(30, Sizeable.Unit.PIXELS);
        header = new Label("Invitations");
        header.setSizeUndefined();
        invitationsTab.addComponent(header);
        invitationsTab.setStyleName("manage-schedules-nav-top-inactive");
        invitationsTab.addLayoutClickListener((LayoutEvents.LayoutClickListener) event -> {
            UI.getCurrent().getNavigator().navigateTo("InvitationsView");
        });



        horizontalLayout.addComponent(mySchedulesTab);
        horizontalLayout.addComponent(invitationsTab);
        horizontalLayout.setSpacing(false);


        //horizontalLayout.setExpandRatio(generalBar, 1);
        return horizontalLayout;
    }

    public void setActiveView(HorizontalLayout e) {
       for (int i=0;i<scheduleItemsList.size();i++){
           scheduleItemsList.get(i).removeStyleName("schedule-list-item-selected");
           scheduleItemsList.get(i).setStyleName("schedule-list-item");
       }
       e.setStyleName("schedule-list-item-selected");
    }
}
