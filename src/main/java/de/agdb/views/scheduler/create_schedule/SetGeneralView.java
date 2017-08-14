package de.agdb.views.scheduler.create_schedule;

import com.vaadin.event.LayoutEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.UserError;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import de.agdb.AppUI;
import de.agdb.views.scheduler.CustomButton;

@UIScope
@SpringView(name = SetGeneralView.VIEW_NAME)
public class SetGeneralView extends VerticalLayout implements View {
    public static final String VIEW_NAME = "GeneralView";
    private TextField scheduleTitle;
    private TextArea scheduleDescription;

    public SetGeneralView() {
        setSizeFull();
        VerticalLayout formWrapper = new VerticalLayout();
        formWrapper.setWidth(1150, Unit.PIXELS);
        formWrapper.setHeight(650, Unit.PIXELS);
        addComponent(formWrapper);
        setComponentAlignment(formWrapper, Alignment.MIDDLE_CENTER);


        FormLayout content = buildContent();
        content.setSpacing(true);
        content.setMargin(true);
        content.setHeight("80%");
        content.setWidth("70%");


        HorizontalLayout topNavBar = createTopNavBar();

        HorizontalLayout bottomNavBar = createBottomNav();

        formWrapper.addComponent(topNavBar);
        //centeringLayout.setExpandRatio(topNavBar, 1);
        formWrapper.addStyleName("solid-border");
        //formWrapper.addStyleName("overflow-auto");
        formWrapper.setSpacing(false);
        formWrapper.setMargin(false);
        formWrapper.addComponent(content);
        //centeringLayout.addComponent(new Label("sadsdd"));
        formWrapper.addComponent(bottomNavBar);
        formWrapper.setComponentAlignment(content, Alignment.MIDDLE_CENTER);
        //formWrapper.setComponentAlignment(button,Alignment.BOTTOM_RIGHT);
        formWrapper.setExpandRatio(content, 1);

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        AppUI app = (AppUI) UI.getCurrent();
        app.getGlobalScheduleWrapper();

        scheduleTitle.setComponentError(null);
        scheduleDescription.setComponentError(null);

        if (app.getGlobalScheduleWrapper().getTitle() == null) {
            scheduleTitle.setValue("");
        }
        if (app.getGlobalScheduleWrapper().getDescription() == null) {
            scheduleDescription.setValue("");
        }
        if (app.getGlobalScheduleWrapper().getTitle() != null) {
            scheduleTitle.setValue(app.getGlobalScheduleWrapper().getTitle());
        }
        if (app.getGlobalScheduleWrapper().getDescription() != null) {
            scheduleDescription.setValue(app.getGlobalScheduleWrapper().getDescription());
        }


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
        generalBar.setStyleName("nav-top-active");

        CssLayout dateBar = new CssLayout();
        dateBar.setWidth("100%");
        dateBar.setHeight(30, Unit.PIXELS);
        Label dateHeader = new Label("Step 2: Set date(s)");
        dateHeader.setSizeUndefined();
        dateBar.addComponent(dateHeader);
        dateBar.setStyleName("nav-top-inactive");

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

    private FormLayout buildContent() {

        Label section;

        FormLayout form = new FormLayout();

        section = new Label("Schedule title");
        section.addStyleName("h3");
        section.addStyleName("colored");
        scheduleTitle = new TextField();
        scheduleTitle.setWidth("100%");
        form.addComponent(section);
        form.addComponent(scheduleTitle);

        section = new Label("Recurrency");
        section.addStyleName("h3");
        section.addStyleName("colored");
        HorizontalLayout recurrencyOptions = new HorizontalLayout();

        NativeSelect<String> dropDownMenu = new NativeSelect();
        dropDownMenu.setEmptySelectionAllowed(false);
        dropDownMenu.setItems("weekly", "monthly");
        dropDownMenu.setSelectedItem("monthly");
        recurrencyOptions.addComponent(dropDownMenu);
        recurrencyOptions.addComponent(new Label("until"));
        recurrencyOptions.addComponent(new DateField());
        CheckBox activateRecurrency = new CheckBox("Make this a recurrying event", false);
        form.addComponent(section);
        form.addComponent(recurrencyOptions);
        form.addComponent(activateRecurrency);

        section = new Label("Description");
        section.addStyleName("h3");
        section.addStyleName("colored");
        scheduleDescription = new TextArea();
        scheduleDescription.setWidth("100%");
        form.addComponent(section);
        form.addComponent(scheduleDescription);


        return form;

    }

    public HorizontalLayout createBottomNav() {
        HorizontalLayout nav = new HorizontalLayout();
        nav.setWidth("100%");
        nav.setSpacing(false);
        nav.setMargin(false);

        LayoutEvents.LayoutClickListener listener = new LayoutEvents.LayoutClickListener() {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent layoutClickEvent) {
                if (scheduleTitle.isEmpty()) {
                    scheduleTitle.setComponentError(new UserError("Please enter a title foryour schedule"));
                }

                if (scheduleDescription.isEmpty()) {
                    scheduleDescription.setComponentError(new UserError("Please enter a description for your schedule"));
                } else {
                    AppUI app = (AppUI) UI.getCurrent();
                    app.getGlobalScheduleWrapper().setTitle(scheduleTitle.getValue());
                    app.getGlobalScheduleWrapper().setDescription(scheduleDescription.getValue());

                    app.getNavigator().navigateTo("DateView");
                }
            }
        };

        CustomButton button = new CustomButton(VaadinIcons.ARROW_CIRCLE_RIGHT_O.getHtml() + " " + "Next", listener);
        button.setWidth("15%");
        button.setHeight(40, Unit.PIXELS);
        button.setWidth(167, Unit.PIXELS);
        button.addStyleName("next-button");

        nav.addComponent(button);
        nav.setComponentAlignment(button, Alignment.MIDDLE_RIGHT);

        return nav;

    }


}
