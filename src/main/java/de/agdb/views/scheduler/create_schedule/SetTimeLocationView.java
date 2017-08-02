package de.agdb.views.scheduler.create_schedule;

import com.vaadin.data.HasValue;
import com.vaadin.event.LayoutEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import de.agdb.AppUI;
import de.agdb.views.scheduler.create_schedule.schedule_wrapper_objects.DayWrapper;
import de.agdb.views.scheduler.create_schedule.schedule_wrapper_objects.GlobalWrapper;
import de.agdb.views.scheduler.create_schedule.schedule_wrapper_objects.TimeLocationWrapper;
import org.vaadin.addons.locationtextfield.GeocodedLocation;
import org.vaadin.addons.locationtextfield.GoogleGeocoder;
import org.vaadin.addons.locationtextfield.LocationTextField;
import org.vaadin.addons.locationtextfield.OpenStreetMapGeocoder;

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
        formWrapper.setWidth("80%");
        formWrapper.setHeight("80%");
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

        CssLayout wrapperLayout = new CssLayout();
        wrapperLayout.setWidth("100%");


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
                Window setTimeLocationWindow = new Window();
                setTimeLocationWindow.setModal(true);
                // setTimeLocationWindow.center();
                setTimeLocationWindow.setResizable(false);
                setTimeLocationWindow.setClosable(false);
                //  setTimeLocationWindow.setDraggable(false);
                //setTimeLocationWindow.setWidth(400, Unit.PIXELS);
                //setTimeLocationWindow.setHeight(800, Unit.PIXELS);
                //setTimeLocationWindow.setSizeUndefined();
                setTimeLocationWindow.setWidth(400, Unit.PIXELS);
                //  setTimeLocationWindow.setHeight(800, Unit.PIXELS);
                setTimeLocationWindow.setCaption(day.getDay().toString());

                VerticalLayout wrapperLayout = new VerticalLayout();
                wrapperLayout.setSpacing(false);
                wrapperLayout.setMargin(true);
                wrapperLayout.setSizeUndefined();
                wrapperLayout.setWidth(400, Unit.PIXELS);

                /* SET UP TIMEPICKER
                */

                VerticalLayout setTimeLayout = new VerticalLayout();
                setTimeLayout.setSpacing(false);
                setTimeLayout.setMargin(false);

                VerticalLayout headerLayout = new VerticalLayout();
                headerLayout.setMargin(false);
                headerLayout.setSpacing(false);
                headerLayout.setWidth("100%");
                headerLayout.setHeight(30, Unit.PIXELS);
                headerLayout.addComponent(new Label("Set time"));
                headerLayout.addStyleNames("modal-window-header");
                headerLayout.addStyleName("solid-border");
                HorizontalLayout selectLayout = new HorizontalLayout();
                selectLayout.setWidth("100%");
                selectLayout.setHeight(60, Unit.PIXELS);
                selectLayout.setMargin(false);
                selectLayout.setSpacing(false);
                Label labelFrom = new Label("from");
                labelFrom.setSizeUndefined();
                selectLayout.addComponent(labelFrom);
                HorizontalLayout timePickerStart = buildTimePicker(startTime);
                timePickerStart.setWidth("100%");
                selectLayout.addComponent(timePickerStart);
                Label labelTo = new Label("to");
                labelTo.setSizeUndefined();
                selectLayout.addComponent(labelTo);
                HorizontalLayout timePickerEnd = buildTimePicker(endTime);
                timePickerEnd.setSizeUndefined();
                selectLayout.addComponent(timePickerEnd);
                selectLayout.addStyleNames("modal-window-content");
                selectLayout.addStyleNames("solid-border");
                selectLayout.setComponentAlignment(labelFrom, Alignment.MIDDLE_LEFT);
                selectLayout.setComponentAlignment(timePickerStart, Alignment.MIDDLE_LEFT);
                selectLayout.setComponentAlignment(labelTo, Alignment.MIDDLE_LEFT);
                selectLayout.setComponentAlignment(timePickerEnd, Alignment.MIDDLE_LEFT);

                setTimeLayout.addComponent(headerLayout);
                setTimeLayout.addComponent(selectLayout);


                /*
                SET UP LOCATIONPICKER
                 */

                VerticalLayout setLocationLayout = new VerticalLayout();
                setLocationLayout.setSpacing(false);
                setLocationLayout.setMargin(false);

                headerLayout = new VerticalLayout();
                headerLayout.setMargin(false);
                headerLayout.setSpacing(false);
                headerLayout.setWidth("100%");
                headerLayout.setHeight(30, Unit.PIXELS);
                headerLayout.addComponent(new Label("Set location"));
                headerLayout.addStyleNames("modal-window-header");
                headerLayout.addStyleName("solid-border");


                setLocationLayout.addComponent(headerLayout);
                setLocationLayout.addComponent(buildLocationPicker());


                /*
                    SET UP BUTTONLAYOUT

                 */

                HorizontalLayout buttonLayout = new HorizontalLayout();
                buttonLayout.setWidth("100%");
                Button cancelButton = new Button("CANCEL");

                cancelButton.addStyleName(ValoTheme.BUTTON_DANGER);
                cancelButton.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent clickEvent) {
                        setTimeLocationWindow.close();
                    }
                });
                Button okayButton = new Button("OKAY");
                okayButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
                okayButton.addClickListener((Button.ClickListener) clickEvent -> {
                    String start = (String) startTime.getValue();
                    String end = (String) endTime.getValue();
                    String address = ltf.getValue().getOriginalAddress();
                    TimeLocationWrapper timeLocationWrapper = new TimeLocationWrapper(start, end, address);
                    itemLayout.addComponent(buildItem(start, end, address, itemLayout, timeLocationWrapper, day));
                    day.addTimeLocation(timeLocationWrapper);
                    setTimeLocationWindow.close();
                });

                buttonLayout.addComponents(cancelButton, okayButton);
                buttonLayout.setComponentAlignment(cancelButton, Alignment.MIDDLE_LEFT);
                buttonLayout.setComponentAlignment(okayButton, Alignment.MIDDLE_RIGHT);
                buttonLayout.addStyleNames("modal-window-margin");

                /*
                ADD COMPONENTS TO WRAPPERLAYOUT
                 */

                wrapperLayout.addComponent(setTimeLayout);
                wrapperLayout.addComponent(setLocationLayout);
                wrapperLayout.addComponent(buttonLayout);

                setTimeLocationWindow.setContent(wrapperLayout);

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
            String start = object.getStartTime();
            String end = object.getEndTime();
            String address = object.getLocation();
            itemLayout.addComponent(buildItem(start, end, address, itemLayout, object, day));
        }


        wrapperLayout.addComponent(itemLayout);
        wrapperLayout.addComponent(plusButtonLayout);

        Label header = new Label(day.getDay().toString());
        header.addStyleNames("h3", "colored");
        formLayout.addComponent(header);
        formLayout.addComponent(wrapperLayout);


        return formLayout;
    }

    private CssLayout buildItem(String startTime, String endTime, String location, CssLayout parentLayout, TimeLocationWrapper content, DayWrapper parentWrapper) {
        CssLayout cssLayout = new CssLayout();
        Label label = new Label(startTime + " - " + endTime + "<br>" + location);
        label.setContentMode(ContentMode.HTML);
        cssLayout.addComponent(label);
        cssLayout.setStyleName("item-box");
        cssLayout.setHeight(52, Unit.PIXELS);
        cssLayout.setWidth("24%");

        CssLayout customDeleteButton = new CssLayout();
        customDeleteButton.setSizeUndefined();
        Label test = new Label(VaadinIcons.CLOSE_SMALL.getHtml());
        test.setSizeUndefined();
        test.setContentMode(ContentMode.HTML);
        customDeleteButton.addComponent(test);
        customDeleteButton.addStyleNames("topcorner-delete-button", "solid-border");
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
        button.setWidth(167, Unit.PIXELS);
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                UI.getCurrent().getNavigator().navigateTo("SetCategoriesView");
            }
        });
        button.addStyleName(ValoTheme.BUTTON_FRIENDLY);

        Button backButton = new Button("BACK");
        backButton.setWidth(167, Unit.PIXELS);
        backButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                UI.getCurrent().getNavigator().navigateTo("DateView");
            }
        });
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
        GlobalWrapper globalScheduleWrapper = app.getGlobalScheduleWrapper();
        content.removeAllComponents();

        List<DayWrapper> days = globalScheduleWrapper.getDays();
        for (int i = 0; i < days.size(); i++) {
            content.addComponent(buildContent(days.get(i)));
        }


    }

    private HorizontalLayout buildTimePicker(NativeSelect selectHour) {
        HorizontalLayout wrapperLayout = new HorizontalLayout();
        wrapperLayout.setSpacing(false);
        wrapperLayout.setMargin(false);


        selectHour.setItems("10:00", "12:00");

        wrapperLayout.addComponent(selectHour);

        return wrapperLayout;
    }

    private VerticalLayout buildLocationPicker() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        GoogleGeocoder geocoder2 = GoogleGeocoder.getInstance();
        OpenStreetMapGeocoder geocoder = OpenStreetMapGeocoder.getInstance();
        geocoder.setLimit(5);
        ltf = new LocationTextField<GeocodedLocation>(geocoder2);
        ltf.setWidth("100%");
        ltf.setMinimumQueryCharacters(5);
        ltf.addStyleName("blue-border");


        //ltf.setAutoSelectionEnabled(false);


        GoogleMap googleMap = new GoogleMap("AIzaSyDmcnkMKoB-xqSaZ6VdqT-k-G8bnHbO-wQ", null, "english");
        googleMap.setSizeFull();
        googleMap.setHeight(300, Unit.PIXELS);
        googleMap.setMinZoom(14);
        googleMap.setMaxZoom(16);
        googleMap.addStyleNames("blue-border");


        ltf.addLocationValueChangeListener(new HasValue.ValueChangeListener<GeocodedLocation>() {
            @Override
            public void valueChange(HasValue.ValueChangeEvent<GeocodedLocation> valueChangeEvent) {
                GeocodedLocation loc = (GeocodedLocation) valueChangeEvent.getValue();
                if (loc != null) {
                    LatLon latLong = new LatLon(loc.getLat(), loc.getLon());
                    googleMap.removeAllComponents();
                    googleMap.setCenter(latLong);
                    googleMap.addMarker("", latLong, false, null);

                } else {

                }
            }
        });


        verticalLayout.addComponent(ltf);
        verticalLayout.addComponent(googleMap);
        verticalLayout.addStyleNames("modal-window-content", "solid-border", "padding-text");

        return verticalLayout;


    }




}
