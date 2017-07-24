package de.agdb.views.scheduler;


import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;

import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.ui.components.calendar.handler.BasicDateClickHandler;
import org.vaadin.addon.calendar.Calendar;
import org.vaadin.addon.calendar.ui.CalendarComponentEvents;


import javax.xml.soap.Text;
import java.text.Normalizer;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


@UIScope
@SpringView(name = SchedulerMainView.VIEW_NAME)
public class SchedulerMainView extends VerticalLayout implements View {
    // Empty view name as this will be the initially loaded view
    public static final String VIEW_NAME = "";
    Calendar cal;



    public SchedulerMainView() {
        setSizeFull();
        VerticalLayout formWrapper = new VerticalLayout();
        formWrapper.setSizeFull();
        addComponent(formWrapper);
        setComponentAlignment(formWrapper, Alignment.MIDDLE_CENTER);






        HorizontalLayout bottomNav = createBottomNav();
        bottomNav.setWidth("100%");


       FormLayout form = new FormLayout();
       form.setWidth("80%");
       form.setHeight("80%");
       form.setMargin(true);
       //form.addStyleNames("solid-border");


       form.addComponent(new CalendarComponent());
       form.addComponent(bottomNav);

//        form.setComponentAlignment(bottomNav, Alignment.TOP_CENTER);



        formWrapper.setMargin(true);
        formWrapper.setSpacing(true);
        formWrapper.addComponent(form);
        formWrapper.setComponentAlignment(form, Alignment.MIDDLE_CENTER);








    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

    private HorizontalLayout createBottomNav() {


        HorizontalLayout nav = new HorizontalLayout();


        VerticalLayout createSchedulesButton = new VerticalLayout();
        //createSchedulesButton.setSizeFull();
        createSchedulesButton.setWidth("100%");
        createSchedulesButton.addStyleName("left-menu-style");
        //createSchedulesButton.addComponent(leftMenuIcon);
        //createSchedulesButton.addComponent(leftMenuCaption);
        Button buttonLeft = new Button("Create schedule");
        buttonLeft.setSizeUndefined();
        buttonLeft.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        //buttonLeft.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
        //buttonLeft.setIcon(VaadinIcons.CALENDAR_O);
        buttonLeft.addStyleName(ValoTheme.BUTTON_LARGE);
        createSchedulesButton.addComponent(buttonLeft);

        VerticalLayout manageSchedulesButton = new VerticalLayout();
        //manageSchedulesButton.setSizeFull();
        manageSchedulesButton.setWidth("100%");
        manageSchedulesButton.addStyleName("right-menu-style");
        Button buttonRight = new Button("Manage schedules");
        buttonRight.setSizeUndefined();
        buttonRight.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        //buttonRight.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
        //buttonRight.setIcon(VaadinIcons.CALENDAR);
        buttonRight.addStyleName(ValoTheme.BUTTON_LARGE);
        manageSchedulesButton.addComponent(buttonRight);

        nav.addComponent(createSchedulesButton);
        nav.addComponent(manageSchedulesButton);
        nav.setMargin(false);
        nav.setSpacing(false);
        //nav.setExpandRatio(createSchedulesButton, 1);
       // nav.setExpandRatio(manageSchedulesButton, 1);
        return nav;
    }

}
