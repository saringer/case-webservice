package de.agdb.views.contacts.manage_contacts;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import de.agdb.AppUI;
import de.agdb.backend.entities.Contact;
import de.agdb.backend.entities.Users;
import de.agdb.backend.entities.UsersRepository;
import de.agdb.views.contacts.synchronize_contacts.SynchronizeContactsView;
import org.atmosphere.config.service.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.alump.materialicons.MaterialIcons;

import javax.annotation.PostConstruct;


@UIScope
@SpringView(name = ManageContactsViews.VIEW_NAME)
public class ManageContactsViews extends VerticalLayout implements View {

    public static final String VIEW_NAME = "ManageContactsView";
    private Grid<Contact> grid;


    @Autowired
    UsersRepository repository;


    /*public ManageContactsViews() {

        setSizeFull();
        VerticalLayout formWrapper = new VerticalLayout();
        formWrapper.setWidth("80%");
        formWrapper.setHeight("75%");
        addComponent(formWrapper);
        setComponentAlignment(formWrapper, Alignment.MIDDLE_CENTER);


        VerticalLayout content = builcContactList();
        content.setWidth("80%");
        content.setHeight("80%");


        formWrapper.addStyleName("solid-border");
        formWrapper.setSpacing(false);
        formWrapper.setMargin(false);
        formWrapper.addComponent(content);
        formWrapper.setComponentAlignment(content, Alignment.MIDDLE_CENTER);
        formWrapper.setExpandRatio(content, 1);

    }*/


    @PostConstruct
    void init() {
        setSizeFull();
        VerticalLayout formWrapper = new VerticalLayout();
        formWrapper.setWidth("80%");
        formWrapper.setHeight("75%");
        addComponent(formWrapper);
        setComponentAlignment(formWrapper, Alignment.MIDDLE_CENTER);


        VerticalLayout content = builcContactList();
        content.setWidth("80%");
        content.setHeight("80%");


        formWrapper.addStyleName("solid-border");
        formWrapper.setSpacing(false);
        formWrapper.setMargin(false);
        formWrapper.addComponent(content);
        formWrapper.setComponentAlignment(content, Alignment.MIDDLE_CENTER);
        formWrapper.setExpandRatio(content, 1);
    }

    private VerticalLayout buildContent() {
        return null;
    }

    private VerticalLayout builcContactList() {
        VerticalLayout wrapperLayout = new VerticalLayout();
        wrapperLayout.setSizeFull();
        wrapperLayout.setSpacing(false);

        CssLayout header = new CssLayout();
        header.setWidth("100%");
        header.setHeight(50, Unit.PIXELS);
        header.addStyleNames("synccontacts-header");
        Label label = new Label("Contact list");
        //label.setWidth("100%");
        label.addStyleNames("headerLabel");
        //label.addStyleNames(ValoTheme.LABEL_H3);
        //label.addStyleNames(ValoTheme.LABEL_COLORED);
        header.addComponent(label);

        TextField searchField = new TextField();
        searchField.setDescription("search...");
        searchField.setValue("search...");
        searchField.setIcon(VaadinIcons.SEARCH);
        searchField.addStyleNames(ValoTheme.TEXTFIELD_INLINE_ICON);
        searchField.setWidth("100%");

        Button addContactButton = new Button("Add contact");
        addContactButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        addContactButton.setIcon(MaterialIcons.GROUP_ADD);
        addContactButton.setWidth("100%");

        // Create a grid bound to the list
        grid = new Grid<>(Contact.class);
        grid.setSizeFull();
//        initContactList();

        Button test = new Button("Test");
        test.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                    initContactList();
            }
        });

        //grid.addColumn(Contact::getFirstName).setCaption("First name");
        //grid.addColumn(Contact::getLastName).setCaption("Last name");

        //grid.addColumn(Contact::)

        wrapperLayout.addComponent(header);
        wrapperLayout.addComponent(searchField);
        wrapperLayout.addComponent(addContactButton);
        wrapperLayout.addComponent(grid);
        wrapperLayout.addComponent(test);
        wrapperLayout.setExpandRatio(grid, 1f);


        return wrapperLayout;
    }

    public void initContactList() {
        AppUI app = (AppUI) UI.getCurrent();
        String userName = app.getAccessControl().getUsername();

        if (!repository.findByUsername(userName).isEmpty()) {
            grid.setItems(repository.findByUsername(userName).get(0).getContacts());
        }

           // grid.setItems(repository.findByUsername("a").get(0).getContacts());
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

               initContactList();
    }

}
