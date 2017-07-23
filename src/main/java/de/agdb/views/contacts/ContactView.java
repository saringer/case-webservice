package de.agdb.views.contacts;


import com.vaadin.event.LayoutEvents;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.ValueChangeMode;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ComponentRenderer;
import com.vaadin.ui.themes.ValoTheme;
import de.agdb.backend.entities.Categories;
import de.agdb.backend.entities.Contacts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.PostConstruct;
import java.util.List;


@SpringView(name = ContactView.VIEW_NAME)
@ViewScope
public class ContactView extends VerticalLayout implements View {

    @Autowired
    JdbcTemplate jdbcTemplate;
    public static final String VIEW_NAME = "Contacts";

    private ContactService service = ContactService.getInstance();
    private Grid<Contacts> grid = new Grid<>(Contacts.class);
    private int counter;


    private TextField filterText = new TextField();
    private ContactForm form = new ContactForm(this);
    private Contacts placeHolder;


    @PostConstruct
    void init() {


        CssLayout horizontalLayout = new CssLayout();
        updateCategories(horizontalLayout);


        setSizeFull();
        final VerticalLayout layout = new VerticalLayout();
        Button test = new Button();


        filterText.setPlaceholder("filter by name...");
        filterText.addValueChangeListener(e -> updateList());
        filterText.setValueChangeMode(ValueChangeMode.LAZY);

        Button clearFilterTextBtn = new Button(FontAwesome.TIMES);
        clearFilterTextBtn.setDescription("Clear the current filter");
        clearFilterTextBtn.addClickListener(e -> filterText.clear());

        CssLayout filtering = new CssLayout();
        filtering.addComponents(filterText, clearFilterTextBtn);
        filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

        Button addContactBtn = new Button("Add new contact");
        addContactBtn.addClickListener(e -> {
            grid.asSingleSelect().clear();
            form.setContacts(new Contacts());
        });
        addContactBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);

        Button resetBtn = new Button("Reset List");
        resetBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                    jdbcTemplate.execute("DELETE FROM contacts");
                    updateList();
            }
        });


        HorizontalLayout toolbar = new HorizontalLayout(filtering, addContactBtn, resetBtn);

        grid.setColumns("firstName", "lastName", "email", "status");

        HorizontalLayout main = new HorizontalLayout(grid, form);
        main.setSizeFull();
        grid.setSizeFull();

        main.setExpandRatio(grid, 1);


        layout.addComponents(toolbar, main);

        // fetch list of Customers from service and assign it to Grid
        updateList();


        addComponent(horizontalLayout);
        addComponent(layout);
        setExpandRatio(horizontalLayout, 0.3f);
        setExpandRatio(layout, 0.7f);


        form.setVisible(false);

        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() == null) {
                form.setVisible(false);
            } else {
                form.setContacts(event.getValue());
            }
        });

        grid.addColumn(contact -> {
            Button button = new Button("Drag Row here!");
            button.setSizeFull();
            button.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
            button.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    service.delete(contact);
                    updateList();
                }
            });




            return button;
        }, new ComponentRenderer());


    }


    public void updateList() {
        grid.asSingleSelect().clear();


        String sql = "SELECT * FROM contacts";
        List<Contacts> contactsList = jdbcTemplate.query(sql,
                new BeanPropertyRowMapper(Contacts.class));

        if (contactsList.size() != 0) {
            grid.setItems(contactsList);
        } else {


            List<Contacts> contactss = service.findAll(filterText.getValue());
            grid.setItems(contactss);

        }





    }

    public void updateCategories(CssLayout layout) {

        layout.setSizeFull();
        layout.addStyleName("categoriesWrapper");

        String sql = "SELECT * FROM categories";
        //List<Categories> categories = jdbcTemp.queryForList(sql, Categories.class);
        List<Categories> categories = jdbcTemplate.query(sql,
                new BeanPropertyRowMapper(Categories.class));

        CssLayout generalCategory = new CssLayout();
        generalCategory.addComponent(new Label("No Status Assigned"));
        generalCategory.addStyleName("generalCategory");
        layout.addComponent(generalCategory);
        generalCategory.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent layoutClickEvent) {
                filterText.setValue("No Status");
                updateList();

            }
        });


        for (counter = 0; counter < categories.size(); counter++) {
            CssLayout category = new CssLayout();
            String statusTitle = categories.get(counter).getTitle();

            // make the layout accept drops


            category.addComponent(new Label(categories.get(counter).getTitle()));
            category.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
                @Override
                public void layoutClick(LayoutEvents.LayoutClickEvent layoutClickEvent) {
                    filterText.setValue(statusTitle);
                    updateList();
                }
            });


            category.addStyleName("category");

            layout.addComponent(category);
        }


    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }


}
