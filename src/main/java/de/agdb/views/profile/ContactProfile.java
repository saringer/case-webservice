package de.agdb.views.profile;

import com.vaadin.event.LayoutEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import de.agdb.backend.data_model.repositories.UsersRepository;
import de.agdb.custom_components.CustomButton;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.alump.scaleimage.ScaleImage;
import org.vaadin.alump.scaleimage.css.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.*;


public class ContactProfile extends VerticalLayout {
    public static final String VIEW_NAME = "ProfileView";
    TextField firstName;
    TextField lastName;
    TextField age;
    TextField mobile;
    TextField home;
    TextField email;
    TextField location;
    TextField homeAddress;
    TextField organization;
    TextField function;
    Label usernameLabel = new Label();
    private ScaleImage image;

    File file;
    ByteArrayOutputStream outputBuffer = null;
    ArrayList<String> allowedMimeTypes = new ArrayList<String>();

    @Autowired
    UsersRepository usersRepository;
    private Window window;


    public ContactProfile(Window window) {
        this.window = window;

        setSizeFull();
        usernameLabel.setValue("Contact");


        HorizontalLayout content = setUpFormLayout();
        content.addStyleName("overflow-auto");
        content.setSpacing(false);
        content.setMargin(true);
        content.setHeight("95%");
        content.setWidth("95%");

        CssLayout userNameHeader = new CssLayout();
        userNameHeader.setWidth("100%");
        userNameHeader.setHeight(50, Unit.PIXELS);
        userNameHeader.addComponent(usernameLabel);
        userNameHeader.addStyleName("username-header");

        CssLayout bottomNav = new CssLayout();
        bottomNav.setSizeUndefined();
        bottomNav.setWidth("100%");
        CustomButton backButton = createBackButton();
        backButton.setWidth(167, Unit.PIXELS);
        backButton.setHeight(40, Unit.PIXELS);
        bottomNav.addComponent(backButton);


        setSpacing(false);
        setMargin(false);
        addComponent(userNameHeader);
        addComponent(content);
        addComponent(bottomNav);
        setComponentAlignment(content, Alignment.MIDDLE_CENTER);
        setExpandRatio(content, 1);


    }

    private CustomButton createBackButton() {
        LayoutEvents.LayoutClickListener listener = (LayoutEvents.LayoutClickListener) layoutClickEvent -> {
            window.close();
        };

        CustomButton backButton = new CustomButton(VaadinIcons.ARROW_CIRCLE_LEFT_O.getHtml() + " " + "BACK", listener);
        backButton.addStyleNames("back-button", "back-button-margin");
        return backButton;
    }

