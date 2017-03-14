package de.agdb.views.contacts;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import de.agdb.entities.Contacts;

public class ContactForm extends FormLayout {

    private TextField firstName = new TextField("First name");
    private TextField lastName = new TextField("Last name");
    private TextField email = new TextField("Email");
    private NativeSelect<ContactStatus> status = new NativeSelect<>("Status");
    private DateField birthdate = new DateField("Birthday");
    private Button save = new Button("Save");
    private Button delete = new Button("Delete");
    private Button cancel = new Button("Cancel");

    private ContactService service = ContactService.getInstance();
    private Contacts contacts;
    private ContactView myUI;
    private Binder<Contacts> binder = new Binder<>(Contacts.class);

    public ContactForm(ContactView myUI) {
        this.myUI = myUI;

        setSizeUndefined();
        HorizontalLayout buttons = new HorizontalLayout(save, delete, cancel);
        addComponents(firstName, lastName, email, status, birthdate, buttons);

        status.setItems(ContactStatus.values());
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(KeyCode.ENTER);

        binder.bindInstanceFields(this);

        save.addClickListener(e -> this.save());
        delete.addClickListener(e -> this.delete());
        cancel.addClickListener(e -> this.cancel());
    }

    public void setContacts(Contacts contacts) {
        this.contacts = contacts;
        binder.setBean(contacts);

        // Show delete button for only customers already in the database
        delete.setVisible(contacts.isPersisted());
        setVisible(true);
        firstName.selectAll();
    }

    private void delete() {
        service.delete(contacts);
        myUI.updateList();
        setVisible(false);
    }

    private void save() {
        service.save(contacts);
        myUI.updateList();
        setVisible(false);
    }

    private void cancel() {
        setVisible(false);
    }

}
