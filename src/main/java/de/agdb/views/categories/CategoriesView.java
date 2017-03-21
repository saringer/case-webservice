package de.agdb.views.categories;


import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;


public class CategoriesView extends VerticalLayout implements View {
    public static final String VIEW_NAME = "Categories";


    public CategoriesView() {
        setSizeFull();
        //addStyleName("login-screen");

        AbsoluteLayout innerLayout = new AbsoluteLayout();
        innerLayout.setSizeFull();
        //innerLayout.setWidth("200px");
        //innerLayout.setHeight("400px");
        //innerLayout.setStyleName("background");
        //setCaption("Settings");

        final TextField nameField = new TextField("Name");
        nameField.setWidth(15, Unit.EM);
        nameField.setDescription("Enter your name");
        //addComponent(nameField);

        final DateField dateField = new DateField("Date of Birth");
        dateField.setWidth(15, Unit.EM);
        //addComponent(dateField);


        final Button submitButton = new Button("Submit");
        submitButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Notification.show("Thanks!");
            }
        });
        //addComponent(submitButton);
        //innerLayout.addComponent(nameField);
        //innerLayout.addComponent(dateField);
        //innerLayout.setComponentAlignment(nameField, Alignment.MIDDLE_CENTER);
        //innerLayout.setComponentAlignment(dateField, Alignment.MIDDLE_CENTER);


        ComboBox comboBox = new ComboBox();

        //comboBox.addItem("sdsd");
        comboBox.setWidth(8, Unit.EM);
        comboBox.setHeight(4, Unit.EM);


        innerLayout.addComponent(new Label(" "));
        innerLayout.addComponent(new Label("work in progress"), "left: 50px; top: 50px;");
        addComponent(innerLayout);
        //setComponentAlignment(innerLayout, Alignment.MIDDLE_CENTER);

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
