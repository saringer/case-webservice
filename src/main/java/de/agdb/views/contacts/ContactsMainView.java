package de.agdb.views.contacts;

import com.vaadin.client.ui.Icon;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import de.agdb.views.profile.ProfileView;
import de.agdb.views.scheduler.CalendarComponent;

@UIScope
@SpringView(name = ContactsMainView.VIEW_NAME)
public class ContactsMainView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "ContactsView";

    public ContactsMainView() {
        setSizeFull();

        VerticalLayout formWrapper = new VerticalLayout();
        formWrapper.setSizeFull();
        addComponent(formWrapper);
        setComponentAlignment(formWrapper, Alignment.MIDDLE_CENTER);




        HorizontalLayout form = new HorizontalLayout();
        form.setSpacing(false);
        form.setMargin(false);
        form.setWidth("60%");
        form.setHeight("65%");


        VerticalLayout syncContactsTile = new VerticalLayout();
        syncContactsTile.setSpacing(false);
        syncContactsTile.setMargin(false);
        syncContactsTile.setSizeFull();
        syncContactsTile.addStyleName("left-menu-style");

        Label icon = new Label();
        icon.setContentMode(ContentMode.HTML);
        icon.setValue(VaadinIcons.AT.getHtml());
        icon.addStyleNames("tile-icon-font");

        Label header = new Label("Sync");
        //header.addStyleNames(ValoTheme.LABEL_HUGE);
        //syncContactsTile.addComponent(icon);
        syncContactsTile.addComponent(icon);
//        syncContactsTile.addComponent(header);

        syncContactsTile.setComponentAlignment(icon, Alignment.MIDDLE_CENTER);
        //syncContactsTile.setComponentAlignment(header, Alignment.BOTTOM_CENTER);

        VerticalLayout manageContactsTile = new VerticalLayout();
        manageContactsTile.setSizeFull();
        manageContactsTile.addStyleName("right-menu-style");
        icon = new Label("Manage contacts");
        manageContactsTile.addComponent(icon);
        manageContactsTile.setComponentAlignment(icon, Alignment.MIDDLE_CENTER);

        form.addComponent(syncContactsTile);
        form.addComponent(manageContactsTile);

//        form.setComponentAlignment(bottomNav, Alignment.TOP_CENTER);



        formWrapper.setMargin(true);
        formWrapper.setSpacing(true);
        formWrapper.addComponent(form);
        formWrapper.setComponentAlignment(form, Alignment.MIDDLE_CENTER);


    }



}
