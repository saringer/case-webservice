package de.agdb.views.scheduler.create_schedule;

import com.vaadin.navigator.View;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

public class SetTimeLocationView extends VerticalLayout implements View {
    public static final String VIEW_NAME = "TimeLocationView";

    public SetTimeLocationView() {

        setSizeFull();
        VerticalLayout formWrapper = new VerticalLayout();
        formWrapper.setWidth("80%");
        formWrapper.setHeight("80%");
        addComponent(formWrapper);
        setComponentAlignment(formWrapper, Alignment.MIDDLE_CENTER);



        FormLayout content = new FormLayout();
        content.setHeight("80%");
        content.setWidth("90%");
        content.addStyleNames("overflow-auto");

        content.addComponent(buildContent("sdsd"));
        content.addComponent( buildContent("String"));




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

    private FormLayout buildContent(String header) {
        FormLayout formLayout = new FormLayout();
        formLayout.setWidth("100%");

        CssLayout wrapperLayout = new CssLayout();
        wrapperLayout.setWidth("100%");


        CssLayout itemLayout = new CssLayout();
        itemLayout.setWidth("100%");


        itemLayout.addComponent(buildItem());
        itemLayout.addComponent(buildItem());

        itemLayout.addComponent(buildItem());
        itemLayout.addComponent(buildItem());

        itemLayout.addComponent(buildItem());
        itemLayout.addComponent(buildItem());

        itemLayout.addComponent(buildItem());
        itemLayout.addComponent(buildItem());

        CssLayout buttonItem = new CssLayout();
        Button plusButton = new Button("Plus");
        plusButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                        itemLayout.addComponent(buildItem());

            }
        });
        buttonItem.addComponent(plusButton);
        buttonItem.setStyleName("item-box");
        buttonItem.setHeight(52, Unit.PIXELS);
        buttonItem.setWidth("33%");


        wrapperLayout.addComponent(itemLayout);
        wrapperLayout.addComponent(buttonItem);

        formLayout.addComponent(new Label("Section Monday 232.21.2"));
        formLayout.addComponent(wrapperLayout);




        return formLayout;
    }

    private CssLayout buildItem() {
        CssLayout cssLayout = new CssLayout();
        cssLayout.addComponent(new Label("Time"));
        cssLayout.setStyleName("item-box");
        cssLayout.setHeight(52, Unit.PIXELS);
        cssLayout.setWidth("33%");
        return cssLayout;
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
        dateBar.setStyleName("nav-top-inactive");

        CssLayout timeLocationBar = new CssLayout();
        timeLocationBar.setWidth("100%");
        timeLocationBar.setHeight(30, Unit.PIXELS);
        Label timeLocationHeader = new Label("Step 3: Set time/location");
        timeLocationHeader.setSizeUndefined();
        timeLocationBar.addComponent(timeLocationHeader);
        timeLocationBar.setStyleName("nav-top-active");

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
