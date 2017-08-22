package de.agdb.views.categories.manage_categories;

import com.vaadin.data.HasValue;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.event.LayoutEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;

import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.shared.ui.colorpicker.Color;
import com.vaadin.v7.ui.ColorPicker;
import de.agdb.AppUI;
import de.agdb.backend.entities.Categories;
import de.agdb.backend.entities.Contact;
import de.agdb.backend.entities.repositories.UsersRepository;
import de.agdb.views.scheduler.CustomButton;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.alump.materialicons.MaterialIcons;

import javax.annotation.PostConstruct;


@UIScope
@SpringView(name = ManageCategoriesView.VIEW_NAME)
public class ManageCategoriesView extends VerticalLayout implements View, ViewChangeListener {


    public static final String VIEW_NAME = "ManageCategoriesView";
    private Grid<Categories> grid = new Grid<>(Categories.class);
    private TextField categoryTitle;
    private TextField categoryShortcut;
    private ColorPicker categoryShortcutColor;
    private TextField categoryTags;
    private TextArea categoryDescription;
    private Label categoryDetailsLabel;
    private CssLayout detailsHeaderLayout;
    private CustomButton userProfileButton;


    @Autowired
    UsersRepository repository;


    @PostConstruct
    void init() {

        /* Listener added to ensure that the ColorPicker PopUp-Window will be closed on view change.
        *  TODO: Implement a custom colorpicker or wait for the fixed vaadin 8 colorpicker
        */
        UI.getCurrent().getNavigator().addViewChangeListener(this);

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

        VerticalLayout categoryListLayout = buildCategoriesList();
        VerticalLayout categoryDetailsLayout = buildCategoryDetails();

        wrapperLayout.addComponent(categoryListLayout);
        wrapperLayout.addComponent(categoryDetailsLayout);

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
        searchField.setPlaceholder("search...");
        searchField.setIcon(VaadinIcons.SEARCH);
        searchField.addStyleNames(ValoTheme.TEXTFIELD_INLINE_ICON);
        searchField.setWidth("100%");
        searchField.addValueChangeListener(this::setupGridFilter);


        Button addCategoryButton = new Button("Add category");
        addCategoryButton.addClickListener((Button.ClickListener) clickEvent -> {
            UI.getCurrent().getNavigator().navigateTo("AddCategoryView");
        });
        addCategoryButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        addCategoryButton.setIcon(MaterialIcons.PLUS_ONE);
        addCategoryButton.setWidth("100%");

        // Create a grid bound to the list


        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);

        grid.setColumns("title");
        grid.removeHeaderRow(0);

        //wrapperLayout.addStyleName("disable-grid-cell-select");
        wrapperLayout.addComponent(header);
        wrapperLayout.addComponent(searchField);
        wrapperLayout.addComponent(addCategoryButton);
        wrapperLayout.addComponent(grid);
        wrapperLayout.addStyleName("solid-border");
        wrapperLayout.setExpandRatio(grid, 1f);


