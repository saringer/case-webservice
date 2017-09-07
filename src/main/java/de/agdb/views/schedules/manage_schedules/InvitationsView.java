package de.agdb.views.schedules.manage_schedules;

import com.vaadin.event.LayoutEvents;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Sizeable;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import de.agdb.AppUI;
import de.agdb.backend.data_model.DailyEvent;
import de.agdb.backend.data_model.Users;
import de.agdb.backend.data_model.repositories.*;
import de.agdb.custom_components.modal_windows.HandleInvitationsWindow;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@UIScope
@SpringView(name = InvitationsView.VIEW_NAME)
public class InvitationsView extends VerticalLayout implements View {
    public static final String VIEW_NAME = "InvitationsView";
    private VerticalLayout schedulesList = new VerticalLayout();
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private DailyEventRepository dailyEventRepository;
    @Autowired
    private DateWrapperRepository dateWrapperRepository;
    @Autowired
    TimeLocationWrapperRepository timeLocationWrapperRepository;
    @Autowired
    CategoriesWrapperRepository categoriesWrapperRepository;
    private List<HorizontalLayout> scheduleItemsList = new ArrayList<>();


    @PostConstruct
    void init() {
        AppUI app = (AppUI) UI.getCurrent();
        setSizeFull();
        VerticalLayout formWrapper = new VerticalLayout();
        formWrapper.setWidth("90%");
        formWrapper.setHeight("80%");
        addComponent(formWrapper);
        setComponentAlignment(formWrapper, Alignment.MIDDLE_CENTER);


        VerticalLayout content = buildContent(app);
        content.setSpacing(false);
        content.setMargin(false);
        content.setHeight("80%");
        content.setWidth("70%");


        HorizontalLayout topNavBar = createTopNavBar();


        formWrapper.addComponent(topNavBar);
        formWrapper.addStyleName("solid-border");
        formWrapper.setSpacing(false);
        formWrapper.setMargin(false);
        formWrapper.addComponent(content);
        formWrapper.setComponentAlignment(content, Alignment.MIDDLE_CENTER);
        formWrapper.setExpandRatio(content, 1);
    }

    private VerticalLayout buildContent(AppUI app) {


        CssLayout headerLayout = createWindowHeader();
        headerLayout.setWidth("100%");
        headerLayout.setHeight(30, Sizeable.Unit.PIXELS);

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();


        Panel scheduleListPanel = new Panel();
        scheduleListPanel.setSizeFull();
        scheduleListPanel.addStyleNames("schedule-list", "solid-border");


        schedulesList.setSizeUndefined();
        schedulesList.setWidth("100%");

        schedulesList.addStyleNames("overflow-auto", "schedule-list");


        scheduleListPanel.setContent(schedulesList);


        content.addComponent(headerLayout);
        content.addComponent(scheduleListPanel);
        content.setExpandRatio(scheduleListPanel, 1);
        content.setComponentAlignment(scheduleListPanel, Alignment.MIDDLE_CENTER);


        return content;
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
        mySchedulesTab.setStyleName("manage-schedules-nav-top-inactive");
        mySchedulesTab.addLayoutClickListener((LayoutEvents.LayoutClickListener) event -> {
            UI.getCurrent().getNavigator().navigateTo("ManageSchedulesView");
        });

        CssLayout invitationsTab = new CssLayout();
        invitationsTab.setWidth("100%");
        invitationsTab.setHeight(30, Sizeable.Unit.PIXELS);
        header = new Label("Invitations");
        header.setSizeUndefined();
        invitationsTab.addComponent(header);
        invitationsTab.setStyleName("nav-top-active");


        horizontalLayout.addComponent(mySchedulesTab);
        horizontalLayout.addComponent(invitationsTab);
        horizontalLayout.setSpacing(false);


        //horizontalLayout.setExpandRatio(generalBar, 1);
        return horizontalLayout;
    }

    private CssLayout createWindowHeader() {
        CssLayout headerLayout = new CssLayout();
        Label headerLabel = new Label("Upcoming");
        headerLayout.setSizeUndefined();
        headerLayout.addComponent(headerLabel);
        headerLayout.addStyleName("invitations-header");
        return headerLayout;
    }

    private void initSchedules(VerticalLayout schedulesListLayout, AppUI app) {
        schedulesListLayout.removeAllComponents();
        String userName = app.getCurrentUsername();
        if (!usersRepository.findByUsername(userName).isEmpty()) {
            Users thisUser = usersRepository.findByUsername(userName).get(0);

            List<DailyEvent> dailyEvents = thisUser.getEvents();

            /**
             * Load up daily events
             */
            for (int i = 0; i < dailyEvents.size(); i++) {
                schedulesListLayout.addComponent(buildItem(dailyEvents.get(i)));

            }

        }

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        AppUI app = (AppUI) UI.getCurrent();
        initSchedules(schedulesList, app);

    }

    private HorizontalLayout buildItem(DailyEvent dailyEvent) {
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



//        Label title = new Label(dateWrapperRepository.findOne(events.get(0).getDateWrapperId()).getDay().toString());
        Label title = new Label(dailyEvent.getTitle());
        //title.addStyleNames(ValoTheme.LABEL_H3);

        Label description = new Label();
        description.setValue(dailyEvent.getDescription());
        description.setWidth("100%");

        textLayout.addComponent(title);
        textLayout.addComponent(description);
        wrapperLayout.addComponent(textLayout);

        wrapperLayout.addLayoutClickListener((LayoutEvents.LayoutClickListener) layoutClickEvent -> {
            /**
             * When the invitation is clicked we set the respective flag in order to remove this event from the notification
             * list for unread invitations.
             */
            DailyEvent updatedEvent = dailyEventRepository.findOne(dailyEvent.getId());
            updatedEvent.setHasBeenReadOnce(true);
            dailyEventRepository.save(updatedEvent);
            updatedEvent = dailyEventRepository.findOne(dailyEvent.getId());
            Window window = new HandleInvitationsWindow(updatedEvent, timeLocationWrapperRepository, categoriesWrapperRepository, usersRepository, dateWrapperRepository);
            window.setWidth("60%");
            window.setHeight(600, Unit.PIXELS);

            UI.getCurrent().addWindow(window);

            setActiveView(wrapperLayout );

        });

        scheduleItemsList.add(wrapperLayout);

        return wrapperLayout;


    }

    public void setActiveView(HorizontalLayout e) {
        for (int i = 0; i < scheduleItemsList.size(); i++) {
            scheduleItemsList.get(i).removeStyleName("schedule-list-item-selected");
            scheduleItemsList.get(i).setStyleName("schedule-list-item");
        }
        e.setStyleName("schedule-list-item-selected");
    }

}
