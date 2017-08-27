package de.agdb.views.categories.assign_categories;

import com.vaadin.data.HasValue;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.dnd.DropEffect;
import com.vaadin.shared.ui.dnd.EffectAllowed;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.components.grid.GridDragSource;
import com.vaadin.ui.components.grid.GridDragStartEvent;
import com.vaadin.ui.dnd.DropTargetExtension;
import com.vaadin.ui.dnd.event.DropEvent;
import com.vaadin.ui.dnd.event.DropListener;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.shared.ui.colorpicker.Color;
import de.agdb.AppUI;
import de.agdb.backend.entities.*;
import de.agdb.backend.entities.repositories.CategoriesRepository;
import de.agdb.backend.entities.repositories.ContactRepository;
import de.agdb.backend.entities.repositories.UsersRepository;
import elemental.json.JsonArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.addons.Toastr;
import org.vaadin.addons.builder.ToastBuilder;
import org.vaadin.addons.popupextension.PopupExtension;
import org.vaadin.anna.dndscroll.PanelAutoScrollExtension;


import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


@UIScope
@SpringView(name = AssignCategoriesView.VIEW_NAME)
public class AssignCategoriesView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "AssignCategoriesView";

    Grid grid = new Grid<>(Contact.class);
    private Grid<Contact> draggedGrid;
    private Set<Contact> draggedItems;
    private List<Label> popUpViewslist;
    private PopupExtension popupExtension;
    private Label testLabel = new Label("#");
    private String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P",
            "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private String mouseOverLetter = "";
    CssLayout categoriesStripLayout = new CssLayout();
    private Label general;
    private Label unassigned;

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    CategoriesRepository categoriesRepository;

    @Autowired
    private ContactRepository contactRepository;
    private Toastr toastr = new Toastr();

    @PostConstruct
    void init() {


        addStyleNames("general-background-color-grey");
        setSizeFull();
        VerticalLayout formWrapper = new VerticalLayout();
        formWrapper.setWidth(1200, Sizeable.Unit.PIXELS);
        formWrapper.setHeight(800, Sizeable.Unit.PIXELS);
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
        content.addComponent(toastr);

    }

    public VerticalLayout buildContent() {
        VerticalLayout wrapperLayout = new VerticalLayout();
        wrapperLayout.setSizeFull();
        wrapperLayout.setSpacing(false);
        wrapperLayout.setMargin(false);


        TextField searchbar = new TextField();
        searchbar.setWidth("100%");
        searchbar.setPlaceholder("search...");
        searchbar.addValueChangeListener(this::setupGridFilter);
        searchbar.setIcon(VaadinIcons.SEARCH);
        searchbar.addStyleNames(ValoTheme.TEXTFIELD_INLINE_ICON);


        // Create a grid bound to the list

        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addStyleNames("disable-grid-cell-select");
        grid.setColumns("firstName", "lastName", "email");
        GridDragSource<Contact> dragSource = new GridDragSource<>(grid);// set the allowed effect
        dragSource.setEffectAllowed(EffectAllowed.MOVE);

        // Add drag start listener
        dragSource.addGridDragStartListener((GridDragStartEvent<Contact> event) -> {
            // Keep reference to the dragged items,
            // note that there can be only one drag at a time
            draggedItems = event.getDraggedItems();
            draggedGrid = grid;

        });

        // Add drag end listener
        dragSource.addGridDragEndListener(event -> {
            // verify that drop effect was the desired -> drop happened
            if (event.getDropEffect() == DropEffect.MOVE) {
                // inside grid reordering is handled on drop event listener,
                // which is always fired before drag end
                if (draggedGrid == null) {
                    return;
                }
                // remove items from this grid
                //items.removeAll(draggedItems);
                // grid.getDataProvider().refreshAll();

                // Remove reference to dragged items
                draggedItems = null;
                draggedGrid = null;
            }
            closePopupViewIfOpen();

            //dragSource.setDragData(null);
        });
        loadCategoriesStrip();
        wrapperLayout.addComponent(categoriesStripLayout);
        wrapperLayout.addComponent(searchbar);
        wrapperLayout.addComponent(grid);
        wrapperLayout.setExpandRatio(grid, 1);

        return wrapperLayout;
    }

    private void setupGridFilter(HasValue.ValueChangeEvent<String> event) {
        ListDataProvider<Contact> dataProvider = (ListDataProvider<Contact>) grid.getDataProvider();
        dataProvider.clearFilters();
        String filterText = event.getValue();
        dataProvider.setFilter(Contact ->
                caseInsensitiveContains(Contact.getFirstName(), filterText) ||
                        caseInsensitiveContains(Contact.getLastName(), filterText) ||
                        caseInsensitiveContains(Contact.getEmail(), filterText));
    }

    private Boolean caseInsensitiveContains(String fullText, String filterText) {
        if (fullText == null && filterText != null) {
            return false;
        } else {
            return fullText.toLowerCase().contains(filterText.toLowerCase());
        }
    }

    private void initView() {

        addStyleNames("general-background-color-grey");
        setSizeFull();
        VerticalLayout formWrapper = new VerticalLayout();
        formWrapper.setWidth(1200, Sizeable.Unit.PIXELS);
        formWrapper.setHeight(800, Sizeable.Unit.PIXELS);
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

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        loadCategoriesStrip();

        AppUI app = (AppUI) UI.getCurrent();
        String userName = app.getAccessControl().getUsername();

        if (!usersRepository.findByUsername(userName).isEmpty()) {


            Users thisUser = usersRepository.findByUsername(userName).get(0);
            grid.setItems(thisUser.getContacts());
            addJavaScriptClickListener(grid, popUpViewslist, thisUser);

        }

        addJavaScriptDragListener();

        /**
         * RESET ON VIEWCHANGE
         */
        mouseOverLetter = "";


    }


    private void handleDragData(Categories category) {
      //  Categories updatedCategory = categoriesRepository.findByTitle(category.getTitle()).get(0);

        try {
            Iterator iter = draggedItems.iterator();
            while (iter.hasNext()) {
                Contact contact = (Contact) iter.next();
                Contact updatedContact = contactRepository.findOne(contact.getId());
                updatedContact.setAssignedCategory(category);
                contactRepository.save(updatedContact);
     //           updatedCategory.addContact(contact);



            }
       //     categoriesRepository.save(updatedCategory);

            /**
             * TOASTER HIER HIN
             *
             */
            toastr.toast(ToastBuilder.success("Contacts successfully assigned").build());

        }
        catch (Exception e) {



        }


    }

    /**
     * Since Vaadin removes and adds div's automatically via Ajax,
     * you must add dragover listener to the components(letters) every time it's added to the DOM.
     *
     * @return
     */
    private void loadCategoriesStrip() {
        AppUI app = (AppUI) UI.getCurrent();
        String currentUser = app.getAccessControl().getUsername();

        if (!usersRepository.findByUsername(currentUser).isEmpty()) {
            Users user = usersRepository.findByUsername(currentUser).get(0);


            popUpViewslist = new ArrayList<>();
            categoriesStripLayout.removeAllComponents();
            categoriesStripLayout.addStyleName("categories-strip");
            categoriesStripLayout.setWidth("100%");

            HorizontalLayout horizontalLayout = new HorizontalLayout();
            horizontalLayout.setWidth("100%");

            Label topHeader = new Label("Categories:");
            topHeader.setContentMode(ContentMode.HTML);
            topHeader.addStyleName("passive-header");
            categoriesStripLayout.addComponent(topHeader);

            general = new Label("<span onclick=\"customClickListener('" + "General" + "')\")> " + "General" + "</span>");
            general.addStyleNames("active-header", "currently-selected");
            general.setContentMode(ContentMode.HTML);
            categoriesStripLayout.addComponent(general);

            unassigned = new Label("<span onclick=\"customClickListener('" + "Unassigned" + "')\")> " + "Unassigned" + "</span>");
            unassigned.setContentMode(ContentMode.HTML);
            unassigned.addStyleName("active-header");
            categoriesStripLayout.addComponent(unassigned);

            for (int i = 0; i < letters.length; i++) {

                Label label = new Label();
                label.setSizeUndefined();
                label.setContentMode(ContentMode.HTML);

                // Make label only a drop target if at least one category exists with respective first letter
                if (user.hasCategoryStartingWith(letters[i])) {
                    label.setValue("<span ondragover=\"myfunc('" + letters[i] + "')\") onclick=\"customClickListener('" + letters[i] + "')\"> " + letters[i] + "</span>");
                    DropTargetExtension<Label> dropTarget = new DropTargetExtension<>(label);
                    dropTarget.setDropEffect(DropEffect.MOVE);
                    label.addStyleName("active-letter");
                } else {
                    //label.setValue("<span onclick=\"customClickListener('" + letters[i] + "')\")> " + letters[i] + "</span>");
                    label.setValue(letters[i]);
                    label.addStyleName("passive-letter");
                }
                popUpViewslist.add(label);
                horizontalLayout.addComponent(label);
            }

            categoriesStripLayout.addComponent(horizontalLayout);
        }


    }


    private void closePopupViewIfOpen() {
        if (popupExtension != null) {
            //popupExtension.remove();

            if (popupExtension.isOpen()) {
                popupExtension.close();
                //  popupExtension.detach();
            }
        }
    }

    private Panel loadCategories(String firstLetter) {
        AppUI app = (AppUI) UI.getCurrent();
        String currentUser = app.getAccessControl().getUsername();

        Panel listPanel = new Panel();
        VerticalLayout content = new VerticalLayout();
        //content.addStyleName("solid-border-grey");
        content.setSizeUndefined();
        content.setWidth(200, Unit.PIXELS);
        Users user = usersRepository.findByUsername(currentUser).get(0);


        List<Categories> categories = user.getCategoriesStartingWith(firstLetter);
        for (int i = 0; i < categories.size(); i++) {
            final int v = i;






            CssLayout wrapperLayout = new CssLayout();
            wrapperLayout.setWidth("100%");

            Label categoryLabel = new Label(categories.get(i).getShortCut());
            categoryLabel.setWidth("100%");
            // make the label accept drops
            DropTargetExtension<Label> dropTarget = new DropTargetExtension<>(categoryLabel);
            // the drop effect must match the allowed effect in the drag source for a successful drop
            dropTarget.setDropEffect(DropEffect.MOVE);
            // catch the drops
            dropTarget.addDropListener(new DropListener<Label>() {
                @Override
                public void drop(DropEvent<Label> dropEvent) {
                    handleDragData(categories.get(v));
                }
            });

            Page.Styles styles = Page.getCurrent().getStyles();
            Color c = new Color(categories.get(i).getShortCutColorRGB());
            Color complementaryColor = new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue());
            styles.add(".headerLabel" + i + " { background-color:" + complementaryColor.getCSS() + "; " + "color: " + c.getCSS() + "; " +
                    "border: 1px solid; border-radius: 5px; padding-left: 5px; }");
            categoryLabel.setStyleName("headerLabel" + i);
            // wrapperLayout.addComponent(categoryLabel);
            // wrapperLayout.addStyleName("header");

            content.addComponent(categoryLabel);
        }

        listPanel.setContent(content);
        listPanel.setHeight(350, Unit.PIXELS);
        listPanel.setWidth(200, Unit.PIXELS);

        PanelAutoScrollExtension extension = new PanelAutoScrollExtension();
        extension.extend(listPanel);

        return listPanel;

    }

    private void addJavaScriptClickListener(Grid grid, List<Label> labelList, Users user) {
        JavaScript.getCurrent().addFunction("customClickListener", new CustomJavaScriptClickListener(grid, labelList, user, general, unassigned, usersRepository));

    }

    private void addJavaScriptDragListener() {
        JavaScript.getCurrent().addFunction("myfunc", new JavaScriptFunction() {
            @Override
            public void call(JsonArray arguments) {
                try {
                    String letter = arguments.getString(0);
                    for (int i = 0; i < letters.length; i++) {
                        if (letter.equals(letters[i])) {
                            if (!mouseOverLetter.equals(letters[i])) {
                                closePopupViewIfOpen();
                                mouseOverLetter = letters[i];
                                popupExtension = PopupExtension.extend(popUpViewslist.get(i));
                                popupExtension.setAnchor(Alignment.BOTTOM_CENTER);
                                popupExtension.setDirection(Alignment.BOTTOM_CENTER);
                                popupExtension.closeOnOutsideMouseClick(false);
                                popupExtension.setContent(loadCategories(letters[i]));
                            } else {
                                if (!popupExtension.isOpen()) {
                                    popupExtension.open();
                                }
                            }
                            break;
                        }
                    }
                } catch (Exception e) {
                    Notification.show("Error: " + e.getMessage());
                }
            }

        });
    }

}
