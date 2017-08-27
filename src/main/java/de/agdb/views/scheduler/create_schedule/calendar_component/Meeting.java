package de.agdb.views.scheduler.create_schedule.calendar_component;

import de.agdb.backend.entities.schedule_wrapper_objects.DateWrapper;

import java.time.ZonedDateTime;

import static de.agdb.views.scheduler.create_schedule.calendar_component.Meeting.State.empty;


public class Meeting {

    enum State {
        empty,
        planned,
        confirmed
    }

    private ZonedDateTime start;

    private ZonedDateTime end;

    private String name;

    private String details;

    private State state = empty;


    private DateWrapper dayObject;

    public Meeting() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public ZonedDateTime getStart() {
        return start;
    }

    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    public ZonedDateTime getEnd() {
        return end;
    }

    public void setEnd(ZonedDateTime end) {
        this.end = end;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public boolean isEditable() {
        return state != State.confirmed;
    }

    public DateWrapper getDayObject() {
        return dayObject;
    }

    public void setDayObject(DateWrapper dayObject) {
        this.dayObject = dayObject;
    }

}
