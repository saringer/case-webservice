package de.agdb.views.scheduler.create_schedule;

import com.vaadin.event.LayoutEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import de.agdb.AppUI;
import de.agdb.views.scheduler.CustomButton;
import de.agdb.views.scheduler.modal_windows.TimeLocationWindow;
import de.agdb.backend.entities.schedule_wrapper_objects.DayWrapper;
import de.agdb.backend.entities.schedule_wrapper_objects.ScheduleWrapper;
import de.agdb.backend.entities.schedule_wrapper_objects.TimeLocationWrapper;
import org.vaadin.addons.locationtextfield.GeocodedLocation;
import org.vaadin.addons.locationtextfield.LocationTextField;

import java.time.format.DateTimeFormatter;
import java.util.List;

@UIScope
@SpringView(name = SetTimeLocationView.VIEW_NAME)
public class SetTimeLocationView extends VerticalLayout implements View {
    public static final String VIEW_NAME = "TimeLocationView";
    private FormLayout content;
    private NativeSelect startTime = new NativeSelect();
    private NativeSelect endTime = new NativeSelect();
    LocationTextField<GeocodedLocation> ltf;

    public SetTimeLocationView() {

        setSizeFull();
        VerticalLayout formWrapper = new VerticalLayout();
        formWrapper.setWidth(1150, Unit.PIXELS);
        formWrapper.setHeight(650, Unit.PIXELS);
        addComponent(formWrapper);
        setComponentAlignment(formWrapper, Alignment.MIDDLE_CENTER);


        content = new FormLayout();
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

    private FormLayout buildContent(DayWrapper day) {

        FormLayout formLayout = new FormLayout();
        formLayout.setWidth("100%");


        CssLayout itemLayout = new CssLayout();
        itemLayout.setWidth("100%");


        CssLayout plusButtonLayout = new CssLayout();
        Button plusButton = new Button();
        plusButton.setSizeUndefined();
        plusButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        plusButton.setIcon(VaadinIcons.PLUS);
        plusButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Window setTimeLocationWindow = new TimeLocationWindow(day, itemLayout, plusButtonLayout);
                setTimeLocationWindow.setWidth(400, Unit.PIXELS);
                //setTimeLocationWindow.setHeight(800, Unit.PIXELS);
                UI.getCurrent().addWindow(setTimeLocationWindow);
            }
        });
        plusButtonLayout.addComponent(plusButton);
        plusButtonLayout.addStyleName("add-button");
        plusButtonLayout.setHeight(52, Unit.PIXELS);
        plusButtonLayout.setWidth("24%");

        /* init already selected times & locations */
        for (int i = 0; i < day.getTimeAndLocationList().size(); i++) {
            TimeLocationWrapper object = day.getTimeAndLocationList().get(i);
            itemLayout.addComponent(buildItem(object.getFormattedStartTime(), object.getFormattedEndTime(), object.getStreet(), object.getStreetNumber(), itemLayout, object, day));
        }

        CssLayout wrapperLayout = new CssLayout();
        wrapperLayout.setWidth("100%");
        itemLayout.addComponent(plusButtonLayout);
        wrapperLayout.addComponent(itemLayout);

        //wrapperLayout.addComponent(plusButtonLayout);

        Label header = new Label(day.getDay().format(DateTimeFormatter.ofPattern("EEEE, dd.MM.yyyy"))
        );
        header.addStyleNames("h3", "colored");
        formLayout.addComponent(header);
        formLayout.addComponent(wrapperLayout);


        return formLayout;
    }

    private CssLayout buildItem(String startTime, String endTime, String street, String streetNumber, CssLayout parentLayout, TimeLocationWrapper content, DayWrapper parentWrapper) {
       /* CssLayout cssLayout = new CssLayout();
        Label label = new Label(startTime + " - " + endTime + "<br>" + street + " " + streetNumber);
        label.setContentMode(ContentMode.HTML);
        label.setWidth("100%");
        cssLayout.addComponent(label);
        cssLayout.setStyleName("item-box");
        cssLayout.setHeight(52, Unit.PIXELS);
        cssLayout.setWidth("24%");

        CssLayout customDeleteButton = new CssLayout();
        customDeleteButton.setWidth(20, Unit.PIXELS);
        customDeleteButton.setHeight(20, Unit.PIXELS);
        Label test = new Label(VaadinIcons.CLOSE_SMALL.getHtml());
        test.setSizeUndefined();
        test.setContentMode(ContentMode.HTML);
        customDeleteButton.addComponent(test);
        customDeleteButton.addStyleNames("topcorner-delete-button");
        customDeleteButton.addLayoutClickListener((LayoutEvents.LayoutClickListener) layoutClickEvent -> {
            parentLayout.removeComponent(cssLayout);
            parentWrapper.removeTimeLocation(content);

        });*/


        CssLayout cssLayout = new CssLayout();
        Label label = new Label(startTime + " - " + endTime + "<br>" + street + " " + streetNumber);
        label.setContentMode(ContentMode.HTML);
        cssLayout.addComponent(label);
        cssLayout.setStyleName("item-box");
        cssLayout.setHeight(52, Unit.PIXELS);
        cssLayout.setWidth("24%");


        CssLayout customDeleteButton = new CssLayout();
        customDeleteButton.setHeight(20, Unit.PIXELS);
        customDeleteButton.setWidth(20, Unit.PIXELS);
        Label test = new Label(VaadinIcons.CLOSE_SMALL.getHtml());
        test.setSizeUndefined();
        test.setContentMode(ContentMode.HTML);
        customDeleteButton.addComponent(test);
        customDeleteButton.addStyleNames("topcorner-delete-button");
        customDeleteButton.addLayoutClickListener((LayoutEvents.LayoutClickListener) layoutClickEvent -> {
            parentLayout.removeComponent(cssLayout);
            parentWrapper.removeTimeLocation(content);

        });



        cssLayout.addComponent(customDeleteButton);
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
        generalBar.setStyleName("nav-top-passed");
        generalBar.addLayoutClickListener((LayoutEvents.LayoutClickListener) layoutClickEvent -> UI.getCurrent().getNavigator().navigateTo("GeneralView"));


        CssLayout dateBar = new CssLayout();
        dateBar.setWidth("100%");
        dateBar.setHeight(30, Unit.PIXELS);
        Label dateHeader = new Label("Step 2: Set date(s)");
        dateHeader.setSizeUndefined();
        dateBar.addComponent(dateHeader);
        dateBar.setStyleName("nav-top-passed");
        dateBar.addLayoutClickListener((LayoutEvents.LayoutClickListener) layoutClickEvent -> UI.getCurrent().getNavigator().navigateTo("DateView"));

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

        LayoutEvents.LayoutClickListener listener = (LayoutEvents.LayoutClickListener) layoutClickEvent -> {
            AppUI app = (AppUI) UI.getCurrent();
            List<DayWrapper> days = app.getGlobalScheduleWrapper().getDays();

            Boolean flag = false;
            for (int i = 0; i < days.size(); i++
                    ) {
                if (days.get(i).getTimeAndLocationList().isEmpty()) {
                    Notification.show("Missing Time/Location", "Please set time and location before continuing", Notification.Type.WARNING_MESSAGE);
                    flag = false;
                    break;
                }
                flag = true;
            }
            if (flag) {
                UI.getCurrent().getNavigator().navigateTo("SetCategoriesView");
            }

        };
        CustomButton button = new CustomButton(VaadinIcons.ARROW_CIRCLE_RIGHT_O.getHtml() + " " + "Next", listener);
        button.setWidth(167, Unit.PIXELS);
        button.setHeight(40, Unit.PIXELS);
        button.addStyleName("next-button");


        listener = (LayoutEvents.LayoutClickListener) layoutClickEvent -> {
            UI.getCurrent().getNavigator().navigateTo("DateView");
        };

        CustomButton backButton = new CustomButton(VaadinIcons.ARROW_CIRCLE_LEFT_O.getHtml() + " " + "BACK", listener);
        backButton.setWidth(167, Unit.PIXELS);
        backButton.setHeight(40, Unit.PIXELS);
        backButton.addStyleName("back-button");

        nav.addComponents(backButton, button);
        nav.setComponentAlignment(button, Alignment.MIDDLE_RIGHT);
        nav.setComponentAlignment(backButton, Alignment.MIDDLE_LEFT);
        return nav;

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        initContent();

    }

    private void initContent() {
        AppUI app = (AppUI) UI.getCurrent();
        ScheduleWrapper globalScheduleWrapper = app.getGlobalScheduleWrapper();
        content.removeAllComponents();

        List<DayWrapper> days = globalScheduleWrapper.getDays();
        for (int i = 0; i < days.size(); i++) {
            content.addComponent(buildContent(days.get(i)));
        }


    }


}
