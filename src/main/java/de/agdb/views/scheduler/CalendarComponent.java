package de.agdb.views.scheduler;

import com.vaadin.ui.*;

import com.vaadin.v7.ui.components.calendar.event.BasicEventProvider;
import de.agdb.views.scheduler.create_schedule.calendar_meetings.Meeting;
import de.agdb.views.scheduler.create_schedule.calendar_meetings.MeetingItem;

import org.vaadin.addon.calendar.Calendar;
import org.vaadin.addon.calendar.event.BasicItemProvider;
import org.vaadin.addon.calendar.handler.BasicDateClickHandler;
import org.vaadin.addon.calendar.ui.CalendarComponentEvents;

import java.text.DateFormatSymbols;
import java.util.Date;
import java.util.GregorianCalendar;

public class CalendarComponent extends GridLayout {

    Button nextButton;
    Button prevButton;
    GregorianCalendar calendar;
    private Date currentMonthsFirstDate;
    Calendar calendarComponent;
    private final Label captionLabel = new Label("");
    private String calendarHeight = null;


    private String calendarWidth = null;
    private Integer firstHour;

    private Integer lastHour;

    private Integer firstDay;

    private Integer lastDay;
    private MeetingDataProvider eventProvider;

    public CalendarComponent() {
        setSizeFull();
        setHeight("500px");
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
        setRowExpandRatio(getRows() - 1, 1.0f);
    }

    private void initCalendar() {

        eventProvider = new MeetingDataProvider();

        calendarComponent = new Calendar(eventProvider);

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
            calendarComponent.setFirstVisibleDayOfWeek(firstDay);
            calendarComponent.setLastVisibleDayOfWeek(lastDay);
        }

        Date today = getToday();
        calendar = new GregorianCalendar();
        calendar.setTime(today);
        calendarComponent.getInternalCalendar().setTime(today);

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
        calendarComponent.setStartDate(currentMonthsFirstDate);

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
            calendarComponent.setEndDate(calendar.getTime());
        } else {
            calendarComponent.setStartDate(calendar.getTime());
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
//        calendar.setHandler(new ExtendedForwardHandler());
//        calendar.setHandler(new ExtendedBackwardHandler());
//        calendar.setHandler(new ExtendedBasicItemMoveHandler());
//        calendar.setHandler(new ExtendedItemResizeHandler());
        //calendar.setHandler(new BasicDateClickHandler(false));
        calendarComponent.setHandler(this::onCalendarClick);
        calendarComponent.setHandler(this::onCalendarRangeSelect);
    }

    private void onCalendarClick(CalendarComponentEvents.ItemClickEvent event) {

        MeetingItem item = (MeetingItem) event.getCalendarItem();

        final Meeting meeting = item.getMeeting();

        Notification.show(meeting.getName(), meeting.getDetails(), Notification.Type.HUMANIZED_MESSAGE);
    }

    private void onCalendarRangeSelect(CalendarComponentEvents.RangeSelectEvent event) {

        Meeting meeting = new Meeting();

        meeting.setStart(event.getStart());
        meeting.setEnd(event.getEnd());
        meeting.setName("A Name");
        meeting.setDetails("A Detail");

        // Random state
        //meeting.setState(R.nextInt(2) == 1 ? Meeting.State.planned : Meeting.State.confirmed);

        eventProvider.addItem(new MeetingItem(meeting));
    }

    private final class MeetingDataProvider extends BasicItemProvider<MeetingItem> {

        void removeAllEvents() {
            this.itemList.clear();
            fireItemSetChanged();
        }
    }
}