        return wrapperLayout;
    }

    public void initCategoriesList() {
        AppUI app = (AppUI) UI.getCurrent();
        String userName = app.getAccessControl().getUsername();
        grid.deselectAll();

        if (!repository.findByUsername(userName).isEmpty()) {
            grid.setItems(repository.findByUsername(userName).get(0).getCategories());
            grid.getSelectionModel().addSelectionListener(event -> {


                boolean somethingSelected = !grid.getSelectedItems().isEmpty();
                if (somethingSelected) {
                    Categories category = event.getFirstSelectedItem().get();
                    categoryDetailsLabel.setValue(category.getTitle());
                    categoryTitle.setValue(category.getTitle());
                    categoryShortcut.setValue(category.getShortCut());
                    categoryShortcutColor.setColor(new Color(category.getShortCutColorRGB()));
                    categoryDescription.setValue(category.getDescription());

                    // Get the stylesheet of the page
                    Page.Styles styles = Page.getCurrent().getStyles();
                    // inject the new color as a style
                    Color c = new Color(category.getShortCutColorRGB());
                    Color complementaryColor = new Color(255-c.getRed(),255-c.getGreen(),255-c.getBlue());
                    styles.add(".headerLabel2 { background-color:" + complementaryColor.getCSS() + "; padding-top: 7px; " +
                            "padding-left:20px; color: "+ c.getCSS() + "; border-bottom: 1px solid black; }");

                    detailsHeaderLayout.setStyleName("headerLabel2");


                }

            });


        }
    }

    private void setupGridFilter(HasValue.ValueChangeEvent<String> event) {
       /* ListDataProvider<Contact> dataProvider = (ListDataProvider<Contact>) grid.getDataProvider();
        dataProvider.clearFilters();
        String filterText = event.getValue();
        dataProvider.setFilter(Contact ->
                caseInsensitiveContains(Contact.getFirstName(), filterText) ||
                        caseInsensitiveContains(Contact.getLastName(), filterText) ||
                        caseInsensitiveContains(Contact.getEmail(), filterText));*/
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

        initCategoriesList();
    }

    private VerticalLayout buildCategoryDetails() {
        VerticalLayout wrapperLayout = new VerticalLayout();
        wrapperLayout.setSizeFull();
        wrapperLayout.setSpacing(false);
        wrapperLayout.setMargin(false);

        detailsHeaderLayout = new CssLayout();
        detailsHeaderLayout.setWidth("100%");
        detailsHeaderLayout.setHeight(50, Unit.PIXELS);
        detailsHeaderLayout.addStyleNames("managecontacts-header");
        detailsHeaderLayout.addStyleNames("solid-border");
        categoryDetailsLabel = new Label("");
        //label.setWidth("100%");
        categoryDetailsLabel.addStyleNames("headerLabel");
        //label.addStyleNames(ValoTheme.LABEL_H3);
        //label.addStyleNames(ValoTheme.LABEL_COLORED);
        detailsHeaderLayout.addComponent(categoryDetailsLabel);

        FormLayout detailsForm = new FormLayout();
        detailsForm.setMargin(true);
        detailsForm.setSizeFull();

        categoryTitle = new TextField();
        categoryTitle.setReadOnly(true);
        categoryTitle.setEnabled(false);
        categoryTitle.setWidth("100%");
        categoryTitle.setCaption("Category title");
        categoryShortcut = new TextField();
        categoryShortcut.setEnabled(false);
        categoryShortcut.setReadOnly(true);
        categoryShortcut.setWidth("100%");
        categoryShortcut.setCaption("Category shortcut");
        CssLayout colorPickerLayout = new CssLayout();
        categoryShortcutColor = new ColorPicker("Pick a color");
        categoryShortcutColor.setPosition(
                Page.getCurrent().getBrowserWindowWidth() / 2 - 246 / 2,
                Page.getCurrent().getBrowserWindowHeight() / 2 - 507 / 2);
        categoryShortcutColor.setHistoryVisibility(false);
        categoryShortcutColor.setHSVVisibility(false);
        categoryShortcutColor.setRGBVisibility(false);
        colorPickerLayout.addComponent(categoryShortcutColor);
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
        //detailsForm.addComponent(categoryTags);

        detailsForm.addComponent(categoryDescription);


        CssLayout bottomNav = new CssLayout();
        bottomNav.setWidth("100%");

        LayoutEvents.LayoutClickListener listener = (LayoutEvents.LayoutClickListener) layoutClickEvent -> {

        };
        CustomButton deleteButton = new CustomButton(VaadinIcons.TRASH.getHtml() + " " + "DELETE", listener);
        deleteButton.addStyleNames("cancel-button", "float-right");
        deleteButton.setHeight(40, Unit.PIXELS);
        deleteButton.setWidth(115, Unit.PIXELS);

        listener = (LayoutEvents.LayoutClickListener) layoutClickEvent -> {

        };
        CustomButton saveButton = new CustomButton(VaadinIcons.SERVER.getHtml() + " " + "SAVE", listener);
        saveButton.addStyleNames("float-right", "save-button");
        saveButton.setHeight(40, Unit.PIXELS);
        saveButton.setWidth(115, Unit.PIXELS);


        bottomNav.addComponents(saveButton, deleteButton);


        wrapperLayout.addComponent(detailsHeaderLayout);
        wrapperLayout.addComponent(detailsForm);
        wrapperLayout.addComponent(bottomNav);
        wrapperLayout.addStyleName("solid-border");

        wrapperLayout.setExpandRatio(detailsForm, 1);


        return wrapperLayout;


    }

    private void generateDynamicCSS() {

    }

    @Override
    public boolean beforeViewChange(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        if (categoryShortcutColor != null) {
            categoryShortcutColor.hidePopup();
        }
        return true;
    }

}
