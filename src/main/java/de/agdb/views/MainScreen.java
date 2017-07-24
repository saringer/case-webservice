package de.agdb.views;


import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.Component;
import de.agdb.AppUI;
import de.agdb.views.categories.CategoriesMainView;
import de.agdb.views.categories.manage_categories.CategoriesView;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import de.agdb.views.contacts.ContactsMainView;
import de.agdb.views.contacts.synchronize_contacts.SynchronizeContactsView;
import de.agdb.views.scheduler.CalendarTest;
import de.agdb.views.scheduler.SchedulerMainView;
import de.agdb.views.profile.ProfileView;
import de.agdb.views.scheduler.create_schedule.SetCategoriesView;
import de.agdb.views.scheduler.create_schedule.SetGeneralView;
import de.agdb.views.scheduler.create_schedule.SetDateView;
import de.agdb.views.scheduler.create_schedule.SetTimeLocationView;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Content of the UI when the user is logged in.
 */

/**
 * @SpringViewDisplay: Stereotype annotation for a bean (implementing either ViewDisplay, SingleComponentContainer
 * or ComponentContainer) that should act as a view display for Vaadin Navigator.
 There should only be one bean annotated as the view display in the scope of a UI.
 */
@SpringViewDisplay

public class MainScreen extends HorizontalLayout implements ViewDisplay {

    Menu menu;
    CssLayout viewContainer;


    private SpringViewProvider viewProvider;

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

    @Autowired
    public MainScreen(AppUI ui, SpringViewProvider springViewProvider) {
        this.viewProvider = springViewProvider;
        setSizeFull();

        /*VerticalLayout menu = new VerticalLayout();
        menu.setSizeFull();
        Label menuLabel = new Label("asdasd");
        menuLabel.setValue("Hauptmenü");
        menuLabel.addStyleName(ValoTheme.LABEL_COLORED);
        menu.addComponent(menuLabel);*/



        viewContainer = new CssLayout();
        viewContainer.addStyleName("valo-content");
        viewContainer.addStyleName("overflow-auto");
        viewContainer.setSizeFull();


        final Navigator navigator = new Navigator(ui, viewContainer);


        //navigator.setErrorView(ErrorView.class);

        navigator.addViewChangeListener(viewChangeListener);
        navigator.addProvider(viewProvider);


        // Add all the views of the application to the view navigator via the Menu class
        menu = new Menu(navigator);

        /*
        "" Empty view name will be the initially loaded view when accessing localhost:8080
         */
        // SCHEDULE
        menu.addView(new SchedulerMainView(), SchedulerMainView.VIEW_NAME, "Schedule", null);
        menu.addSubView(new SetGeneralView(), SetGeneralView.VIEW_NAME, "Create Schedule");
        menu.addDetailsView(new SetDateView(), SetDateView.VIEW_NAME);
        menu.addDetailsView(new SetTimeLocationView(), SetTimeLocationView.VIEW_NAME);
        menu.addDetailsView(new SetCategoriesView(), SetCategoriesView.VIEW_NAME);

        // CATEGORIES
        menu.addView(new CategoriesMainView(), CategoriesMainView.VIEW_NAME, "Categories", null);


        // CONTACTS
        menu.addView(new ContactsMainView(), ContactsMainView.VIEW_NAME, "Contacts", null);
        menu.addSubView(new SynchronizeContactsView(), SynchronizeContactsView.VIEW_NAME, "Synchronize contacts");


        // PROFILE
        menu.addView(new ProfileView(), ProfileView.VIEW_NAME, ProfileView.VIEW_NAME, null);


        addComponent(menu);
        addComponent(viewContainer);

        setExpandRatio(viewContainer, 1);
        setSpacing(false);


    }

    @Override
    public void showView(View view) {
        viewContainer.addComponent((Component) view);
    }

}
