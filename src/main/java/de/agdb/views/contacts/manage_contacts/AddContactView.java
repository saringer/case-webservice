package de.agdb.views.contacts.manage_contacts;

import com.vaadin.data.HasValue;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.event.LayoutEvents;
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
import de.agdb.backend.entities.repositories.UsersRepository;
import de.agdb.views.scheduler.CustomButton;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.alump.materialicons.MaterialIcons;

import javax.annotation.PostConstruct;


@UIScope
@SpringView(name = AddContactView.VIEW_NAME)
public class AddContactView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "AddContactView";
    private Grid<Contact> grid;
    private TextField firstName;
    private TextField lastName;
    private TextField age;
    private TextField mobile;
    private TextField home;
    private TextField email;
    private TextArea function;


    @Autowired
    UsersRepository repository;


    @PostConstruct
    void init() {
        addStyleNames("general-background-color-grey");
        setSizeFull();
        VerticalLayout formWrapper = new VerticalLayout();
        formWrapper.setWidth(1200, Unit.PIXELS);
        formWrapper.setHeight(800, Unit.PIXELS);
        addComponent(formWrapper);
        setComponentAlignment(formWrapper, Alignment.MIDDLE_CENTER);


        HorizontalLayout content = buildContent();
        content.setWidth("80%");
        content.setHeight("80%");


        formWrapper.addStyleName("solid-border");
        formWrapper.addStyleName("general-background-color-white");
        formWrapper.setSpacing(false);
        formWrapper.setMargin(false);
        formWrapper.addComponent(content);
        formWrapper.setComponentAlignment(content, Alignment.MIDDLE_CENTER);
        formWrapper.setExpandRatio(content, 1);
    }

    private HorizontalLayout buildContent() {
        HorizontalLayout wrapperLayout = new HorizontalLayout();
        wrapperLayout.setSpacing(false);
        wrapperLayout.setSizeFull();

        VerticalLayout contactListLayout = builcContactList();
        VerticalLayout contactDetailsLayout = buildContactDetails();

        wrapperLayout.addComponent(contactListLayout);
        wrapperLayout.addComponent(contactDetailsLayout);
        //wrapperLayout.setExpandRatio(contactListLayout, 0.5f);
        // wrapperLayout.setExpandRatio(contactDetailsLayout, 1);

        return wrapperLayout;
    }

    private VerticalLayout builcContactList() {
        VerticalLayout wrapperLayout = new VerticalLayout();
        wrapperLayout.setSizeFull();
        wrapperLayout.setSpacing(false);
        wrapperLayout.setMargin(false);

        CssLayout header = new CssLayout();
        header.setWidth("100%");
        header.setHeight(50, Unit.PIXELS);
        header.addStyleNames("managecontacts-header");
        header.addStyleNames("solid-border");
        Label label = new Label("Contact list");
        label.addStyleNames("headerLabel");
        header.addComponent(label);

        TextField searchField = new TextField();
        searchField.setPlaceholder("search...");
        searchField.setIcon(VaadinIcons.SEARCH);
        searchField.addStyleNames(ValoTheme.TEXTFIELD_INLINE_ICON);
        searchField.setWidth("100%");
        searchField.setEnabled(false);

        Button addContactButton = new Button("Add contact");
        addContactButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        addContactButton.setIcon(MaterialIcons.GROUP_ADD);
        addContactButton.setWidth("100%");
        addContactButton.setEnabled(false);

        // Create a grid bound to the list
        grid = new Grid<>(Contact.class);
        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setColumns("firstName", "lastName", "email", "function");
//        initContactList();

        //grid.addColumn(Contact::getFirstName).setCaption("First name");
        //grid.addColumn(Contact::getLastName).setCaption("Last name");

        //grid.addColumn(Contact::)
        wrapperLayout.addStyleName("disable-grid-cell-select");
        wrapperLayout.addComponent(header);
        wrapperLayout.addComponent(searchField);
        wrapperLayout.addComponent(addContactButton);
        wrapperLayout.addComponent(grid);
        wrapperLayout.addStyleName("solid-border");

        wrapperLayout.setExpandRatio(grid, 1f);


        return wrapperLayout;
    }



    public void initContactList() {
        AppUI app = (AppUI) UI.getCurrent();
        String userName = app.getAccessControl().getUsername();

        if (!repository.findByUsername(userName).isEmpty()) {
            grid.setItems(repository.findByUsername(userName).get(0).getContacts());
        }

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        resetDetailsFields();
        initContactList();
    }

    private VerticalLayout buildContactDetails() {
        VerticalLayout wrapperLayout = new VerticalLayout();
        wrapperLayout.setSizeFull();
        wrapperLayout.setSpacing(false);
        wrapperLayout.setMargin(false);

        CssLayout header = new CssLayout();
        header.setWidth("100%");
        header.setHeight(50, Unit.PIXELS);
        header.addStyleNames("addcategory-header");
        header.addStyleNames("solid-border");
        Label label = new Label("New contact");
        label.addStyleName("headerLabel");
        header.addComponent(label);


        FormLayout detailsForm = new FormLayout();
        detailsForm.setMargin(true);
        detailsForm.setSizeFull();

        firstName = new TextField();
        firstName.setWidth("100%");
        firstName.setCaption("First name");
        lastName = new TextField();
        lastName.setWidth("100%");
        lastName.setCaption("Last name");
        age = new TextField();
        age.setCaption("Age");
        mobile = new TextField();
        mobile.setCaption("Mobile");
        home = new TextField();
        home.setCaption("Home");
        email = new TextField();
        email.setCaption("Email");
        function = new TextArea();
        function.setRows(3);
        function.setWidth("100%");
        function.setCaption("Function");
        detailsForm.addComponent(firstName);
        detailsForm.addComponent(lastName);
        detailsForm.addComponent(age);
        detailsForm.addComponent(mobile);
        detailsForm.addComponent(email);
        detailsForm.addComponent(function);

        CssLayout bottomNav = new CssLayout();
        bottomNav.setWidth("100%");

        LayoutEvents.LayoutClickListener listener = (LayoutEvents.LayoutClickListener) layoutClickEvent -> {
            UI.getCurrent().getNavigator().navigateTo("ManageContactsView");

        };
        CustomButton backButton = new CustomButton(VaadinIcons.ARROW_CIRCLE_LEFT_O.getHtml() + " " + "BACK", listener);
        backButton.addStyleNames("float-left", "back-button");
        backButton.setHeight(40, Unit.PIXELS);
        backButton.setWidth(115, Unit.PIXELS);


        listener = (LayoutEvents.LayoutClickListener) layoutClickEvent -> {
            //  if (binder.isValid()) {
            AppUI app = (AppUI) UI.getCurrent();
            String userName = app.getAccessControl().getUsername();
            Users thisUser = repository.findByUsername(userName).get(0);
            Contact contact = new Contact();
            contact.setFirstName(firstName.getValue());
            contact.setLastName(lastName.getValue());
            contact.setEmail(email.getValue());
            contact.setMobile(mobile.getValue());
            contact.setFunction(function.getValue());
            contact.setAge(Integer.parseInt(age.getValue()));


            thisUser.addContact(contact);
            repository.save(thisUser);
            app.getNavigator().navigateTo("ManageContactsView");
          /*  }
            else {
                binder.validate();
            }*/
        };
        CustomButton createButton = new CustomButton("CREATE", listener);
        createButton.addStyleNames("float-right", "next-button");
        createButton.setHeight(40, Unit.PIXELS);
        createButton.setWidth(115, Unit.PIXELS);

        bottomNav.addComponents(backButton, createButton);


        wrapperLayout.addComponent(header);
        wrapperLayout.addComponent(detailsForm);
        wrapperLayout.addComponent(bottomNav);
        wrapperLayout.setExpandRatio(detailsForm, 1);
        wrapperLayout.addStyleName("solid-border");

        return wrapperLayout;


    }

    private void resetDetailsFields() {
        firstName.clear();
        lastName.clear();
        age.clear();
        mobile.clear();
        email.clear();
        function.clear();

    }

}

