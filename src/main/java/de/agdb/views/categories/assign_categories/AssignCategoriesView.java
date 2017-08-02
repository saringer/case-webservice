package de.agdb.views.categories.assign_categories;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.dnd.DropEffect;
import com.vaadin.shared.ui.dnd.EffectAllowed;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.components.grid.GridDragSource;
import com.vaadin.ui.dnd.DragSourceExtension;
import com.vaadin.ui.dnd.DropTargetExtension;
import com.vaadin.ui.dnd.event.DragStartEvent;
import com.vaadin.ui.dnd.event.DragStartListener;
import com.vaadin.ui.themes.ValoTheme;
import de.agdb.AppUI;
import de.agdb.backend.entities.Contact;
import de.agdb.backend.entities.Users;
import de.agdb.backend.entities.UsersRepository;
import de.agdb.views.categories.manage_categories.ManageCategoriesView;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.Set;

@UIScope
@SpringView(name = AssignCategoriesView.VIEW_NAME)
public class AssignCategoriesView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "AssignCategoriesView";
    Grid grid = new Grid<>(Contact.class);
    private Grid<Contact> draggedGrid;
    private Set<Contact> draggedItems;

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
        Label test = new Label("Categories:");
        test.addStyleNames("assign-category-header");
        test.setSizeUndefined();
        cssLayout.addComponent(test);

        TextField searchbar = new TextField();
        searchbar.setWidth("100%");

        // Create a grid bound to the list

        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
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
            //dragSource.setDragData(null);
        });

        wrapperLayout.addComponent(cssLayout);
        wrapperLayout.addComponent(searchbar);
        wrapperLayout.addComponent(grid);
        wrapperLayout.setExpandRatio(grid, 1);

        return wrapperLayout;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

        AppUI app = (AppUI) UI.getCurrent();
        String userName = app.getAccessControl().getUsername();

        if (!usersRepository.findByUsername(userName).isEmpty()) {


            Users thisUser = usersRepository.findByUsername(userName).get(0);
            grid.setItems(thisUser.getContacts());
        }

    }

    private CssLayout dropLayout() {
        CssLayout dropTargetLayout = new CssLayout();

        // make the layout accept drops
        DropTargetExtension<CssLayout> dropTarget = new DropTargetExtension<>(dropTargetLayout);

// the drop effect must match the allowed effect in the drag source for a successful drop
        dropTarget.setDropEffect(DropEffect.MOVE);

// catch the drops
        dropTarget.addDropListener(event -> {
            // if the drag source is in the same UI as the target
            /*Optional<AbstractComponent> dragSource = event.getDragSourceComponent();
            if (dragSource.isPresent()) {
                // move the label to the layout
                dropTargetLayout.addComponent(dragSource.get());

                // get possible transfer data
                String message = event.getDataTransferText();
                if (message != null) {
                    Notification.show("DropEvent with data transfer html: " + message);
                } else {
                    // get transfer text
                    message = event.getDataTransferText();
                    Notification.show("DropEvent with data transfer text: " + message);
                }
                //  event.getDragData().ifPresent();
                // handle possible server side drag data, if the drag source was in the same UI
                event.getDragData().ifPresent(data -> handleMyDragData((Contact) data));
            }*/

            handleMyDragData(event.getMouseEventDetails().getClientX(), event.getMouseEventDetails().getClientY());
        });

        return  dropTargetLayout;
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
}
