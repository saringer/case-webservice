package de.agdb.views;


import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import de.agdb.AppUI;
import de.agdb.views.categories.CategoriesMainView;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import de.agdb.views.categories.assign_categories.AssignCategoriesView;
import de.agdb.views.categories.manage_categories.AddCategoryView;
import de.agdb.views.categories.manage_categories.ManageCategoriesView;
import de.agdb.views.contacts.ContactsMainView;
import de.agdb.views.contacts.manage_contacts.AddContactView;
import de.agdb.views.contacts.manage_contacts.ManageContactsView;
import de.agdb.views.contacts.synchronize_contacts.SynchronizeContactsView;
import de.agdb.views.scheduler.SchedulerMainView;
import de.agdb.views.profile.ProfileView;
import de.agdb.views.scheduler.create_schedule.SetCategoriesView;
import de.agdb.views.scheduler.create_schedule.SetGeneralView;
import de.agdb.views.scheduler.create_schedule.SetDateView;
import de.agdb.views.scheduler.create_schedule.SetTimeLocationView;
import de.agdb.views.scheduler.manage_schedules.InvitationsView;
import de.agdb.views.scheduler.manage_schedules.ManageSchedulesView;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.addons.ToastDisplayMethod;
import org.vaadin.addons.ToastPosition;
import org.vaadin.addons.ToastType;
import org.vaadin.addons.Toastr;
import org.vaadin.addons.builder.ToastBuilder;

import static org.vaadin.addons.builder.ToastOptionsBuilder.having;

/**
 * Content of the UI when the user is logged in.
 */

/**
 * @SpringViewDisplay: Stereotype annotation for a bean (implementing either ViewDisplay, SingleComponentContainer
 * or ComponentContainer) that should act as a view display for Vaadin Navigator.
 * There should only be one bean annotated as the view display in the scope of a UI.
 */
@SpringViewDisplay

public class MainScreen extends HorizontalLayout implements ViewDisplay {

    Menu menu;
    CssLayout viewContainer;
    Toastr toastr = new Toastr();


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
        menu.addSubView(new ManageSchedulesView(), ManageSchedulesView.VIEW_NAME, "Manage schedules");
        menu.addSubView(new SetGeneralView(), SetGeneralView.VIEW_NAME, "Add event");
        menu.addDetailsView(new SetDateView(), SetDateView.VIEW_NAME);
        menu.addDetailsView(new SetTimeLocationView(), SetTimeLocationView.VIEW_NAME);
        menu.addDetailsView(new SetCategoriesView(), SetCategoriesView.VIEW_NAME);
        menu.addDetailsView(new InvitationsView(), InvitationsView.VIEW_NAME);

        // CATEGORIES
        menu.addView(new CategoriesMainView(), CategoriesMainView.VIEW_NAME, "Categories", null);
        menu.addSubView(new ManageCategoriesView(), ManageCategoriesView.VIEW_NAME, "Manage categories");
        menu.addSubView(new AssignCategoriesView(), AssignCategoriesView.VIEW_NAME, "Assign categories");
        menu.addDetailsView(new AddCategoryView(), AddCategoryView.VIEW_NAME);

        // CONTACTS
        menu.addView(new ContactsMainView(), ContactsMainView.VIEW_NAME, "Contacts", null);
        menu.addSubView(new ManageContactsView(), ManageContactsView.VIEW_NAME, "Manage contacts");
        menu.addSubView(new SynchronizeContactsView(), SynchronizeContactsView.VIEW_NAME, "Synchronize contacts");
        menu.addDetailsView(new AddContactView(), AddContactView.VIEW_NAME);


        // PROFILE
        menu.addView(new ProfileView(), ProfileView.VIEW_NAME, "Profile", null);


        addComponent(menu);
        addComponent(viewContainer);

        setExpandRatio(viewContainer, 1);
        setSpacing(false);
        addComponent(toastr);


    }

    @Override
    public void showView(View view) {
        viewContainer.addComponent((Component) view);
    }

}
