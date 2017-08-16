package de.agdb.views.scheduler.create_schedule.calendar_component;

import com.vaadin.ui.*;

import de.agdb.AppUI;

import de.agdb.backend.entities.schedule_wrapper_objects.DayWrapper;

import org.vaadin.addon.calendar.Calendar;
import org.vaadin.addon.calendar.item.BasicItemProvider;
import org.vaadin.addon.calendar.handler.BasicWeekClickHandler;
import org.vaadin.addon.calendar.ui.CalendarComponentEvents;

import java.text.DateFormatSymbols;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class CalendarComponent extends VerticalLayout {

    Button nextButton;
    Button prevButton;
    GregorianCalendar calendar;
    private Date currentMonthsFirstDate;
    Calendar<MeetingItem> calendarComponent;
    private final Label captionLabel = new Label("");
    private String calendarHeight = null;


    private String calendarWidth = null;
    private Integer firstHour;

    private Integer lastHour;

    private Integer firstDay;

    private Integer lastDay;
    private MeetingDataProvider eventProvider;
    private AppUI app;
    private boolean readonly = false;


    public CalendarComponent() {
        this.readonly = true;
        setSizeFull();
        //setHeight("500px");
        //setMargin(true);
        //setSpacing(true);
        initContent();
    }

    public CalendarComponent(AppUI app) {
        this.app = app;
        setSizeFull();
        //setHeight("500px");
        setMargin(true);
        setSpacing(true);
        initContent();

    }

    private void initContent() {
        initCalendar();
        initLayoutContent();
        nextMonth();
        previousMonth();

    }

    private void initLayoutContent() {
        initNavigationButtons();
        HorizontalLayout hl = new HorizontalLayout();
        hl.setWidth("100%");
        hl.setSpacing(true);
        hl.addComponent(prevButton);
        hl.addComponent(captionLabel);
        hl.addComponent(nextButton);
        hl.setComponentAlignment(prevButton, Alignment.MIDDLE_LEFT);
        hl.setComponentAlignment(captionLabel, Alignment.MIDDLE_CENTER);
        hl.setComponentAlignment(nextButton, Alignment.MIDDLE_RIGHT);
        addComponent(hl);
        addComponent(calendarComponent);
        setExpandRatio(hl, 0.1f);
        setExpandRatio(calendarComponent, 0.9f);
        //setRowExpandRatio(getRows() - 1, 1.0f);

    }

    private void initCalendar() {

        eventProvider = new MeetingDataProvider();

        calendarComponent = new Calendar(eventProvider);
        calendarComponent.setSizeFull();
        calendarComponent.setResponsive(true);

        if (calendarWidth != null || calendarHeight != null) {
            if (calendarHeight != null) {
                calendarComponent.setHeight(calendarHeight);
            }
            if (calendarWidth != null) {
                calendarComponent.setWidth(calendarWidth);
            }
        } else {
            calendarComponent.setSizeFull();
        }

        if (firstHour != null && lastHour != null) {
            calendarComponent.setFirstVisibleHourOfDay(firstHour);
            calendarComponent.setLastVisibleHourOfDay(lastHour);
        }

        if (firstDay != null && lastDay != null) {


        }

        Date today = getToday();
        calendar = new GregorianCalendar();
        calendar.setTime(today);


        // Calendar getStartDate (and getEndDate) has some strange logic which
        // returns Monday of the current internal time if no start date has been
        // set
        calendarComponent.setStartDate(calendarComponent.getStartDate());
        calendarComponent.setEndDate(calendarComponent.getEndDate());
        int rollAmount = calendar.get(GregorianCalendar.DAY_OF_MONTH) - 1;
        calendar.add(GregorianCalendar.DAY_OF_MONTH, -rollAmount);
        currentMonthsFirstDate = calendar.getTime();

        addCalendarEventListeners();
        updateCaptionLabel();

    }

    private Date getToday() {
        return new Date();
    }

    private void initNavigationButtons() {

        nextButton = new Button("Next", new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                nextMonth();
            }
        });

        prevButton = new Button("Prev", new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                previousMonth();
            }
        });
    }

    private void nextMonth() {
        rollMonth(1);
    }

    private void previousMonth() {
        rollMonth(-1);
    }

    private void rollMonth(int direction) {

        calendar.setTime(currentMonthsFirstDate);

        calendar.add(GregorianCalendar.MONTH, direction);
        //resetTime(false);
        currentMonthsFirstDate = calendar.getTime();
        //calendarComponent.setStartDate(currentMonthsFirstDate);
        calendarComponent.setStartDate(calendar.toZonedDateTime());

        updateCaptionLabel();

        calendar.add(GregorianCalendar.MONTH, 1);
        calendar.add(GregorianCalendar.DATE, -1);
        resetCalendarTime(true);
    }

    private void updateCaptionLabel() {
        DateFormatSymbols s = new DateFormatSymbols();
        String month = s.getShortMonths()[calendar
                .get(GregorianCalendar.MONTH)];
        captionLabel
                .setValue(month + " " + calendar.get(GregorianCalendar.YEAR));
    }

    private void resetCalendarTime(boolean resetEndTime) {
        resetTime(resetEndTime);
        if (resetEndTime) {
            calendarComponent.setEndDate(calendar.toZonedDateTime());
        } else {
            calendarComponent.setStartDate(calendar.toZonedDateTime());
            updateCaptionLabel();
        }
    }

    private void resetTime(boolean max) {
        if (max) {
            calendar.set(GregorianCalendar.HOUR_OF_DAY,
                    calendar.getMaximum(GregorianCalendar.HOUR_OF_DAY));
            calendar.set(GregorianCalendar.MINUTE,
                    calendar.getMaximum(GregorianCalendar.MINUTE));
            calendar.set(GregorianCalendar.SECOND,
                    calendar.getMaximum(GregorianCalendar.SECOND));
            calendar.set(GregorianCalendar.MILLISECOND,
                    calendar.getMaximum(GregorianCalendar.MILLISECOND));
        } else {

            calendar.set(GregorianCalendar.HOUR_OF_DAY, 0);
            calendar.set(GregorianCalendar.MINUTE, 0);
            calendar.set(GregorianCalendar.SECOND, 0);
            calendar.set(GregorianCalendar.MILLISECOND, 0);
        }
    }

    private void addCalendarEventListeners() {
        if (this.readonly == false) {
            calendarComponent.setHandler((BasicWeekClickHandler) null);
            calendarComponent.setHandler((CalendarComponentEvents.DateClickHandler) null);
            calendarComponent.setHandler(this::onCalendarClick);
            calendarComponent.setHandler(this::onCalendarRangeSelect);
        } else {
            calendarComponent.setHandler((BasicWeekClickHandler) null);
            calendarComponent.setHandler((CalendarComponentEvents.DateClickHandler) null);
            calendarComponent.setHandler(this::onCalendarClick);
            calendarComponent.setHandler((CalendarComponentEvents.RangeSelectHandler) null);
        }
    }

    private void onCalendarClick(CalendarComponentEvents.ItemClickEvent event) {

        MeetingItem item = (MeetingItem) event.getCalendarItem();

        final Meeting meeting = item.getMeeting();

        Notification.show(meeting.getName(), meeting.getDetails(), Notification.Type.HUMANIZED_MESSAGE);
    }

    private void onCalendarRangeSelect(CalendarComponentEvents.RangeSelectEvent event) {

        List<MeetingItem> meetingItemList = eventProvider.getItems(event.getStart(), event.getStart());
        if (meetingItemList.size() == 0) {
            app.getGlobalScheduleWrapper().addDay(new DayWrapper(event.getStart()));

            Meeting meeting = new Meeting();

            meeting.setStart(event.getStart());
            meeting.setEnd(event.getStart());
            meeting.setName("A Name");
            meeting.setDetails("Selected Day");

            MeetingItem item = new MeetingItem(meeting);
            eventProvider.addItem(item);

        } else {
            app.getGlobalScheduleWrapper().removeDay(event.getStart());
            eventProvider.removeItem(meetingItemList.get(0));

        }

    }

    public void clearEvents(boolean calendarOnly) {
        if (calendarOnly) {
            eventProvider.removeAllEvents();
        } else {
            eventProvider.removeAllEvents();
            app.getGlobalScheduleWrapper().removeAlldays();

        }

    }

    public boolean eventListisEmpty() {
        return eventProvider.isEmpty();
    }

    public void addEvent(ZonedDateTime startTime, String title) {
        Meeting meeting = new Meeting();

        meeting.setStart(startTime);
        meeting.setEnd(startTime);
        meeting.setName("A Name");
        meeting.setDetails(title);

        MeetingItem item = new MeetingItem(meeting);
        item.setStyleName("color2");
        eventProvider.addItem(item);
    }

    public MeetingDataProvider getEventProvider() {
        return this.eventProvider;
    }

    private final class MeetingDataProvider extends BasicItemProvider<MeetingItem> {

        void removeAllEvents() {
            this.itemList.clear();
            fireItemSetChanged();
        }

        boolean isEmpty() {
            if (this.itemList.size() > 0) {
                return false;
            } else {
                return true;
            }
        }


    }
}