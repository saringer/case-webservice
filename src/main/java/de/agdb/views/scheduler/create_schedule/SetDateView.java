package de.agdb.views.scheduler.create_schedule;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import de.agdb.views.scheduler.CalendarComponent;
import org.vaadin.addon.calendar.Calendar;

@UIScope
@SpringView(name = GeneralView.VIEW_NAME)
public class SetDateView extends VerticalLayout implements View{
    public static final String VIEW_NAME = "DateView";

    public SetDateView() {
        setSizeFull();

        VerticalLayout formWrapper = new VerticalLayout();
        formWrapper.setWidth("80%");
        formWrapper.setHeight("80%");
        formWrapper.setSpacing(false);
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


        Button button = new Button("NEXT");
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                UI.getCurrent().getNavigator().navigateTo("TimeLocationView");
            }
        });
        button.setWidth("15%");
        button.addStyleName(ValoTheme.BUTTON_FRIENDLY);


        CalendarComponent calendar = new CalendarComponent();

        formWrapper.addComponent(calendar);
        //centeringLayout.addComponent(new Label("sadsdd"));
        formWrapper.addComponent(button);
        //formWrapper.setComponentAlignment(content, Alignment.MIDDLE_CENTER);
        formWrapper.setComponentAlignment(button,Alignment.BOTTOM_RIGHT);
        formWrapper.setComponentAlignment(calendar, Alignment.MIDDLE_CENTER);
        formWrapper.setExpandRatio(calendar ,1);
    }

    private VerticalLayout buildContent() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        verticalLayout.addComponent(new CalendarComponent());
        return  verticalLayout;
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
        generalBar.setStyleName("nav-top-inactive");

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
}

