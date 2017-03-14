package de.agdb.views.contacts;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import de.agdb.entities.Contacts;

import java.util.List;


public class ContactView extends CssLayout implements View {


    public static final String VIEW_NAME = "Contacts";
    private ContactService service = ContactService.getInstance();
    private Grid<Contacts> grid = new Grid<>(Contacts.class);
    private TextField filterText = new TextField();
    private ContactForm form = new ContactForm(this);


    public ContactView() {
        setSizeFull();
        final VerticalLayout layout = new VerticalLayout();

        filterText.setPlaceholder("filter by name...");
        filterText.addValueChangeListener(e -> updateList());
        filterText.setValueChangeMode(ValueChangeMode.LAZY);

        Button clearFilterTextBtn = new Button(FontAwesome.TIMES);
        clearFilterTextBtn.setDescription("Clear the current filter");
        clearFilterTextBtn.addClickListener(e -> filterText.clear());

        CssLayout filtering = new CssLayout();
        filtering.addComponents(filterText, clearFilterTextBtn);
        filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

        Button addContactBtn = new Button("Add new contact");
        addContactBtn.addClickListener(e -> {
            grid.asSingleSelect().clear();
            form.setContacts(new Contacts());
        });
        addContactBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);

        Button addEmailContactsBtn = new Button("Synchronize with your e-Mail contacts");
        addEmailContactsBtn.addClickListener(e -> {

        });
        addEmailContactsBtn.setStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);

        HorizontalLayout toolbar = new HorizontalLayout(filtering, addContactBtn, addEmailContactsBtn);

        grid.setColumns("firstName", "lastName", "email");

        HorizontalLayout main = new HorizontalLayout(grid, form);
        main.setSizeFull();
        grid.setSizeFull();
        main.setExpandRatio(grid, 1);

        layout.addComponents(toolbar, main);

        // fetch list of Customers from service and assign it to Grid
        updateList();


        addComponent(layout);

        form.setVisible(false);

        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() == null) {
                form.setVisible(false);
            } else {
                form.setContacts(event.getValue());
            }
        });



    }

    public void updateList() {
        List<Contacts> contactss = service.findAll(filterText.getValue());
        grid.setItems(contactss);
    }



    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }


}
