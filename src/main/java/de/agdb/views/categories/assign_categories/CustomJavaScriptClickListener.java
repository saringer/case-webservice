package de.agdb.views.categories.assign_categories;

import com.vaadin.data.HasValue;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import de.agdb.backend.entities.Categories;
import de.agdb.backend.entities.Contact;
import de.agdb.backend.entities.Users;
import elemental.json.JsonArray;
import org.vaadin.addons.popupextension.PopupExtension;

import java.util.List;


public class CustomJavaScriptClickListener implements JavaScriptFunction {
    Grid grid;
    Grid categoriesGrid = new Grid<>(Categories.class);
    List<Label> labelList;
    PopupExtension popupExtension;
    Window window = new Window();
    Users user;

    public CustomJavaScriptClickListener(Grid grid, List<Label> labelList, Users user) {
        this.labelList = labelList;
        this.grid = grid;
        this.user = user;

        categoriesGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        categoriesGrid.setSizeFull();
        categoriesGrid.setColumns("title");
        categoriesGrid.removeHeaderRow(0);

        categoriesGrid.setItems(user.getCategories());

        window.center();
        window.setResizable(false);
        window.setContent(categoriesGrid);
        window.setWidth(200, Sizeable.Unit.PIXELS);
        window.setHeight(300, Sizeable.Unit.PIXELS);
        window.setModal(true);
        window.setCaption("Sort contacts by:");

    }

    private void setupGridFilter(String categoryPrefix) {
        ListDataProvider<Categories> dataProvider = (ListDataProvider<Categories>) categoriesGrid.getDataProvider();
        dataProvider.clearFilters();
        dataProvider.setFilter(Category ->
                test(Category.getTitle(), categoryPrefix)
        );
    }

    private boolean test(String categoryTitle, String filterText) {
        if (categoryTitle.substring(0,1).equalsIgnoreCase(filterText)) {
            return true;

        }
        else {
            return false;
        }
    }

    @Override
    public void call(JsonArray jsonArray) {
        try {
            String letter = jsonArray.getString(0);

            if (letter.equals("A")) {
                setupGridFilter("A");
                UI.getCurrent().addWindow(window);
            }
            if (letter.equals("B")) {
                setupGridFilter("B");
                UI.getCurrent().addWindow(window);
            }
        } catch (Exception e) {
            Notification.show(e.getLocalizedMessage());
        }
    }
}
