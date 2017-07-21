package de.agdb.views.scheduler.create_schedule;

import com.vaadin.client.ui.FontIcon;
import com.vaadin.client.ui.Icon;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import javax.swing.text.html.CSS;
import javax.xml.soap.Text;

@UIScope
@SpringView(name = GeneralView.VIEW_NAME)
public class GeneralView extends VerticalLayout implements View {
    public static final String VIEW_NAME = "Create Schedule";

    public GeneralView() {
        setSizeFull();
        VerticalLayout centeringLayout = new VerticalLayout();
        centeringLayout.setWidth("80%");
        centeringLayout.setHeight("80%");
        addComponent(centeringLayout);
        setComponentAlignment(centeringLayout, Alignment.MIDDLE_CENTER);

        HorizontalLayout topNavBar = createTopNavBar();
        centeringLayout.addComponent(topNavBar);
        //centeringLayout.setExpandRatio(topNavBar, 1);
        centeringLayout.addStyleName("solid-border");
        centeringLayout.setSpacing(false);
        centeringLayout.setMargin(false);

        VerticalLayout content = buildContent();
        content.setHeight("70%");
        content.setWidth("70%");


        Button button = new Button("NEXT");
        button.setWidth("15%");
        button.addStyleName(ValoTheme.BUTTON_FRIENDLY);

        centeringLayout.addComponent(content);
        //centeringLayout.addComponent(new Label("sadsdd"));
        centeringLayout.addComponent(button);
        centeringLayout.setComponentAlignment(content, Alignment.MIDDLE_CENTER);
        centeringLayout.setComponentAlignment(button,Alignment.BOTTOM_RIGHT);
        centeringLayout.setExpandRatio(content, 1);

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

    private VerticalLayout buildContent() {

        VerticalLayout containerLayout = new VerticalLayout();
        containerLayout.setSizeFull();

        TextField scheduleTitle = new TextField("sdsd");
        scheduleTitle.setCaption("Schedule title");
        scheduleTitle.setWidth("100%");

        TextArea scheduleDescription = new TextArea("sdsd");
        scheduleDescription.setCaption("Schedule description");
        scheduleDescription.setWidth("100%");

        containerLayout.addComponent(scheduleTitle);
        containerLayout.addComponent(scheduleDescription);
        return containerLayout;
    }


}
