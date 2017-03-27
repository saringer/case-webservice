package de.agdb.views.scheduler;


import com.vaadin.event.dnd.DragSourceExtension;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.dnd.EffectAllowed;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TreeGrid;
import com.vaadin.ui.VerticalLayout;
import de.agdb.views.categories.CategoriesView;

@UIScope
@SpringView(name = SchedulerView.VIEW_NAME)
public class SchedulerView extends VerticalLayout implements View {
    public static final String VIEW_NAME = "Scheduler";


    public SchedulerView() {
        setSizeFull();
        Label draggableLabel = new Label("Work in progress");
        addComponent(draggableLabel);



    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
