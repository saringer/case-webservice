package de.agdb.views.scheduler.create_schedule.schedule_wrapper_objects;

import java.util.Date;
import java.util.List;

public class DayWrapper {

    private Date day;
    private TimeLocationWrapper timeAndLocationList;

    public DayWrapper(Date day) {
        this.day = day;
        this.timeAndLocationList = new TimeLocationWrapper();
    }

    public void addTimeLocation(TimeLocationObject e) {
            timeAndLocationList.addTimeAndLocation(e);
    }

    public Date getDay() {
        return this.day;
    }




}
