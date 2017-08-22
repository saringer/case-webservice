package de.agdb.views.categories;

import com.vaadin.event.LayoutEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
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
        VerticalLayout manageCategoriesTile = new VerticalLayout();
        manageCategoriesTile.setSpacing(false);
        manageCategoriesTile.setMargin(false);
        manageCategoriesTile.setSizeFull();
        manageCategoriesTile.addStyleName("left-menu-style");

        Label icon = new Label();
        icon.setContentMode(ContentMode.HTML);
        icon.setValue(VaadinIcons.LIST_OL.getHtml());
        icon.addStyleName("tile-icon-font");

        Label header = new Label("Manage categories");
        header.addStyleNames(ValoTheme.LABEL_HUGE,"label-margin-top");


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

        manageCategoriesTile.addComponent(wrapperLayout);
        manageCategoriesTile.setComponentAlignment(wrapperLayout, Alignment.MIDDLE_CENTER);
        manageCategoriesTile.addLayoutClickListener((LayoutEvents.LayoutClickListener) layoutClickEvent -> {
            UI.getCurrent().getNavigator().navigateTo("ManageCategoriesView");
        });


        /*
        RIGHT MENU TILE
         */

        VerticalLayout assignCategoriesTile = new VerticalLayout();
        assignCategoriesTile.setSizeFull();
        assignCategoriesTile.addStyleName("right-menu-style");

        icon = new Label();
        icon.setContentMode(ContentMode.HTML);
        icon.setValue(VaadinIcons.TAGS.getHtml());
        icon.addStyleName("tile-icon-font");

        header = new Label("Assign categories");
        header.addStyleNames(ValoTheme.LABEL_HUGE, "label-margin-top");


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
        assignCategoriesTile.addComponent(wrapperLayout);
        assignCategoriesTile.setComponentAlignment(wrapperLayout, Alignment.MIDDLE_CENTER);
        assignCategoriesTile.addLayoutClickListener((LayoutEvents.LayoutClickListener) layoutClickEvent -> {
            UI.getCurrent().getNavigator().navigateTo("AssignCategoriesView");
        });



        form.addComponent(manageCategoriesTile);
        form.addComponent(assignCategoriesTile);

        return form;

    }



}
