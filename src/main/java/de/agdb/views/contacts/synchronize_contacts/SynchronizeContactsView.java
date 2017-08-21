package de.agdb.views.contacts.synchronize_contacts;

import com.google.api.client.util.store.FileDataStoreFactory;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import de.agdb.AppUI;
import de.agdb.backend.entities.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.PostConstruct;
import java.io.File;

@UIScope
@SpringView(name = SynchronizeContactsView.VIEW_NAME)
public class SynchronizeContactsView extends VerticalLayout implements View, Button.ClickListener {

    public static final String VIEW_NAME = "SynchronizeContactsView";

    @Autowired
    JdbcTemplate jdbcTemplate;


    /**
     * File for storing user credentials.
     */
    private static final java.io.File DATA_STORE_FILE =
            new File(System.getProperty("user.home"), ".credentials/user-credentials.json");

    /**
     * Global instance of the {DataStoreFactory}. The best practice is to make it a single
     * globally shared instance across your application.
     */
    private static FileDataStoreFactory dataStoreFactory;





    private Button googleButton;

    @Autowired
    UsersRepository usersRepository;

    @PostConstruct
    public void init() {
        setSizeFull();
        VerticalLayout formWrapper = new VerticalLayout();
        formWrapper.setWidth("80%");
        formWrapper.setHeight("75%");
        addComponent(formWrapper);
        setComponentAlignment(formWrapper, Alignment.MIDDLE_CENTER);



        VerticalLayout content = new VerticalLayout();
        content.addStyleName("solid-border");
        content.setSpacing(false);
        content.setMargin(false);
        content.setWidth("70%");
        content.setHeight("70%");
        content.addComponent(buildContent());


        formWrapper.addStyleName("solid-border");
        formWrapper.setSpacing(false);
        formWrapper.setMargin(false);
        formWrapper.addComponent(content);
        formWrapper.setComponentAlignment(content, Alignment.MIDDLE_CENTER);
        formWrapper.setExpandRatio(content, 1);
    }


    private VerticalLayout buildContent() {

        VerticalLayout wrapperLayout = new VerticalLayout();
        wrapperLayout.setSizeFull();
        //wrapperLayout.setSpacing(false);
        wrapperLayout.setMargin(false);

        CssLayout header = new CssLayout();
        header.setWidth("100%");
        header.setHeight(50, Unit.PIXELS);
        header.addStyleNames("synccontacts-header");
        Label label = new Label("Synchronized email accounts");
        //label.setWidth("100%");
        label.addStyleNames("headerLabel");
        //label.addStyleNames(ValoTheme.LABEL_H3);
        //label.addStyleNames(ValoTheme.LABEL_COLORED);
        header.addComponent(label);

        VerticalLayout innerWrapperLayout = new VerticalLayout();
        innerWrapperLayout.setSizeFull();
        innerWrapperLayout.setSpacing(false);

        AppUI app = (AppUI) UI.getCurrent();
        String currentUser = app.getAccessControl().getUsername();

        HorizontalLayout googleLayout = new ContactServiceLayout("google", currentUser, usersRepository);
        HorizontalLayout microsoftLayout = new ContactServiceLayout("microsoft", currentUser, usersRepository);
        HorizontalLayout yahooLayout = new ContactServiceLayout("yahoo", currentUser, usersRepository);
        innerWrapperLayout.addComponents(googleLayout, microsoftLayout, yahooLayout);


        wrapperLayout.addComponent(header);
        wrapperLayout.addComponent(innerWrapperLayout);

        wrapperLayout.setExpandRatio(innerWrapperLayout, 1);

        return wrapperLayout;

    }





    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        Button clickedButton = clickEvent.getButton();


    }

}
