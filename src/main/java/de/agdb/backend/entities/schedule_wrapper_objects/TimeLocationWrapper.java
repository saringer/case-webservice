package de.agdb.backend.entities.schedule_wrapper_objects;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "timelocation")
public class TimeLocationWrapper {


    // An autogenerated id (unique for each schedule in the db)
    @Id
    @Column(name = "TIMELOCATION_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int startHour;
    private int startMin;
    private int endHour;
    private int endMin;
    private String formattedStartTime;
    private String formattedEndTime;
    private String location;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = CategoriesWrapper.class)
    @JoinTable(name = "TIMELOCATION_CATEGORYSETUP", joinColumns = {@JoinColumn(name = "TIMELOCATION_ID")}, inverseJoinColumns = {@JoinColumn(name = "CATEGORYSETUP_ID")})
    List<CategoriesWrapper> categoriesList;


    public TimeLocationWrapper() {

    }

    public TimeLocationWrapper(String formattedStartTime, String formattedEndTime, int startHour, int startMin, int endHour, int endMin, String location) {
        this.formattedStartTime = formattedStartTime;
        this.formattedEndTime = formattedEndTime;
        this.startHour = startHour;
        this.startMin = startMin;
        this.endHour = endHour;
        this.endMin = endMin;
        this.location = location;
        this.categoriesList = new ArrayList<>();

    }

    public void addCategory(CategoriesWrapper e) {
        categoriesList.add(e);
    }

    public void removeCategory(CategoriesWrapper e) {
        categoriesList.remove(e);

    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public List<CategoriesWrapper> getCategoriesList() {
        return this.categoriesList;
    }

    public void setCategoriesList(List<CategoriesWrapper> categoriesList) {
        this.categoriesList = categoriesList;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getStartMin() {
        return startMin;
    }

    public void setStartMin(int startMin) {
        this.startMin = startMin;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public int getEndMin() {
        return endMin;
    }

    public void setEndMin(int endMin) {
        this.endMin = endMin;
    }

    public String getFormattedStartTime() {
        return formattedStartTime;
    }

    public void setFormattedStartTime(String formattedStartTime) {
        this.formattedStartTime = formattedStartTime;
    }

    public String getFormattedEndTime() {
        return formattedEndTime;
    }

    public void setFormattedEndTime(String formattedEndTime) {
        this.formattedEndTime = formattedEndTime;
    }

}
