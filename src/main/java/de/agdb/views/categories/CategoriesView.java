package de.agdb.views.categories;


import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import de.agdb.entities.Categories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.PostConstruct;
import java.util.List;


@UIScope
@SpringView(name = CategoriesView.VIEW_NAME)

public class CategoriesView extends VerticalLayout implements View {
    public static final String VIEW_NAME = "Categories";
    private Grid<Categories> grid = new Grid<>(Categories.class);
    FormLayout form;

    @Autowired
    private JdbcTemplate jdbcTemp;


    @PostConstruct
    void init() {
        setUpLayout();

    }


    public void setUpLayout() {

        setSizeFull();
        Label section = new Label("Create a new Category");
        section.addStyleName("h3");
        section.addStyleName("colored");

        form = new FormLayout();
        form.setMargin(true);
        form.addComponent(section);


        TextField categoryName = new TextField("Title");
        categoryName.setDescription("http://");

        categoryName.setWidth("50%");
        form.addComponent(categoryName);
        TextArea categoryDescription = new TextArea("Description");
        categoryDescription.setWidth("50%");
        categoryDescription.setRows(2);
        form.addComponent(categoryDescription);

        Button addCategoryBtn = new Button("Add new category");
        addCategoryBtn.addClickListener(e -> {

            jdbcTemp.update(
                    "insert into categories (title, description) values (?, ?)",
                    categoryName.getValue(), categoryDescription.getValue());
                    updateGrid();
        });
        addCategoryBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        form.addComponent(addCategoryBtn);

        section = new Label("Currently active categories");
        section.addStyleName("h3");
        section.addStyleName("colored");
        form.addComponent(section);

        grid.setColumns("id", "title", "description");

        form.addComponent(grid);
        updateGrid();
        addComponent(form);
        setMargin(true);


    }

    private void updateGrid() {
        grid.asSingleSelect().clear();
        String sql = "SELECT * FROM categories";
        //List<Categories> categories = jdbcTemp.queryForList(sql, Categories.class);



        List<Categories> categories  = jdbcTemp.query(sql,
                new BeanPropertyRowMapper(Categories.class));


        grid.setItems(categories);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // This view is constructed in the init() method()
    }
}
