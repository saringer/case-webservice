package de.agdb.views.scheduler.create_schedule;

import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

@UIScope
@SpringView(name = SetCategoriesView.VIEW_NAME)
public class SetCategoriesView extends VerticalLayout implements View{

    public static final String VIEW_NAME = "SetCategoriesView";

    public SetCategoriesView() {

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

       // content.addComponent(buildContent("sdsd"));
       // content.addComponent( buildContent("String"));




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

        CssLayout dateBar = new CssLayout();
        dateBar.setWidth("100%");
        dateBar.setHeight(30, Unit.PIXELS);
        Label dateHeader = new Label("Step 2: Set date(s)");
        dateHeader.setSizeUndefined();
        dateBar.addComponent(dateHeader);
        dateBar.setStyleName("nav-top-passed");

        CssLayout timeLocationBar = new CssLayout();
        timeLocationBar.setWidth("100%");
        timeLocationBar.setHeight(30, Unit.PIXELS);
        Label timeLocationHeader = new Label("Step 3: Set time/location");
        timeLocationHeader.setSizeUndefined();
        timeLocationBar.addComponent(timeLocationHeader);
        timeLocationBar.setStyleName("nav-top-passed");

        CssLayout categoriesBar = new CssLayout();
        categoriesBar.setWidth("100%");
        categoriesBar.setHeight(30, Unit.PIXELS);
        Label categoriesHeader = new Label("Step 4: Set categories");
        categoriesHeader.setSizeUndefined();
        categoriesBar.addComponent(categoriesHeader);
        categoriesBar.setStyleName("nav-top-active");

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
        Button createButton = new Button("CREATE SCHEDULE");
        createButton.setWidth(167, Unit.PIXELS);
        createButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                String callBackUrl = Page.getCurrent().getLocation().toString();
                if(callBackUrl.contains("#")) {
                    callBackUrl = callBackUrl.substring(0, callBackUrl.indexOf("#"));
                    UI.getCurrent().showNotification(callBackUrl);
                }

            }
        });
        createButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);

        Button backButton = new Button("BACK");
        backButton.setWidth(167, Unit.PIXELS);
        backButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                UI.getCurrent().getNavigator().navigateTo("TimeLocationView");
            }
        });
        backButton.addStyleName("back-button");




        nav.addComponent(backButton);
        nav.addComponent(createButton);
        //nav.addComponent(b);
        nav.setComponentAlignment(backButton, Alignment.MIDDLE_LEFT);
        nav.setComponentAlignment(createButton, Alignment.MIDDLE_RIGHT);
        // nav.setComponentAlignment(b, Alignment.MIDDLE_RIGHT);
        return nav;

    }
}
