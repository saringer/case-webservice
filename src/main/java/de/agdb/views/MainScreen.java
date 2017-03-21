package de.agdb.views;

import de.agdb.AppUI;
import de.agdb.views.categories.CategoriesView;
import de.agdb.views.contacts.ContactView;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import de.agdb.views.scheduler.SchedulerView;
import de.agdb.views.userProfile.ProfileView;

import java.io.IOException;

/**
 * Content of the UI when the user is logged in.
 */
public class MainScreen extends HorizontalLayout {

    Menu menu;
    // notify the view menu about view changes so that it can display which view
    // is currently active
    ViewChangeListener viewChangeListener = new ViewChangeListener() {

        @Override
        public boolean beforeViewChange(ViewChangeEvent event) {
            return true;
        }

        @Override
        public void afterViewChange(ViewChangeEvent event) {
            menu.setActiveView(event.getViewName());
        }

    };

    public MainScreen(AppUI ui) {

        setSizeFull();

        /*VerticalLayout menu = new VerticalLayout();
        menu.setSizeFull();
        Label menuLabel = new Label("asdasd");
        menuLabel.setValue("Hauptmenü");
        menuLabel.addStyleName(ValoTheme.LABEL_COLORED);
        menu.addComponent(menuLabel);*/


        CssLayout viewContainer = new CssLayout();
        viewContainer.addStyleName("valo-content");
        viewContainer.addStyleName("overflow-auto");
        viewContainer.setSizeFull();


        final Navigator navigator = new Navigator(ui, viewContainer);
        //navigator.setErrorView(ErrorView.class);
        navigator.addViewChangeListener(viewChangeListener);


        menu = new Menu(navigator);
        menu.addView(new SchedulerView(), SchedulerView.VIEW_NAME, SchedulerView.VIEW_NAME, FontAwesome.USER);
        try {
            menu.addView(new ProfileView(), ProfileView.VIEW_NAME, ProfileView.VIEW_NAME,  FontAwesome.USER_PLUS);
        } catch (IOException e) {
            e.printStackTrace();
        }
        menu.addView(new CategoriesView(), CategoriesView.VIEW_NAME, CategoriesView.VIEW_NAME, FontAwesome.CHAIN);
        menu.addView(new ContactView(), ContactView.VIEW_NAME, ContactView.VIEW_NAME, FontAwesome.EDIT);




        addComponent(menu);
        addComponent(viewContainer);

        setExpandRatio(viewContainer, 1);

    }
}
