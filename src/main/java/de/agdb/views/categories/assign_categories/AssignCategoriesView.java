package de.agdb.views.categories.assign_categories;

import com.vaadin.navigator.View;
import com.vaadin.server.Sizeable;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import de.agdb.backend.entities.Contact;
import de.agdb.views.categories.manage_categories.ManageCategoriesView;

import javax.annotation.PostConstruct;

@UIScope
@SpringView(name = AssignCategoriesView.VIEW_NAME)
public class AssignCategoriesView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "AssignCategoriesView";

    @PostConstruct
    void init() {
        addStyleNames("general-background-color-grey");
        setSizeFull();
        VerticalLayout formWrapper = new VerticalLayout();
        formWrapper.setWidth(1000, Sizeable.Unit.PIXELS);
        formWrapper.setHeight(600, Sizeable.Unit.PIXELS);
        addComponent(formWrapper);
        setComponentAlignment(formWrapper, Alignment.MIDDLE_CENTER);


        VerticalLayout content = buildContent();
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

    public VerticalLayout buildContent() {
        VerticalLayout wrapperLayout = new VerticalLayout();
        wrapperLayout.setSizeFull();
        wrapperLayout.setSpacing(false);
        wrapperLayout.setMargin(false);

        Label test = new Label("Test");
        test.setWidth("100%");

        TextField searchbar = new TextField();
        searchbar.setWidth("100%");

        // Create a grid bound to the list
        Grid grid = new Grid<>(Contact.class);
        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setColumns("firstName", "lastName");
        grid.setItems(new Contact("Riva", "Rova", "Riva"));

        wrapperLayout.addComponent(test);
        wrapperLayout.addComponent(searchbar);
        wrapperLayout.addComponent(grid);
        wrapperLayout.setExpandRatio(grid, 1);

        return  wrapperLayout;
    }
}
