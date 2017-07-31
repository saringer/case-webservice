package de.agdb.views.scheduler.create_schedule.schedule_wrapper_objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GlobalWrapper {

    private List<DayWrapper> days;
    private String title;

    public GlobalWrapper() {
            days = new ArrayList<>();
    }

    public void addDay(DayWrapper e) {
        days.add(e);
        System.out.println("Day added" +  e.getDay());
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return this.title;
    }
    public List<DayWrapper> getDays() {
        return this.days;
    }

    public void removeDay(Date day) {
        for (int i=0; i<days.size();i++) {
            if ((days.get(i).getDay().compareTo(day)) == 0) {
                        days.remove(i);
                System.out.println("Day removed" + day);

                break;
            }
        }
    }

}
