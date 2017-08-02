package de.agdb.views.scheduler.create_schedule.schedule_wrapper_objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DayWrapper {

    private Date day;
    private List<TimeLocationWrapper> timeAndLocationList;


    public DayWrapper(Date day) {
        this.day = day;
        this.timeAndLocationList = new ArrayList<>();
    }

    public void addTimeLocation(TimeLocationWrapper e) {
            timeAndLocationList.add(e);
    }
    public void removeTimeLocation(TimeLocationWrapper e) {
        timeAndLocationList.remove(e);
    }

    public Date getDay() {
        return this.day;
    }

    public List<TimeLocationWrapper> getTimeAndLocationList() {
        return  this.timeAndLocationList;
    }




}
