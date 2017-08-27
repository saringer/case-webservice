package de.agdb.views.scheduler.create_schedule;

import com.vaadin.event.LayoutEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import de.agdb.AppUI;
import de.agdb.backend.entities.schedule_wrapper_objects.DateWrapper;
import de.agdb.views.scheduler.CustomButton;
import de.agdb.views.scheduler.create_schedule.calendar_component.CalendarComponent;

import javax.annotation.PostConstruct;
import java.util.List;

@UIScope
@SpringView(name = SetDateView.VIEW_NAME)
public class SetDateView extends VerticalLayout implements View {
    public static final String VIEW_NAME = "DateView";
    CalendarComponent calendar;

    @PostConstruct
    void init() {

        setSizeFull();

        VerticalLayout formWrapper = new VerticalLayout();
        formWrapper.setWidth(1150, Unit.PIXELS);
        formWrapper.setHeight(650, Unit.PIXELS);
        formWrapper.setSpacing(true);
        formWrapper.setMargin(false);
        formWrapper.addStyleName("solid-border");
        addComponent(formWrapper);
        setComponentAlignment(formWrapper, Alignment.MIDDLE_CENTER);

        HorizontalLayout topNavBar = createTopNavBar();
        topNavBar.setMargin(false);
        topNavBar.setSpacing(false);
        formWrapper.addComponent(topNavBar);
        //centeringLayout.setExpandRatio(topNavBar, 1);


        VerticalLayout content = buildContent();
        content.setMargin(false);
        content.setHeight("85%");
        content.setWidth("85%");


        calendar = new CalendarComponent((AppUI) UI.getCurrent());
        calendar.setSizeFull();
        calendar.setHeight("90%");

        formWrapper.addComponent(calendar);
        //centeringLayout.addComponent(new Label("sadsdd"));
        formWrapper.addComponent(createBottomNav());
        //formWrapper.setComponentAlignment(content, Alignment.MIDDLE_CENTER);
        //formWrapper.setComponentAlignment(button,Alignment.BOTTOM_RIGHT);
        formWrapper.setComponentAlignment(calendar, Alignment.MIDDLE_CENTER);
        formWrapper.setExpandRatio(calendar, 1);

    }


    private VerticalLayout buildContent() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        verticalLayout.addComponent(new CalendarComponent());
        return verticalLayout;
    }

    private HorizontalLayout createTopNavBar() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setWidth("100%");
        horizontalLayout.setHeight(30, Unit.PIXELS);

        CssLayout generalBar = new CssLayout();
        generalBar.setWidth("100%");
        generalBar.setHeight(30, Unit.PIXELS);
        Label generalHeader = new Label("Step 1: General");
        generalHeader.setSizeUndefined();
        generalBar.addComponent(generalHeader);
        generalBar.setStyleName("nav-top-passed");
        generalBar.addLayoutClickListener((LayoutEvents.LayoutClickListener) layoutClickEvent -> UI.getCurrent().getNavigator().navigateTo("GeneralView"));

        CssLayout dateBar = new CssLayout();
        dateBar.setWidth("100%");
        dateBar.setHeight(30, Unit.PIXELS);
        Label dateHeader = new Label("Step 2: Set date(s)");
        dateHeader.setSizeUndefined();
        dateBar.addComponent(dateHeader);
        dateBar.setStyleName("nav-top-active");

        CssLayout timeLocationBar = new CssLayout();
        timeLocationBar.setWidth("100%");
        timeLocationBar.setHeight(30, Unit.PIXELS);
        Label timeLocationHeader = new Label("Step 3: Set time/location");
        timeLocationHeader.setSizeUndefined();
        timeLocationBar.addComponent(timeLocationHeader);
        timeLocationBar.setStyleName("nav-top-inactive");

        CssLayout categoriesBar = new CssLayout();
        categoriesBar.setWidth("100%");
        categoriesBar.setHeight(30, Unit.PIXELS);
        Label categoriesHeader = new Label("Step 4: Set categories");
        categoriesHeader.setSizeUndefined();
        categoriesBar.addComponent(categoriesHeader);
        categoriesBar.setStyleName("nav-top-inactive");

        horizontalLayout.addComponent(generalBar);
        horizontalLayout.addComponent(dateBar);
        horizontalLayout.addComponent(timeLocationBar);
        horizontalLayout.addComponent(categoriesBar);
        horizontalLayout.setSpacing(false);


        //horizontalLayout.setExpandRatio(generalBar, 1);
        return horizontalLayout;
    }

    public CssLayout createBottomNav() {
        CssLayout nav = new CssLayout();
        nav.setWidth("100%");
        //nav.setSpacing(false);
        //nav.setMargin(false);

        LayoutEvents.LayoutClickListener listener = (LayoutEvents.LayoutClickListener) layoutClickEvent -> {
            if (calendar.eventListisEmpty()) {
                Notification.show("No date selected",
                        "Please select a date before you continue",
                        Notification.Type.WARNING_MESSAGE);
            } else {
                UI.getCurrent().getNavigator().navigateTo("TimeLocationView");
            }
        };

        CustomButton nextButton = new CustomButton(VaadinIcons.ARROW_CIRCLE_RIGHT_O.getHtml() + " " + "NEXT", listener);
        nextButton.setWidth(167, Unit.PIXELS);
        nextButton.setHeight(40, Unit.PIXELS);
        nextButton.addStyleNames("next-button", "float-right");


        listener = (LayoutEvents.LayoutClickListener) layoutClickEvent -> {
            calendar.clearEvents(false);
        };

        CustomButton clearButton = new CustomButton(VaadinIcons.ERASER.getHtml() + " " + "CLEAR", listener);
        clearButton.setWidth(167, Unit.PIXELS);
        clearButton.setHeight(40, Unit.PIXELS);
        clearButton.addStyleNames("clear-button", "float-right");

        listener = (LayoutEvents.LayoutClickListener) layoutClickEvent -> {
            UI.getCurrent().getNavigator().navigateTo("GeneralView");
        };

        CustomButton backButton = new CustomButton(VaadinIcons.ARROW_CIRCLE_LEFT_O.getHtml() + " " + "BACK", listener);
        backButton.setWidth(167, Unit.PIXELS);
        backButton.setHeight(40, Unit.PIXELS);
        backButton.addStyleNames("back-button", "float-left");


        nav.addComponent(backButton);
        nav.addComponent(nextButton);
        nav.addComponent(clearButton);

        //nav.addComponent(b);
        //nav.setComponentAlignment(backButton, Alignment.MIDDLE_LEFT);
        //nav.setComponentAlignment(nextButton, Alignment.MIDDLE_RIGHT);
        // nav.setComponentAlignment(b, Alignment.MIDDLE_RIGHT);
        return nav;

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        calendar.clearEvents(true);


        AppUI app = (AppUI) UI.getCurrent();
        List<DateWrapper> days = app.getGlobalScheduleWrapper().getDays();
        for (int i=0; i<days.size();i++) {
            calendar.addEvent(days.get(i), app.getGlobalScheduleWrapper().getTitle());
        }


    }
}

