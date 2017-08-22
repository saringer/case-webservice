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
    private CategoriesRepository categoriesRepository;
    private UsersRepository usersRepository;

    public CustomJavaScriptClickListener(Grid grid, List<Label> labelList, Users user, Label general, Label unassigned, CategoriesRepository categoriesRepository,
                                         UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
        this.categoriesRepository = categoriesRepository;
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
                /*
                Ensure that we work on an up to date object
                 */
                Categories updatedCategory = categoriesRepository.findByTitle(category.getTitle()).get(0);
                grid.setItems(updatedCategory.getContacts());
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
                    Contact contact = updatedUser.getContacts().get(i);
                    boolean flag = true;

                    for (int x = 0; x < updatedUser.getCategories().size(); x++) {
                        for (int y = 0; y < updatedUser.getCategories().get(x).getContacts().size(); y++) {
                            if (contact.getEmail().equals(updatedUser.getCategories().get(x).getContacts().get(y).getEmail())) {
                                flag = false;
                                break;
                            }
                        }
                    }
                    if (flag) {
                        unassignedContacts.add(contact);
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