    public HorizontalLayout setUpFormLayout() {
        HorizontalLayout wrapperLayout = new HorizontalLayout();
        wrapperLayout.setSizeFull();


        FormLayout formLayout = new FormLayout();
        // formLayout.addStyleName("overflow-auto");
        formLayout.setSizeFull();

        VerticalLayout imageLayout = new VerticalLayout();
        imageLayout.addStyleName("solid-border-grey");
        imageLayout.setSizeUndefined();
        imageLayout.setSpacing(false);
        imageLayout.setMargin(false);
        imageLayout.addStyleName("image-layout");
        setImageBox();
        AbsoluteLayout absoluteLayout = new AbsoluteLayout();
        absoluteLayout.setWidth(200, Unit.PIXELS);
        absoluteLayout.setHeight(200, Unit.PIXELS);
        absoluteLayout.addComponent(image);
        imageLayout.addComponents(absoluteLayout);


        wrapperLayout.addComponents(imageLayout, formLayout);
        wrapperLayout.setExpandRatio(formLayout, 1);

        // PERSONAL INFORMATION
        Label personalSection = new Label("Personal information");
        personalSection.addStyleNames(ValoTheme.LABEL_H3, ValoTheme.LABEL_COLORED);
        VerticalLayout blockLayoutPersonalInformation = new VerticalLayout();
        blockLayoutPersonalInformation.addStyleName("info-block");
        blockLayoutPersonalInformation.setSizeUndefined();
        blockLayoutPersonalInformation.setWidth("100%");
        blockLayoutPersonalInformation.setMargin(false);
        blockLayoutPersonalInformation.setSpacing(false);
        HorizontalLayout personalInformationLayout = new HorizontalLayout();
        personalInformationLayout.setMargin(true);
        personalInformationLayout.setSpacing(true);
        FormLayout innerForm = new FormLayout();

        firstName = new TextField("First name:");
        firstName.setValue("Riva");
        firstName.setEnabled(false);
        firstName.setWidth("100%");
        lastName = new TextField("Last name:");
        lastName.setValue("Saringer");
        lastName.setEnabled(false);
        lastName.setWidth("100%");
        age = new TextField("Age:");
        age.setValue("27");
        age.setWidth("10%");
        age.setEnabled(false);

        innerForm.addComponents(firstName, lastName, age);
        personalInformationLayout.addComponent(innerForm);
        personalInformationLayout.setExpandRatio(innerForm, 1);
        personalInformationLayout.setWidth("100%");
        blockLayoutPersonalInformation.addComponents(personalInformationLayout);


        // Contact INFORMATION
        Label contactSection = new Label("Contact information");
        contactSection.addStyleNames(ValoTheme.LABEL_H3, ValoTheme.LABEL_COLORED);
        VerticalLayout blockLayoutContactInformation = new VerticalLayout();
        blockLayoutContactInformation.addStyleName("info-block");
        blockLayoutContactInformation.setSizeUndefined();
        blockLayoutContactInformation.setWidth("100%");
        blockLayoutContactInformation.setMargin(false);
        blockLayoutContactInformation.setSpacing(false);
        personalInformationLayout = new HorizontalLayout();
        personalInformationLayout.setMargin(true);
        personalInformationLayout.setSpacing(true);
        innerForm = new FormLayout();


        mobile = new TextField("Mobile:");
        mobile.setValue("0176 75189503");
        mobile.setEnabled(false);
        mobile.setWidth("100%");
        home = new TextField("Home:");
        home.setValue("030 44678388");
        home.setEnabled(false);
        home.setWidth("100%");
        email = new TextField("Email:");
        email.setValue("bidding@web.de");
        email.setWidth("100%");
        email.setEnabled(false);
        location = new TextField("Location:");
        location.setValue("...");
        location.setWidth("100%");
        location.setEnabled(false);
        homeAddress = new TextField("Home Address:");
        homeAddress.setValue("Lettestraße 3");
        homeAddress.setWidth("100%");
        homeAddress.setEnabled(false);


        innerForm.addComponents(mobile, home, email, location, homeAddress);
        personalInformationLayout.addComponent(innerForm);
        personalInformationLayout.setExpandRatio(innerForm, 1);
        personalInformationLayout.setWidth("100%");
        blockLayoutContactInformation.addComponents(personalInformationLayout);


        // JOB INFORMATION
        Label jobSection = new Label("Job information");
        jobSection.addStyleNames(ValoTheme.LABEL_H3, ValoTheme.LABEL_COLORED);
        VerticalLayout blockLayoutJobInformation = new VerticalLayout();
        blockLayoutJobInformation.addStyleName("info-block");
        blockLayoutJobInformation.setSizeUndefined();
        blockLayoutJobInformation.setWidth("100%");
        blockLayoutJobInformation.setMargin(false);
        blockLayoutJobInformation.setSpacing(false);
        personalInformationLayout = new HorizontalLayout();
        personalInformationLayout.setMargin(true);
        personalInformationLayout.setSpacing(true);
        innerForm = new FormLayout();

        organization = new TextField("Organization:");
        organization.setValue("Charite");
        organization.setEnabled(false);
        organization.setWidth("100%");
        function = new TextField("Function:");
        function.setValue("Ambulance Driver");
        function.setEnabled(false);
        function.setWidth("100%");



        innerForm.addComponents(organization, function);
        personalInformationLayout.addComponent(innerForm);
        personalInformationLayout.setExpandRatio(innerForm, 1);
        personalInformationLayout.setWidth("100%");
        blockLayoutJobInformation.addComponents(personalInformationLayout);


        formLayout.addComponent(personalSection);
        formLayout.addComponents(blockLayoutPersonalInformation);
        formLayout.addComponent(contactSection);
        formLayout.addComponent(blockLayoutContactInformation);
        formLayout.addComponent(jobSection);
        formLayout.addComponent(blockLayoutJobInformation);

        return wrapperLayout;

    }

    private void setImageBox() {
        image = new ScaleImage();
        image.setWidth(200, Unit.PIXELS);
        image.setHeight(200, Unit.PIXELS);


        image.setCssValues(

                BackgroundSize.COVER,
                BackgroundAttachment.LOCAL,
                //BackgroundClip.PADDING_BOX,
                //BackgroundOrigin.PADDING_BOX,
                BackgroundPositionX.CENTER,
                BackgroundPositionY.CENTER,
                new BackgroundColor("silver")
        );

    }







}

