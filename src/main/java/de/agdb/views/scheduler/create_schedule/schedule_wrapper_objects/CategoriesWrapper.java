package de.agdb.views.scheduler.create_schedule.schedule_wrapper_objects;

public class CategoriesWrapper {

    public int getNumberParticipants() {
        return numberParticipants;
    }

    public void setNumberParticipants(int numberParticipants) {
        this.numberParticipants = numberParticipants;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    int numberParticipants;
    String category;

    public CategoriesWrapper(int numberParticipants, String category) {
            this.numberParticipants = numberParticipants;
            this.category = category;
    }

}
