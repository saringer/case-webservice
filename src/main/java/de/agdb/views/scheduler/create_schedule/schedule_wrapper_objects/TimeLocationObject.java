package de.agdb.views.scheduler.create_schedule.schedule_wrapper_objects;

class TimeLocationObject {

    String time;
    String location;
    int componentId;

    public int getComponentId() {
        return componentId;
    }

    public void setComponentId(int componentId) {
        this.componentId = componentId;
    }



    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }



    public TimeLocationObject(String time, String location, int id) {
        this.time = time;
        this.location = location;
        this.componentId = id;
    }


}
