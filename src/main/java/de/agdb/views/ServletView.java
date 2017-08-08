package de.agdb.views;


import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;

@UIScope
@SpringView(name = ServletView.VIEW_NAME)
public class ServletView extends VerticalLayout implements View {
    public static final String VIEW_NAME = "Callback";

    HorizontalSplitPanel rootLayout;
    // User user;
    // UserImage picture;
    ListSelect groupList;
    //StartSeite startSeiteInstance;

    VerticalLayout formWrapper;
    FormLayout form;

    // UserDetails
    TextField name;
    DateField birthday;
    TextField username;
    NativeSelect<String> sex;
    TextField email;
    TextField location;
    TextField phone;
    TextField website;
    TextArea shortbio;
    RichTextArea bio;


    public ServletView() {





        //	form.addComponent(groupList);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        setSizeFull();
        UI.getCurrent().getPage().setLocation("https://www.google.de/");
    }
}
