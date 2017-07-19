package de.agdb.views.scheduler;


import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;

import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.addon.calendar.Calendar;
import org.vaadin.addon.calendar.ui.CalendarComponentEvents;


import java.util.Date;


@UIScope
@SpringView(name = SchedulerMainView.VIEW_NAME)
public class SchedulerMainView extends VerticalLayout implements View {
    // Empty view name as this will be the initially loaded view
    public static final String VIEW_NAME = "";
    Calendar cal;



    public SchedulerMainView() {
        setSizeFull();

        VerticalLayout centeringLayout = new VerticalLayout();
        centeringLayout.setHeight("85%");
        centeringLayout.setWidth("85%");
        centeringLayout.setStyleName("solid-border");
        centeringLayout.setMargin(false);

        cal = new Calendar();
        cal.setSizeFull();
        cal.setStartDate(new Date());
        cal.setEndDate(new HelperClass().getCurrentDatePlusOneYear());
        // disable weekly-view
        cal.setHandler((CalendarComponentEvents.WeekClickHandler)null);
        // disable daily-view
        cal.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);


       // calendar.setEndDate(new HelperClass().getCurrentDatePlusOneYear());




        centeringLayout.addComponent(cal);

        HorizontalLayout bottomNav = createBottomNav();
        centeringLayout.addComponent(bottomNav);
        centeringLayout.setExpandRatio(cal, 0.85f);
        centeringLayout.setExpandRatio(bottomNav, 0.15f);


        addComponent(centeringLayout);

        setComponentAlignment(centeringLayout, Alignment.MIDDLE_CENTER);
        setStyleName("solid-border");



    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

    private HorizontalLayout createBottomNav() {
        Label leftMenuIcon = new Label();
        leftMenuIcon.setIcon(VaadinIcons.CALENDAR_O);
        leftMenuIcon.addStyleName(ValoTheme.LABEL_HUGE);
        Label leftMenuCaption = new Label("Create schedule");
        leftMenuCaption.addStyleName(ValoTheme.LABEL_H2);

        HorizontalLayout nav = new HorizontalLayout();
        nav.setSizeFull();

        VerticalLayout createSchedulesButton = new VerticalLayout();
        createSchedulesButton.setSizeFull();
        createSchedulesButton.addStyleName("left-menu-style");
        //createSchedulesButton.addComponent(leftMenuIcon);
        //createSchedulesButton.addComponent(leftMenuCaption);
        Button buttonLeft = new Button("Create schedule");
        buttonLeft.setSizeUndefined();
        buttonLeft.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        buttonLeft.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
        buttonLeft.setIcon(VaadinIcons.CALENDAR_O);
        buttonLeft.addStyleName(ValoTheme.BUTTON_LARGE);
        createSchedulesButton.addComponent(buttonLeft);

        VerticalLayout manageSchedulesButton = new VerticalLayout();
        manageSchedulesButton.setSizeFull();
        manageSchedulesButton.addStyleName("right-menu-style");
        Button buttonRight = new Button("Manage schedules");
        buttonRight.setSizeFull();
        buttonRight.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        buttonRight.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
        buttonRight.setIcon(VaadinIcons.CALENDAR);
        buttonRight.addStyleName(ValoTheme.BUTTON_LARGE);
        manageSchedulesButton.addComponent(buttonRight);

        nav.addComponent(createSchedulesButton);
        nav.addComponent(manageSchedulesButton);
        nav.setExpandRatio(createSchedulesButton, 0.5f);
        nav.setExpandRatio(manageSchedulesButton, 0.5f);
        return nav;
    }

}
