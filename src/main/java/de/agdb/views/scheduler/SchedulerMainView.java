package de.agdb.views.scheduler;


import com.vaadin.event.LayoutEvents;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;

import com.vaadin.ui.themes.ValoTheme;
import de.agdb.views.scheduler.create_schedule.calendar_component.CalendarComponent;
import org.vaadin.addon.calendar.Calendar;


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
        bottomNav.setSizeUndefined();
        bottomNav.setWidth("100%");


       VerticalLayout form = new VerticalLayout();
       form.setWidth("80%");
       form.setHeight("80%");
       form.setMargin(false);
       form.setSpacing(false);
       //form.addStyleNames("solid-border");

        CalendarComponent calendar = new CalendarComponent();
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

}
