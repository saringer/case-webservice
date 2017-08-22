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
import de.agdb.backend.entities.Categories;
import de.agdb.backend.entities.Contact;
import de.agdb.backend.entities.Users;
import de.agdb.backend.entities.repositories.CategoriesRepository;
import de.agdb.backend.entities.repositories.ContactRepository;
import de.agdb.backend.entities.repositories.UsersRepository;
import de.agdb.views.profile.ContactProfile;
import de.agdb.views.profile.ProfileView;
import de.agdb.views.scheduler.CustomButton;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.alump.materialicons.MaterialIcons;

import javax.annotation.PostConstruct;
import java.util.List;


@UIScope
@SpringView(name = ManageContactsView.VIEW_NAME)
public class ManageContactsView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "ManageContactsView";
    private Grid<Contact> grid;
    private TextField firstName;
    private TextField lastName;
    private TextField age;
    private TextField mobile;
    private TextField home;
    private TextField email;
    private TextArea function;
    private Label userDetailsHeader;
    private Contact selectedContact;
    private CustomButton userProfileButton;


    @Autowired
    UsersRepository repository;
    @Autowired
    ContactRepository contactRepository;
    @Autowired
    private CategoriesRepository categoriesRepository;


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
        Label label = new Label("Contact list");
        //label.setWidth("100%");
        label.addStyleNames("headerLabel");
        //label.addStyleNames(ValoTheme.LABEL_H3);
        //label.addStyleNames(ValoTheme.LABEL_COLORED);
        header.addComponent(label);

        TextField searchField = new TextField();
        searchField.setPlaceholder("search...");
        searchField.setIcon(VaadinIcons.SEARCH);
        searchField.addStyleNames(ValoTheme.TEXTFIELD_INLINE_ICON);
        searchField.setWidth("100%");
        searchField.addValueChangeListener(this::setupGridFilter);

        Button addContactButton = new Button("Add contact");
        addContactButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        addContactButton.setIcon(MaterialIcons.GROUP_ADD);
        addContactButton.setWidth("100%");
        addContactButton.addClickListener((Button.ClickListener) clickEvent -> {
            UI.getCurrent().getNavigator().navigateTo("AddContactView");
        });

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

        wrapperLayout.setExpandRatio(grid, 1f);
        wrapperLayout.addStyleName("solid-border");


        return wrapperLayout;
    }

    private void setupGridFilter(HasValue.ValueChangeEvent<String> event) {
        ListDataProvider<Contact> dataProvider = (ListDataProvider<Contact>) grid.getDataProvider();
        dataProvider.clearFilters();
        String filterText = event.getValue();
        dataProvider.setFilter(Contact ->
                caseInsensitiveContains(Contact.getFirstName(), filterText) ||
                        caseInsensitiveContains(Contact.getLastName(), filterText) ||
                        caseInsensitiveContains(Contact.getEmail(), filterText) ||
                        caseInsensitiveContains(Contact.getFunction(), filterText));
    }

    private Boolean caseInsensitiveContains(String fullText, String filterText) {
        if (fullText == null && filterText != null) {
            return false;
        } else {
            return fullText.toLowerCase().contains(filterText.toLowerCase());
        }
    }

    public void initContactList() {
        AppUI app = (AppUI) UI.getCurrent();
        String userName = app.getAccessControl().getUsername();

        if (!repository.findByUsername(userName).isEmpty()) {
            grid.setItems(repository.findByUsername(userName).get(0).getContacts());
        }
        grid.getSelectionModel().addSelectionListener(event -> {


            boolean somethingSelected = !grid.getSelectedItems().isEmpty();
            if (somethingSelected) {
                resetFields();

                Contact contact = event.getFirstSelectedItem().get();
                if (contact.getFirstName() != null) {
                    userDetailsHeader.setValue(contact.getFirstName());
                    firstName.setValue(contact.getFirstName());
                }
                if (contact.getLastName() != null) {
                    String placeHolder = userDetailsHeader.getValue();
                    userDetailsHeader.setValue(placeHolder + " " + contact.getLastName());
                    lastName.setValue(contact.getLastName());
                }
                if (contact.getEmail() != null) {
                    email.setValue(contact.getEmail());
                }
                if (contact.getMobile() != null) {
                    mobile.setValue(contact.getMobile());
                }
                if (contact.getHome() != null) {
                    home.setValue(contact.getHome());
                }
                if (contact.getAge() != null) {
                    age.setValue(String.valueOf(contact.getAge()));
                }
                if (contact.getFunction() != null) {
                    function.setValue(contact.getFunction());
                }
                selectedContact = contact;
                userProfileButton.setVisible(true);
            }

        });
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

        initContactList();
        resetFields();
    }

    private VerticalLayout buildContactDetails() {
        VerticalLayout wrapperLayout = new VerticalLayout();
        wrapperLayout.setSizeFull();
        wrapperLayout.setSpacing(false);
        wrapperLayout.setMargin(false);

        CssLayout header = new CssLayout();
        header.setWidth("100%");
        header.setHeight(50, Unit.PIXELS);
        header.addStyleNames("managecontacts-header");
        userDetailsHeader = new Label("");
        //label.setWidth("100%");
        userDetailsHeader.addStyleNames("headerLabel");
        //label.addStyleNames(ValoTheme.LABEL_H3);
        //label.addStyleNames(ValoTheme.LABEL_COLORED);
        header.addComponent(userDetailsHeader);

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
        mobile.setWidth("100%");
        mobile.setCaption("Mobile");
        home = new TextField();
        home.setCaption("Home");
        home.setWidth("100%");
        email = new TextField();
        email.setCaption("Email");
        email.setEnabled(false);
        email.setWidth("100%");
        function = new TextArea();
        function.setRows(3);
        function.setWidth("100%");
        function.setCaption("Function");
        detailsForm.addComponent(firstName);
        detailsForm.addComponent(lastName);
        detailsForm.addComponent(age);
        detailsForm.addComponent(mobile);
        detailsForm.addComponent(home);
        detailsForm.addComponent(email);
        detailsForm.addComponent(function);

        CssLayout bottomNav = new CssLayout();
        bottomNav.setWidth("100%");

        LayoutEvents.LayoutClickListener listener = (LayoutEvents.LayoutClickListener) layoutClickEvent -> {
            if (selectedContact != null) {

                AppUI app = (AppUI) UI.getCurrent();
                Users user = repository.findByUsername(app.getAccessControl().getUsername()).get(0);

                user.removeContact(selectedContact);
                repository.save(user);

                selectedContact = null;
                user = repository.findByUsername(app.getAccessControl().getUsername()).get(0);
                grid.setItems(user.getContacts());
            }

        };
        CustomButton deleteButton = new CustomButton(VaadinIcons.TRASH.getHtml() + " " + "DELETE", listener);
        deleteButton.addStyleNames("delete-button", "float-right");
        deleteButton.setHeight(40, Unit.PIXELS);
        deleteButton.setWidth(115, Unit.PIXELS);

        listener = (LayoutEvents.LayoutClickListener) layoutClickEvent -> {

            if (selectedContact != null) {
                selectedContact.setFirstName(firstName.getValue());
                selectedContact.setLastName(lastName.getValue());
                selectedContact.setAge(Integer.parseInt(age.getValue()));
                selectedContact.setMobile(mobile.getValue());
                selectedContact.setHome(home.getValue());
                selectedContact.setFunction(function.getValue());
                contactRepository.save(selectedContact);
                selectedContact = null;
                AppUI app = (AppUI) UI.getCurrent();
                Users user = repository.findByUsername(app.getAccessControl().getUsername()).get(0);
                grid.setItems(user.getContacts());
                resetFields();
            }


        };
        CustomButton saveButton = new CustomButton(VaadinIcons.SERVER.getHtml() + " " + "SAVE", listener);
        saveButton.addStyleNames("float-right", "save-button");
        saveButton.setHeight(40, Unit.PIXELS);
        saveButton.setWidth(115, Unit.PIXELS);

        listener = (LayoutEvents.LayoutClickListener) event -> {
            Window window = new Window();
            window.setWidth(900, Unit.PIXELS);
            window.setHeight(700, Unit.PIXELS);
            window.center();
            window.setModal(true);
            window.addStyleName("set-window-style");


            window.setContent(new ContactProfile(window));
            UI.getCurrent().addWindow(window);
        };

        userProfileButton = new CustomButton("Contact Info", listener);
        userProfileButton.addStyleNames("float-left", "next-button");
        userProfileButton.setHeight(40, Unit.PIXELS);
        userProfileButton.setWidth(115, Unit.PIXELS);
        userProfileButton.setVisible(false);

        bottomNav.addComponents(userProfileButton, saveButton, deleteButton);

        wrapperLayout.addComponent(header);
        wrapperLayout.addComponent(detailsForm);
        wrapperLayout.addComponent(bottomNav);
        wrapperLayout.setExpandRatio(detailsForm, 1);
        wrapperLayout.addStyleName("details-layout-border");

        return wrapperLayout;


    }

    private void resetFields() {
        userDetailsHeader.setValue("");
        firstName.setValue("");
        lastName.setValue("");
        email.setValue("");
        age.setValue("");
        mobile.setValue("");
        home.setValue("");
        function.setValue("");
        userProfileButton.setVisible(false);
    }
}
