package de.agdb.views.categories.assign_categories;

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
import com.vaadin.ui.dnd.DropTargetExtension;
import de.agdb.AppUI;
import de.agdb.backend.entities.Contact;
import de.agdb.backend.entities.Users;
import de.agdb.backend.entities.UsersRepository;
import elemental.json.JsonArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.addons.popupextension.PopupExtension;


import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
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


        addJavaScriptFunction();

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

        // Create a grid bound to the list

        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        //grid.addStyleNames("disable-grid-cell-select");
        grid.setColumns("firstName", "lastName", "email");
        GridDragSource<Contact> dragSource = new GridDragSource<>(grid);// set the allowed effect
        dragSource.setEffectAllowed(EffectAllowed.MOVE);

        // Add drag start listener
        dragSource.addGridDragStartListener(event -> {
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
        wrapperLayout.addComponent(createCategoriesStrip());
        wrapperLayout.addComponent(searchbar);
        wrapperLayout.addComponent(grid);
        wrapperLayout.setExpandRatio(grid, 1);

        return wrapperLayout;
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

        AppUI app = (AppUI) UI.getCurrent();
        String userName = app.getAccessControl().getUsername();

        if (!usersRepository.findByUsername(userName).isEmpty()) {


            Users thisUser = usersRepository.findByUsername(userName).get(0);
            grid.setItems(thisUser.getContacts());
        }
        Contact c = new Contact();
        c.setFirstName("First");
        c.setLastName("Last");
        Contact b = new Contact();
        b.setFirstName("First");
        b.setLastName("Last");
        Contact a = new Contact();
        a.setFirstName("First");
        a.setLastName("Last");
        List<Contact> set = new ArrayList<>();
        set.add(a);
        set.add(c);
        set.add(b);
        grid.setItems(set);
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
    private CssLayout createCategoriesStrip() {
        popUpViewslist = new ArrayList<>();
        CssLayout horizontalWrapper = new CssLayout();
        horizontalWrapper.setWidth("100%");
        //horizontalWrapper.setSpacing(false);
        //horizontalWrapper.setMargin(false);
        // Label c = new Label("Categories");
        // c.setSizeUndefined();
        HorizontalLayout verticalLayout = new HorizontalLayout();
        verticalLayout.setWidth("100%");

        horizontalWrapper.addComponents(new Label("CATEGORIES:"), new Label("General"), new Label("Unassigned"));
        //horizontalWrapper.addComponent(c);
        for (int i = 0; i < letters.length; i++) {


            Label label = new Label();
            label.setSizeUndefined();
            label.setContentMode(ContentMode.HTML);
            label.setValue("<span ondragover=\"myfunc('" + letters[i] + "',1)\")> " + letters[i] + "</span>");
//            label.setValue("<span ondragover=\"myfunc('" + letters[i] + "')\")> " + letters[i] + "</span>");

            // make the label accept drops
            DropTargetExtension<Label> dropTarget = new DropTargetExtension<>(label);

// the drop effect must match the allowed effect in the drag source for a successful drop
            dropTarget.setDropEffect(DropEffect.MOVE);


           /* PopupExtension extension;
            ListSelect listSelect = new ListSelect();
            listSelect.setData("Test");
            listSelect.setWidth(300, Unit.PIXELS);
            listSelect.setHeight(300, Unit.PIXELS);
            extension = PopupExtension.extend(label);
            extension.setContent(listSelect);
            extension.setAnchor(Alignment.BOTTOM_CENTER);
            extension.setDirection(Alignment.BOTTOM_CENTER);
            extension.closeOnOutsideMouseClick(false);*/
            popUpViewslist.add(label);


            verticalLayout.addComponent(label);
        }
        horizontalWrapper.addComponent(verticalLayout);
        return horizontalWrapper;


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

    private void addJavaScriptFunction() {
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
                            Thread.sleep(500);
                            popupExtension = PopupExtension.extend(popUpViewslist.get(0));
                            popupExtension.setAnchor(Alignment.BOTTOM_CENTER);
                            popupExtension.setDirection(Alignment.BOTTOM_CENTER);
                            popupExtension.closeOnOutsideMouseClick(false);
                            popupExtension.setContent(new Label("A"));

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
                            popupExtension.setContent(new Label("B"));

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
                            popupExtension.setContent(new Label("C"));

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
                            popupExtension.setContent(new Label("D"));

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
                            popupExtension.setContent(new Label("E"));

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
                            popupExtension.setContent(new Label("F"));

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
