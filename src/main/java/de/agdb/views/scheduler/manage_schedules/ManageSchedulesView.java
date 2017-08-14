package de.agdb.views.scheduler.manage_schedules;

import com.vaadin.event.LayoutEvents;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Sizeable;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import de.agdb.AppUI;
import de.agdb.backend.entities.Users;
import de.agdb.backend.entities.UsersRepository;
import de.agdb.backend.entities.schedule_wrapper_objects.ScheduleWrapper;
import de.agdb.views.scheduler.create_schedule.calendar_component.CalendarComponent;
import de.agdb.views.scheduler.modal_windows.SetParticipantsWindow;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;

@UIScope
@SpringView(name = ManageSchedulesView.VIEW_NAME)
public class ManageSchedulesView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "ManageSchedulesView";
    private VerticalLayout schedulesList = new VerticalLayout();
    CalendarComponent calendar = new CalendarComponent();
    @Autowired
    UsersRepository usersRepository;

    @PostConstruct
    void init() {
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
        /*HorizontalLayout layout = new HorizontalLayout();
        layout.setSizeFull();

        CalendarComponent c2 = new CalendarComponent();

        //c2.setMargin(false);

        VerticalLayout l = new VerticalLayout();
        l.setSizeFull();
        l.addStyleName("solid-border");

        layout.addComponent(l);
        layout.addComponent(c2);
        layout.setExpandRatio(l, 0.4f);
        layout.setExpandRatio(c2, 0.6f);*/
        HorizontalLayout content = new HorizontalLayout();
        content.setSizeFull();


        calendar.setMargin(true);
        calendar.addStyleName("solid-border-grey");

        //c2.setMargin(false);

        Panel panel = new Panel();
        panel.setSizeFull();
        panel.addStyleName("schedule-list");


        schedulesList.setSizeUndefined();
        schedulesList.setWidth("100%");
       // l.setSpacing(false);
     //   l.setMargin(true);
        schedulesList.addStyleNames("overflow-auto", "schedule-list");

        initSchedules(schedulesList, app);

        panel.setContent(schedulesList);

        content.addComponent(panel);
        content.addComponent(calendar);
        content.setExpandRatio(panel, 0.4f);
        content.setExpandRatio(calendar, 0.6f);



        buildModalWindow();
        return  content;
    }

    private void buildModalWindow(){
        Window window = new SetParticipantsWindow();
        window.setWidth("80%");
        window.setHeight("50%");

        UI.getCurrent().addWindow(window);

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
        title.addStyleNames(ValoTheme.LABEL_H3);

        Label description = new Label();
        description.setValue(schedule.getDescription());
        description.setWidth("100%");

        textLayout.addComponent(title);
        textLayout.addComponent(description);
        wrapperLayout.addComponent(textLayout);

        wrapperLayout.addLayoutClickListener((LayoutEvents.LayoutClickListener) layoutClickEvent -> {
            calendar.clearEvents(true);
            for (int i=0;i<schedule.getDays().size(); i++) {
                calendar.addEvent(schedule.getDays().get(i).getDay(), schedule.getTitle());
            }

        });

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
        invitationsTab.setStyleName("nav-top-inactive");



        horizontalLayout.addComponent(mySchedulesTab);
        horizontalLayout.addComponent(invitationsTab);
        horizontalLayout.setSpacing(false);


        //horizontalLayout.setExpandRatio(generalBar, 1);
        return horizontalLayout;
    }
}
