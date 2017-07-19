package de.agdb.views.scheduler;


import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.dnd.EffectAllowed;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;

import com.vaadin.ui.themes.ValoTheme;
import de.agdb.views.categories.CategoriesView;
import org.vaadin.addon.calendar.Calendar;
import org.vaadin.addon.calendar.ui.CalendarComponentEvents;


import java.util.Date;


@UIScope
@SpringView(name = SchedulerView.VIEW_NAME)
public class SchedulerView extends VerticalLayout implements View {
    // Empty view name as this will be the initially loaded view
    public static final String VIEW_NAME = "";
    Calendar cal;



    public SchedulerView() {
        setSizeFull();

        VerticalLayout centeringLayout = new VerticalLayout();
        centeringLayout.setHeight("85%");
        centeringLayout.setWidth("85%");
        centeringLayout.setStyleName("solid-border");

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


        addComponent(centeringLayout);


        setComponentAlignment(centeringLayout, Alignment.MIDDLE_CENTER);
        setStyleName("solid-border");



    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

}
