package de.agdb.views.scheduler.create_schedule.schedule_wrapper_objects;


import java.util.List;


public class TimeLocationWrapper {

    List<TimeLocationObject> timeAndLocationList;

    public List<TimeLocationObject> getTimeAndLocationList() {
        return timeAndLocationList;
    }

    public void setTimeAndLocationList(List<TimeLocationObject> timeAndLocationList) {
        this.timeAndLocationList = timeAndLocationList;
    }

    public void addTimeAndLocation(TimeLocationObject e) {
        timeAndLocationList.add(e);
    }




    public TimeLocationWrapper() {

    }


}
