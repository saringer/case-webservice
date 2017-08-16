package de.agdb.views.categories.manage_categories;

import com.vaadin.event.LayoutEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.UI;
import com.vaadin.v7.shared.ui.colorpicker.Color;
import com.vaadin.v7.ui.ColorPicker;
import de.agdb.AppUI;
import de.agdb.backend.entities.Categories;
import de.agdb.backend.entities.Users;
import de.agdb.backend.entities.UsersRepository;
import de.agdb.views.scheduler.CustomButton;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.alump.materialicons.MaterialIcons;

import javax.annotation.PostConstruct;


@UIScope
@SpringView(name = AddCategoryView.VIEW_NAME)
public class AddCategoryView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "AddCategoryView";
    private Grid<Categories> grid;
    private TextField categoryTitle;
    private TextField categoryShortcut;
    private ColorPicker shortcutColor;
    private TextField categoryTags;
    private TextArea categoryDescription;


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

        VerticalLayout contactListLayout = buildCategoriesList();
        VerticalLayout contactDetailsLayout = buildCategoryDetails();

        wrapperLayout.addComponent(contactListLayout);
        wrapperLayout.addComponent(contactDetailsLayout);
        //wrapperLayout.setExpandRatio(contactListLayout, 0.5f);
        // wrapperLayout.setExpandRatio(contactDetailsLayout, 1);

        return wrapperLayout;
    }

    private VerticalLayout buildCategoriesList() {
        VerticalLayout wrapperLayout = new VerticalLayout();
        wrapperLayout.setSizeFull();
        wrapperLayout.setSpacing(false);
        wrapperLayout.setMargin(false);

        CssLayout header = new CssLayout();
        header.setWidth("100%");
        header.setHeight(50, Unit.PIXELS);
        header.addStyleNames("managecontacts-header");
        header.addStyleNames("solid-border");
        Label label = new Label("Categories");
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

        Button addContactButton = new Button("Add category");
        addContactButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        addContactButton.setEnabled(false);
        addContactButton.setIcon(MaterialIcons.PLUS_ONE);
        addContactButton.setWidth("100%");

        // Create a grid bound to the list
        grid = new Grid<>(Categories.class);
        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setColumns("title");
        grid.removeHeaderRow(0);
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


        return wrapperLayout;
    }

    public void initCategoriesList() {
        AppUI app = (AppUI) UI.getCurrent();
        String userName = app.getAccessControl().getUsername();

        if (!repository.findByUsername(userName).isEmpty()) {
            grid.setItems(repository.findByUsername(userName).get(0).getCategories());
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        resetDetailFields();
        initCategoriesList();
    }

    private VerticalLayout buildCategoryDetails() {
        VerticalLayout wrapperLayout = new VerticalLayout();
        wrapperLayout.setSizeFull();
        wrapperLayout.setSpacing(false);
        wrapperLayout.setMargin(false);

        CssLayout header = new CssLayout();
        header.setWidth("100%");
        header.setHeight(50, Unit.PIXELS);
        header.addStyleNames("addcategory-header");
        header.addStyleNames("solid-border");
        Label label = new Label("New category");
        //label.setWidth("100%");
        label.addStyleNames("headerLabel");
        //label.addStyleNames(ValoTheme.LABEL_H3);
        //label.addStyleNames(ValoTheme.LABEL_COLORED);
        header.addComponent(label);

        FormLayout detailsForm = new FormLayout();
        detailsForm.setMargin(true);
        detailsForm.setSizeFull();
        //detailsForm.addStyleNames("solid-border");



        categoryTitle = new TextField();
        categoryTitle.setWidth("100%");
        categoryTitle.setCaption("Category title");
        categoryShortcut = new TextField();
        categoryShortcut.setWidth("100%");
        categoryShortcut.setCaption("Category shortcut");
        CssLayout colorPickerLayout = new CssLayout();
        shortcutColor = new ColorPicker("Pick a color");
        shortcutColor.setHistoryVisibility(false);
        shortcutColor.setHSVVisibility(false);
        shortcutColor.setRGBVisibility(false);
        shortcutColor.setPosition(
                Page.getCurrent().getBrowserWindowWidth() / 2 - 246/2,
                Page.getCurrent().getBrowserWindowHeight() / 2 - 507/2);
        colorPickerLayout.addComponent(shortcutColor);
        colorPickerLayout.setCaption("Shortcut-color");
        categoryTags = new TextField();
        categoryTags.setCaption("Category tags ");
        categoryDescription = new TextArea();
        categoryDescription.setRows(3);
        categoryDescription.setWidth("100%");
        categoryDescription.setCaption("Category description");
        detailsForm.addComponent(categoryTitle);
        detailsForm.addComponent(categoryShortcut);
        detailsForm.addComponent(colorPickerLayout);
        detailsForm.addComponent(categoryTags);

        detailsForm.addComponent(categoryDescription);


        CssLayout bottomNav = new CssLayout();
        bottomNav.setWidth("100%");

        LayoutEvents.LayoutClickListener listener = (LayoutEvents.LayoutClickListener) layoutClickEvent -> {
            UI.getCurrent().getNavigator().navigateTo("ManageCategoriesView");

        };
        CustomButton backButton = new CustomButton(VaadinIcons.ARROW_CIRCLE_LEFT_O.getHtml() + " " + "BACK", listener);
        backButton.addStyleNames("float-left", "back-button");
        backButton.setHeight(40, Unit.PIXELS);
        backButton.setWidth(115, Unit.PIXELS);


        listener = (LayoutEvents.LayoutClickListener) layoutClickEvent -> {
            AppUI app = (AppUI) UI.getCurrent();
            String userName = app.getAccessControl().getUsername();
            Users thisUser = repository.findByUsername(userName).get(0);
            Categories category = new Categories();
            category.setTitle(categoryTitle.getValue());
            category.setShortCut(categoryShortcut.getValue());
            category.setDescription(categoryDescription.getValue());
            category.setShortCutColorCss(shortcutColor.getColor().getCSS());
            category.setShortCutColorRGB(shortcutColor.getColor().getRGB());
            thisUser.addCategory(category);
            repository.save(thisUser);
            app.getNavigator().navigateTo("ManageCategoriesView");
        };
        CustomButton createButton = new CustomButton("CREATE", listener);
        createButton.addStyleNames("float-right", "next-button");
        createButton.setHeight(40, Unit.PIXELS);
        createButton.setWidth(115, Unit.PIXELS);

        bottomNav.addComponents(backButton, createButton);

        wrapperLayout.addComponent(header);
        wrapperLayout.addComponent(detailsForm);
        wrapperLayout.addComponent(bottomNav);
        wrapperLayout.addStyleName("solid-border");
        wrapperLayout.setExpandRatio(detailsForm, 1);


        return wrapperLayout;


    }

    private void resetDetailFields() {
        categoryTitle.clear();
        categoryShortcut.clear();
        shortcutColor.setColor(Color.WHITE);
        categoryTags.clear();
        categoryDescription.clear();

    }

}

