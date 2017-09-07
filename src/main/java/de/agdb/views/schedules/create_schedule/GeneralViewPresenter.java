package de.agdb.views.schedules.create_schedule;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GeneralViewPresenter {

    private EventDescriptionView eventDescriptionView;

    public void setEventDescriptionView(EventDescriptionView eventDescriptionView) {
        this.eventDescriptionView = eventDescriptionView;
    }
}
