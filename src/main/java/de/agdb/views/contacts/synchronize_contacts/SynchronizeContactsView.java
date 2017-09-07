package de.agdb.views.contacts.synchronize_contacts;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import de.agdb.AppUI;
import de.agdb.backend.data_model.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.vaadin.addons.Toastr;

import javax.annotation.PostConstruct;

@UIScope
@SpringView(name = SynchronizeContactsView.VIEW_NAME)
public class SynchronizeContactsView extends VerticalLayout implements View, Button.ClickListener {

    public static final String VIEW_NAME = "SynchronizeContactsView";

    @Autowired
    JdbcTemplate jdbcTemplate;
    private Toastr toastr = new Toastr();




    @Autowired
    UsersRepository usersRepository;

    @PostConstruct
    public void init() {
        setSizeFull();
        VerticalLayout formWrapper = new VerticalLayout();
        formWrapper.setWidth(1150,     Unit.PIXELS);
        formWrapper.setHeight(650, Unit.PIXELS);
        //formWrapper.setSizeFull();
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
        formWrapper.addComponent(toastr);

       // addComponent(toastr);
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

        HorizontalLayout googleLayout = new ContactServiceLayout("google",  usersRepository, toastr);
        HorizontalLayout microsoftLayout = new ContactServiceLayout("microsoft",  usersRepository, toastr);
        HorizontalLayout yahooLayout = new ContactServiceLayout("yahoo",  usersRepository, toastr);
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
