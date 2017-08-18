package de.agdb.views.categories.assign_categories;

import com.vaadin.data.HasValue;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
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
import de.agdb.AppUI;
import de.agdb.backend.entities.Categories;
import de.agdb.backend.entities.Contact;
import de.agdb.backend.entities.Users;
import de.agdb.backend.entities.UsersRepository;
import elemental.json.JsonArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.addons.popupextension.PopupExtension;
import org.vaadin.anna.dndscroll.PanelAutoScrollExtension;


import javax.annotation.PostConstruct;
import java.util.ArrayList;
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

    @Autowired
    UsersRepository usersRepository;

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


    }

    public VerticalLayout buildContent() {
        VerticalLayout wrapperLayout = new VerticalLayout();
        wrapperLayout.setSizeFull();
        wrapperLayout.setSpacing(false);
        wrapperLayout.setMargin(false);

        CssLayout cssLayout = dropLayout();
        cssLayout.setWidth("100%");
        Button test = new Button("Categories:");
        test.setSizeUndefined();
        //cssLayout.addComponent(createCategoriesStrip());


        test.addStyleNames("assign-category-header");
        test.setSizeUndefined();
        cssLayout.addComponent(test);

        TextField searchbar = new TextField();
        searchbar.setWidth("100%");
        searchbar.setPlaceholder("search...");
        searchbar.addValueChangeListener(this::setupGridFilter);


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

    private CssLayout dropLayout() {
        CssLayout dropTargetLayout = new CssLayout();

        // make the layout accept drops
        DropTargetExtension<CssLayout> dropTarget = new DropTargetExtension<>(dropTargetLayout);

// the drop effect must match the allowed effect in the drag source for a successful drop
        dropTarget.setDropEffect(DropEffect.MOVE);


// catch the drops
        dropTarget.addDropListener(event -> {
            Notification.show("TEst");
            handleMyDragData(event.getMouseEventDetails().getClientX(), event.getMouseEventDetails().getClientY());
        });

        return dropTargetLayout;
    }

    private void handleMyDragData(int x, int y) {
        Contact data = draggedItems.iterator().next();

        Window window = new Window();
        window.setModal(true);
        window.center();
        window.setResizable(false);
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.addComponent(new Label(data.getFirstName() + " " + data.getLastName()));
        window.setContent(verticalLayout);
        window.setPosition(x, y);

        UI.getCurrent().addWindow(window);

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

            topHeader = new Label("General");
            topHeader.addStyleName("active-header");
            categoriesStripLayout.addComponent(topHeader);

            topHeader = new Label("Unassigned");
            topHeader.addStyleName("active-header");
            categoriesStripLayout.addComponent(topHeader);

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
                    label.setValue("<span onclick=\"customClickListener('" + letters[i] + "')\")> " + letters[i] + "</span>");
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
            content.addComponent(new Label(categories.get(i).getShortCut()));
        }

        listPanel.setContent(content);
        listPanel.setHeight(350, Unit.PIXELS);
        listPanel.setWidth(200, Unit.PIXELS);

        PanelAutoScrollExtension extension = new PanelAutoScrollExtension();
        extension.extend(listPanel);

        return listPanel;

    }

    /**
     * LISTENERS FOR CATEGORY-POPUPVIEWS
     * TODO: Clean up
     */

    private void addJavaScriptClickListener(Grid grid, List<Label> labelList, Users user) {
        JavaScript.getCurrent().addFunction("customClickListener", new CustomJavaScriptClickListener(grid, labelList, user));

    }

    private void addJavaScriptDragListener() {
        JavaScript.getCurrent().addFunction("myfunc", new JavaScriptFunction() {
            @Override
            public void call(JsonArray arguments) {
                try {
                    //System.out.println("aha");
                    //double c = arguments.getNumber(0);
                    String letter = arguments.getString(0);
                    //System.out.println(list);

                    if (letter.equals("A")) {
                        if (!mouseOverLetter.equals("A")) {
                            closePopupViewIfOpen();
                            mouseOverLetter = "A";
                            popupExtension = PopupExtension.extend(popUpViewslist.get(0));
                            popupExtension.setAnchor(Alignment.BOTTOM_CENTER);
                            popupExtension.setDirection(Alignment.BOTTOM_CENTER);
                            popupExtension.closeOnOutsideMouseClick(false);
                            popupExtension.setContent(loadCategories("A"));

                        } else {
                            //popupExtension = popUpViewslist.get(0);
                            if (!popupExtension.isOpen()) {
                                popupExtension.open();
                            }
                            //  popupExtension.close();
                            // popupExtension.open();
                        }

                    }
                    if (letter.equals("B")) {
                        if (!mouseOverLetter.equals("B")) {
                            closePopupViewIfOpen();
                            mouseOverLetter = "B";
                            popupExtension = PopupExtension.extend(popUpViewslist.get(1));
                            popupExtension.setAnchor(Alignment.BOTTOM_CENTER);
                            popupExtension.setDirection(Alignment.BOTTOM_CENTER);
                            popupExtension.closeOnOutsideMouseClick(false);
                            popupExtension.setContent(loadCategories("B"));

                        } else {
                            //popupExtension = popUpViewslist.get(0);
                            if (!popupExtension.isOpen())
                                popupExtension.open();
                        }

                    }

                    if (letter.equals("C")) {
                        if (!mouseOverLetter.equals("C")) {
                            closePopupViewIfOpen();
                            mouseOverLetter = "C";
                            popupExtension = PopupExtension.extend(popUpViewslist.get(2));
                            popupExtension.setAnchor(Alignment.BOTTOM_CENTER);
                            popupExtension.setDirection(Alignment.BOTTOM_CENTER);
                            popupExtension.closeOnOutsideMouseClick(false);
                            popupExtension.setContent(loadCategories("C"));

                        } else {
                            //popupExtension = popUpViewslist.get(0);
                            if (!popupExtension.isOpen())
                                popupExtension.open();
                        }

                    }


                    if (letter.equals("D")) {
                        if (!mouseOverLetter.equals("D")) {
                            closePopupViewIfOpen();
                            mouseOverLetter = "D";
                            popupExtension = PopupExtension.extend(popUpViewslist.get(3));
                            popupExtension.setAnchor(Alignment.BOTTOM_CENTER);
                            popupExtension.setDirection(Alignment.BOTTOM_CENTER);
                            popupExtension.closeOnOutsideMouseClick(false);
                            popupExtension.setContent(loadCategories("D"));

                        } else {
                            //popupExtension = popUpViewslist.get(0);
                            if (!popupExtension.isOpen())
                                popupExtension.open();
                        }

                    }


                    if (letter.equals("E")) {
                        if (!mouseOverLetter.equals("E")) {
                            closePopupViewIfOpen();
                            mouseOverLetter = "E";
                            popupExtension = PopupExtension.extend(popUpViewslist.get(4));
                            popupExtension.setAnchor(Alignment.BOTTOM_CENTER);
                            popupExtension.setDirection(Alignment.BOTTOM_CENTER);
                            popupExtension.closeOnOutsideMouseClick(false);
                            popupExtension.setContent(loadCategories("E"));

                        } else {
                            //popupExtension = popUpViewslist.get(0);
                            if (!popupExtension.isOpen())
                                popupExtension.open();
                        }

                    }


                    if (letter.equals("F")) {
                        if (!mouseOverLetter.equals("F")) {
                            closePopupViewIfOpen();
                            mouseOverLetter = "F";
                            popupExtension = PopupExtension.extend(popUpViewslist.get(5));
                            popupExtension.setAnchor(Alignment.BOTTOM_CENTER);
                            popupExtension.setDirection(Alignment.BOTTOM_CENTER);
                            popupExtension.closeOnOutsideMouseClick(false);
                            popupExtension.setContent(loadCategories("F"));

                        } else {
                            //popupExtension = popUpViewslist.get(0);
                            if (!popupExtension.isOpen())
                                popupExtension.open();
                        }

                    }


                    if (letter.equals("G")) {
                        if (!mouseOverLetter.equals("G")) {
                            closePopupViewIfOpen();
                            mouseOverLetter = "G";
                            popupExtension = PopupExtension.extend(popUpViewslist.get(6));
                            popupExtension.setAnchor(Alignment.BOTTOM_CENTER);
                            popupExtension.setDirection(Alignment.BOTTOM_CENTER);
                            popupExtension.closeOnOutsideMouseClick(false);
                            popupExtension.setContent(loadCategories("G"));

                        } else {
                            //popupExtension = popUpViewslist.get(0);
                            if (!popupExtension.isOpen())
                                popupExtension.open();
                        }

                    }

                    if (letter.equals("H")) {
                        if (!mouseOverLetter.equals("H")) {
                            closePopupViewIfOpen();
                            mouseOverLetter = "H";
                            popupExtension = PopupExtension.extend(popUpViewslist.get(7));
                            popupExtension.setAnchor(Alignment.BOTTOM_CENTER);
                            popupExtension.setDirection(Alignment.BOTTOM_CENTER);
                            popupExtension.closeOnOutsideMouseClick(false);
                            popupExtension.setContent(loadCategories("H"));

                        } else {
                            //popupExtension = popUpViewslist.get(0);
                            if (!popupExtension.isOpen())
                                popupExtension.open();
                        }

                    }

                    if (letter.equals("I")) {
                        if (!mouseOverLetter.equals("I")) {
                            closePopupViewIfOpen();
                            mouseOverLetter = "I";
                            popupExtension = PopupExtension.extend(popUpViewslist.get(8));
                            popupExtension.setAnchor(Alignment.BOTTOM_CENTER);
                            popupExtension.setDirection(Alignment.BOTTOM_CENTER);
                            popupExtension.closeOnOutsideMouseClick(false);
                            popupExtension.setContent(loadCategories("I"));

                        } else {
                            //popupExtension = popUpViewslist.get(0);
                            if (!popupExtension.isOpen())
                                popupExtension.open();
                        }

                    }


                } catch (Exception e) {
                    Notification.show("Error: " + e.getMessage());
                }
            }

        });
    }

}
