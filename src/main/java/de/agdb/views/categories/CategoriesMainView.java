package de.agdb.views.categories;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@UIScope
@SpringView(name = CategoriesMainView.VIEW_NAME)
public class CategoriesMainView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "CategoriesView";

    public CategoriesMainView() {
        setSizeFull();
        
        VerticalLayout formWrapper = new VerticalLayout();
        formWrapper.setSizeFull();
        addComponent(formWrapper);
        setComponentAlignment(formWrapper, Alignment.MIDDLE_CENTER);


        HorizontalLayout form = buildMainMenu();


        formWrapper.setMargin(true);
        formWrapper.setSpacing(true);
        formWrapper.addComponent(form);
        formWrapper.setComponentAlignment(form, Alignment.MIDDLE_CENTER);


    }

    public HorizontalLayout buildMainMenu() {

        HorizontalLayout form = new HorizontalLayout();
        form.addStyleNames("overflow-auto");
        form.setSpacing(false);
        form.setMargin(false);
        //form.setWidth("60%");
        //form.setHeight("65%");
        form.setWidth(800, Unit.PIXELS);
        form.setHeight(400, Unit.PIXELS);

        /*
        LEFT MENU TILE
         */
        VerticalLayout syncContactsTile = new VerticalLayout();
        syncContactsTile.setSpacing(false);
        syncContactsTile.setMargin(false);
        syncContactsTile.setSizeFull();
        syncContactsTile.addStyleName("left-menu-style");

        Label icon = new Label();
        icon.setContentMode(ContentMode.HTML);
        icon.setValue(VaadinIcons.LIST_OL.getHtml());
        icon.addStyleNames("tile-icon-font");

        Label header = new Label("Manage categories");
        header.addStyleNames(ValoTheme.LABEL_HUGE);


        VerticalLayout wrapperLayout = new VerticalLayout();
        wrapperLayout.setMargin(true);
        wrapperLayout.addComponent(icon);
        wrapperLayout.addComponent(header);
        wrapperLayout.setMargin(false);
        wrapperLayout.setSpacing(false);
        wrapperLayout.setWidth("50%");
        wrapperLayout.setHeight("50%");
        wrapperLayout.setComponentAlignment(icon, Alignment.MIDDLE_CENTER);
        wrapperLayout.setComponentAlignment(header, Alignment.MIDDLE_CENTER);

        syncContactsTile.addComponent(wrapperLayout);
        syncContactsTile.setComponentAlignment(wrapperLayout, Alignment.MIDDLE_CENTER);

        /*
        RIGHT MENU TILE
         */

        VerticalLayout manageContactsTile = new VerticalLayout();
        manageContactsTile.setSizeFull();
        manageContactsTile.addStyleName("right-menu-style");

        icon = new Label();
        icon.setContentMode(ContentMode.HTML);
        icon.setValue(VaadinIcons.TAGS.getHtml());
        icon.addStyleNames("tile-icon-font");

        header = new Label("Assign categories");
        header.addStyleNames(ValoTheme.LABEL_HUGE);


        wrapperLayout = new VerticalLayout();
        wrapperLayout.setMargin(true);
        wrapperLayout.addComponent(icon);
        wrapperLayout.addComponent(header);
        wrapperLayout.setMargin(false);
        wrapperLayout.setSpacing(false);
        wrapperLayout.setWidth("50%");
        wrapperLayout.setHeight("50%");
        wrapperLayout.setComponentAlignment(icon, Alignment.MIDDLE_CENTER);
        wrapperLayout.setComponentAlignment(header, Alignment.MIDDLE_CENTER);
        manageContactsTile.addComponent(wrapperLayout);
        manageContactsTile.setComponentAlignment(wrapperLayout, Alignment.MIDDLE_CENTER);



        form.addComponent(syncContactsTile);
        form.addComponent(manageContactsTile);

        return form;

    }



}
