package de.agdb.views.scheduler.create_schedule.schedule_wrapper_objects;

import java.util.ArrayList;
import java.util.List;

public class TimeLocationWrapper {

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }


    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    String startTime;
    String endTime;
    String location;

    public List<CategoriesWrapper> getCategoriesList() {
        return this.categoriesList;
    }

    public void setCategoriesList(List<CategoriesWrapper> categoriesList) {
        this.categoriesList = categoriesList;
    }

    List<CategoriesWrapper> categoriesList;


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public TimeLocationWrapper(String startTime, String endTime, String location) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.categoriesList = new ArrayList<>();

    }

    public void addCategory(CategoriesWrapper e) {
        categoriesList.add(e);
    }

    public void removeCategory(CategoriesWrapper e) {
        categoriesList.remove(e);

    }


}
