package de.agdb.views.categories.assign_categories;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.server.Page;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import com.vaadin.v7.shared.ui.colorpicker.Color;
import de.agdb.backend.entities.Categories;
import de.agdb.backend.entities.Contact;
import de.agdb.backend.entities.Users;
import de.agdb.backend.entities.repositories.CategoriesRepository;
import de.agdb.backend.entities.repositories.ContactRepository;
import de.agdb.backend.entities.repositories.UsersRepository;
import elemental.json.JsonArray;
import org.vaadin.addons.popupextension.PopupExtension;

import java.util.ArrayList;
import java.util.List;


public class CustomJavaScriptClickListener implements JavaScriptFunction {
    Grid grid;
    Grid categoriesGrid = new Grid<>(Categories.class);
    List<Label> labelList;
    PopupExtension popupExtension;
    Window window = new Window();
    Users user;
    Label general;
    Label unassigned;
    private Label selectedLabel;
    private String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P",
            "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private UsersRepository usersRepository;

    public CustomJavaScriptClickListener(Grid grid, List<Label> labelList, Users user, Label general, Label unassigned,
                                         UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
        this.labelList = labelList;
        this.grid = grid;
        this.user = user;
        this.general = general;
        this.unassigned = unassigned;
        this.selectedLabel = general;

        categoriesGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        categoriesGrid.setSizeFull();
        categoriesGrid.setColumns("title");
        categoriesGrid.removeHeaderRow(0);

        categoriesGrid.setItems(user.getCategories());
        categoriesGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        categoriesGrid.getSelectionModel().addSelectionListener(event -> {
            boolean somethingSelected = !categoriesGrid.getSelectedItems().isEmpty();
            if (somethingSelected) {
                Categories category = (Categories) event.getFirstSelectedItem().get();

                List<Contact> assignedContacts = new ArrayList<>();
                Users updatedUser = usersRepository.findByUsername(user.getUsername()).get(0);

                for (int i = 0; i < updatedUser.getContacts().size(); i++) {
                    if (updatedUser.getContacts().get(i).getAssignedCategory() != null) {
                        if (updatedUser.getContacts().get(i).getAssignedCategory().getTitle().equals(category.getTitle())) {
                                        assignedContacts.add(updatedUser.getContacts().get(i));
                        }

                    }
                }
                grid.setItems(assignedContacts);
                window.close();
            }
        });

        window.center();
        window.setResizable(false);
        window.setClosable(false);
        window.setContent(categoriesGrid);
        window.setWidth(200, Sizeable.Unit.PIXELS);
        window.setHeight(300, Sizeable.Unit.PIXELS);
        window.setModal(true);
        window.setCaption("Sort contacts by:");

    }

    private void setLabelAsSelected(Label label) {
        selectedLabel.removeStyleName("currently-selected");
        label.addStyleName("currently-selected");
        selectedLabel = label;
    }

    private void setupGridFilter(String categoryPrefix) {
        ListDataProvider<Categories> dataProvider = (ListDataProvider<Categories>) categoriesGrid.getDataProvider();
        dataProvider.clearFilters();
        dataProvider.setFilter(Category ->
                search(Category.getTitle(), categoryPrefix)
        );
    }

    private boolean search(String categoryTitle, String filterText) {
        if (categoryTitle.substring(0, 1).equalsIgnoreCase(filterText)) {
            return true;

        } else {
            return false;
        }
    }

    @Override
    public void call(JsonArray jsonArray) {
        try {
            String letter = jsonArray.getString(0);

            if (letter.equals("General")) {
                setLabelAsSelected(general);
                grid.setItems(user.getContacts());
            }

            if (letter.equals("Unassigned")) {
                setLabelAsSelected(unassigned);
                List<Contact> unassignedContacts = new ArrayList<>();
                Users updatedUser = usersRepository.findByUsername(user.getUsername()).get(0);
                for (int i = 0; i < updatedUser.getContacts().size(); i++) {
                    if (updatedUser.getContacts().get(i).getAssignedCategory() == null) {
                        unassignedContacts.add(updatedUser.getContacts().get(i));
                    }
                }
                grid.setItems(unassignedContacts);

            }

            for (int i = 0; i < letters.length; i++) {
                if (letter.equalsIgnoreCase(letters[i])) {
                    setLabelAsSelected(labelList.get(i));
                    setupGridFilter(letters[i]);
                    UI.getCurrent().addWindow(window);
                    break;
                }
            }

          /*  if (letter.equals("A")) {

            }
            if (letter.equals("B")) {
                setupGridFilter("B");
                UI.getCurrent().addWindow(window);
            }*/
        } catch (Exception e) {
            Notification.show(e.getLocalizedMessage());
        }
    }
}
