package de.agdb.views.scheduler.create_schedule;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

@UIScope
@SpringView(name = SetGeneralView.VIEW_NAME)
public class SetGeneralView extends VerticalLayout implements View {
    public static final String VIEW_NAME = "GeneralView";

    public SetGeneralView() {
        setSizeFull();
        VerticalLayout formWrapper = new VerticalLayout();
        formWrapper.setWidth("80%");
        formWrapper.setHeight("80%");
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
        TextField scheduleTitle = new TextField();
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
        TextArea scheduleDescription = new TextArea();
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
        Button button = new Button("NEXT");
        button.setWidth("15%");
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                UI.getCurrent().getNavigator().navigateTo("DateView");
            }
        });
        button.addStyleName(ValoTheme.BUTTON_FRIENDLY);


        Button b = new Button("Clear");
        b.setWidth("15%");
        b.addStyleName(ValoTheme.BUTTON_FRIENDLY);



        nav.addComponent(button);
        //nav.addComponent(b);
        nav.setComponentAlignment(button, Alignment.MIDDLE_RIGHT);
       // nav.setComponentAlignment(b, Alignment.MIDDLE_RIGHT);
        return nav;

    }


}
