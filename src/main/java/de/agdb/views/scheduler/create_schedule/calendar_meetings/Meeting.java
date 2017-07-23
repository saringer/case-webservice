package de.agdb.views.scheduler.create_schedule.calendar_meetings;

import java.util.Date;

import static de.agdb.views.scheduler.create_schedule.calendar_meetings.Meeting.State.empty;


public class Meeting {

    enum State {
        empty,
        planned,
        confirmed
    }

    private Date start;

    private Date end;

    private String name;

    private String details;

    private State state = empty;

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

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
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

}
