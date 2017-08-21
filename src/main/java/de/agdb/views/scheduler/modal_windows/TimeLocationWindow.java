package de.agdb.views.scheduler.modal_windows;

import com.vaadin.data.HasValue;
import com.vaadin.event.LayoutEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.UserError;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.shared.ui.datefield.Resolution;
import com.vaadin.v7.ui.Field;
import de.agdb.backend.entities.schedule_wrapper_objects.DayWrapper;
import de.agdb.backend.entities.schedule_wrapper_objects.TimeLocationWrapper;
import de.agdb.views.scheduler.CustomButton;
import de.agdb.views.scheduler.create_schedule.timefield_component.AbstractTimeField;
import de.agdb.views.scheduler.create_schedule.timefield_component.TimeTextField;
import org.vaadin.addons.locationtextfield.GeocodedLocation;
import org.vaadin.addons.locationtextfield.GoogleGeocoder;
import org.vaadin.addons.locationtextfield.LocationTextField;
import org.vaadin.addons.locationtextfield.OpenStreetMapGeocoder;

import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeLocationWindow extends Window {

    private NativeSelect startTime = new NativeSelect();
    private NativeSelect endTime = new NativeSelect();
    LocationTextField<GeocodedLocation> ltf;

    public TimeLocationWindow(DayWrapper day, CssLayout itemLayout, CssLayout plusButtonLayout) {


        setModal(true);
        setResizable(false);
        setClosable(false);
        setCaption(day.getDay().format(DateTimeFormatter.ofPattern("EEEE, dd.MM.yyyy")));

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
        CssLayout selectLayout = new CssLayout();
        selectLayout.setWidth("100%");
        selectLayout.setHeight(60, Unit.PIXELS);
        Label labelFrom = new Label("from");
        labelFrom.addStyleName("label-from");
        labelFrom.setSizeUndefined();
        selectLayout.addComponent(labelFrom);

        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(new Date());
        int hour = rightNow.get(Calendar.HOUR_OF_DAY);
        int min = rightNow.get(Calendar.MINUTE);

        AbstractTimeField<?> startTime = new TimeTextField();
        startTime.setLocale(Locale.getDefault());
        startTime.setWidth("60px");
        startTime.setImmediate(true);
        startTime.setHours(hour);
        startTime.setMinutes(min);
        startTime.addStyleName("endtime");
        selectLayout.addComponent(startTime);

        Label labelTo = new Label("to");
        labelTo.setSizeUndefined();
        labelTo.addStyleNames("label-to");
        selectLayout.addComponent(labelTo);

        rightNow = Calendar.getInstance();
        rightNow.setTime(new Date());
        rightNow.add(Calendar.HOUR_OF_DAY, 2);
        hour = rightNow.get(Calendar.HOUR_OF_DAY);
        min = rightNow.get(Calendar.MINUTE);

        AbstractTimeField<?> endTime = new TimeTextField();
        endTime.setLocale(Locale.getDefault());
        endTime.setWidth("60px");
        endTime.setImmediate(true);
        endTime.setHours(hour);
        endTime.setMinutes(min);
        endTime.addStyleName("endtime");
        selectLayout.addComponent(endTime);

        System.out.println(endTime.getResolution().getCalendarField());


        selectLayout.addStyleNames("modal-window-content");
        selectLayout.addStyleNames("solid-border");

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

        LayoutEvents.LayoutClickListener listener = (LayoutEvents.LayoutClickListener) layoutClickEvent -> close();
        CustomButton cancelButton = new CustomButton(VaadinIcons.CLOSE.getHtml(), listener);
        cancelButton.addStyleName("cancel-button");
        cancelButton.setHeight(40, Unit.PIXELS);
        cancelButton.setWidth(150, Unit.PIXELS);


        listener = (LayoutEvents.LayoutClickListener) layoutClickEvent -> {
            startTime.valueChange(new Field.ValueChangeEvent(startTime));
            endTime.valueChange(new Field.ValueChangeEvent(endTime));

           // changeListener.valueChange(weightField.new ValueChangeEvent(weightField));

            startTime.validate();
            endTime.validate();
            if (startTime.getComponentError()==null && endTime.getComponentError()==null && startTime.isValid()) {

                if (ltf.getValue() != null) {
                    String address = ltf.getValue().getGeocodedAddress();
                    TimeLocationWrapper timeLocationWrapper = new TimeLocationWrapper(startTime.getFormattedValue(), endTime.getFormattedValue(), startTime.getHours(), startTime.getMinutes(), endTime.getHours(), endTime.getMinutes(), address);
                    /*
                            Remove the plus button and add it again at the end of the item layout
                     */
                    itemLayout.removeComponent(plusButtonLayout);
                    itemLayout.addComponent(buildItem(startTime.getFormattedValue(), endTime.getFormattedValue(), address, itemLayout, timeLocationWrapper, day));
                    itemLayout.addComponent(plusButtonLayout);
                    day.addTimeLocation(timeLocationWrapper);
                    close();
                }
                else {
                    ltf.setComponentError(new UserError("Please pick a location from the proposed list"));
                }

            }
        };
        CustomButton okayButton = new CustomButton(VaadinIcons.CHECK.getHtml(), listener);
        okayButton.addStyleName("next-button");
        okayButton.setHeight(40, Unit.PIXELS);
        okayButton.setWidth(150, Unit.PIXELS);


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

        setContent(wrapperLayout);

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


    private CssLayout buildItem(String startTime, String endTime, String location, CssLayout parentLayout, TimeLocationWrapper content, DayWrapper parentWrapper) {
        CssLayout cssLayout = new CssLayout();
        Label label = new Label(startTime + " - " + endTime + "<br>" + location);
        label.setContentMode(ContentMode.HTML);
        cssLayout.addComponent(label);
        cssLayout.setStyleName("item-box");
        cssLayout.setHeight(52, Unit.PIXELS);


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
}
