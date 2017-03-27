package de.agdb.views;


import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.Component;
import de.agdb.AppUI;
import de.agdb.views.categories.CategoriesView;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import de.agdb.views.contacts.ContactView;
import de.agdb.views.scheduler.SchedulerView;
import de.agdb.views.userProfile.ProfileView;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * Content of the UI when the user is logged in.
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

        menu = new Menu(navigator);
        menu.addView(new SchedulerView(), SchedulerView.VIEW_NAME, SchedulerView.VIEW_NAME, FontAwesome.USER);

        menu.addView(new ProfileView(), ProfileView.VIEW_NAME, ProfileView.VIEW_NAME, FontAwesome.USER_PLUS);

        menu.addView(new CategoriesView(), CategoriesView.VIEW_NAME, CategoriesView.VIEW_NAME, FontAwesome.CHAIN);
        menu.addView(new ContactView(), ContactView.VIEW_NAME, ContactView.VIEW_NAME, FontAwesome.EDIT);
        //menu.addView(new ServletView(), ServletView.VIEW_NAME, ServletView.VIEW_NAME, FontAwesome.ARROW_CIRCLE_UP);


        addComponent(menu);
        addComponent(viewContainer);

        setExpandRatio(viewContainer, 1);

    }

    @Override
    public void showView(View view) {
        viewContainer.addComponent((Component) view);
    }

}
