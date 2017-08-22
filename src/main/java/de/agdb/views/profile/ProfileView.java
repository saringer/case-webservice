package de.agdb.views.profile;


import com.vaadin.event.LayoutEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.*;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;


import com.vaadin.ui.themes.ValoTheme;
import de.agdb.AppUI;
import de.agdb.backend.entities.repositories.UsersRepository;
import de.agdb.views.scheduler.CustomButton;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.alump.materialicons.MaterialIcons;
import org.vaadin.alump.scaleimage.ScaleImage;
import org.vaadin.alump.scaleimage.css.*;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.*;

@UIScope
@SpringView(name = ProfileView.VIEW_NAME)
public class ProfileView extends VerticalLayout implements View, Upload.Receiver,
        Upload.SucceededListener, Upload.StartedListener {
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
    private Upload uploader;
    private CssLayout personalButtons;
    private CssLayout contactButtons;
    private CssLayout jobButtons;

    File file;
    ByteArrayOutputStream outputBuffer = null;
    ArrayList<String> allowedMimeTypes = new ArrayList<String>();

    @Autowired
    UsersRepository usersRepository;



    @PostConstruct
    void init() {
        allowedMimeTypes.add("image/jpeg");
        allowedMimeTypes.add("image/png");

        setSizeFull();
        VerticalLayout formWrapper = new VerticalLayout();
        formWrapper.setWidth(900, Unit.PIXELS);
        formWrapper.setHeight(700, Unit.PIXELS);
        formWrapper.addStyleName("profile-layout");
        formWrapper.setSpacing(false);
        formWrapper.setMargin(false);


        addComponent(formWrapper);
        setComponentAlignment(formWrapper, Alignment.MIDDLE_CENTER);


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

        formWrapper.setSpacing(false);
        formWrapper.setMargin(false);
        formWrapper.addComponent(userNameHeader);
        formWrapper.addComponent(content);
        formWrapper.setComponentAlignment(content, Alignment.MIDDLE_CENTER);
        formWrapper.setExpandRatio(content, 1);


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
        uploader = new Upload();
        uploader.setSizeFull();
        uploader.setButtonCaption("Upload new picture");
        uploader.setReceiver(this);
        uploader.addSucceededListener(this);
        uploader.addStartedListener(this);
        AbsoluteLayout absoluteLayout = new AbsoluteLayout();
        absoluteLayout.setWidth(200, Unit.PIXELS);
        absoluteLayout.setHeight(200, Unit.PIXELS);
        absoluteLayout.addComponent(image);
        Button rotateButton = new Button();
        rotateButton.setIcon(VaadinIcons.ROTATE_RIGHT);
        rotateButton.addStyleNames(ValoTheme.BUTTON_BORDERLESS_COLORED);
        absoluteLayout.addComponent(rotateButton,"right:0px; top:0px");
        imageLayout.addComponents(absoluteLayout, uploader);




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
        Button editButton = new Button("edit");
        editButton.addClickListener((Button.ClickListener) clickEvent -> {
            personalButtons.setVisible(true);
            editButton.setVisible(false);
            firstName.setEnabled(true);
            lastName.setEnabled(true);
            age.setEnabled(true);
        });
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
        LayoutEvents.LayoutClickListener cancelListener = (LayoutEvents.LayoutClickListener) layoutClickEvent -> {
                personalButtons.setVisible(false);
                editButton.setVisible(true);
                firstName.setEnabled(false);
                lastName.setEnabled(false);
                age.setEnabled(false);
        };
        LayoutEvents.LayoutClickListener okayListener = (LayoutEvents.LayoutClickListener) layoutClickEvent -> {

        };
        personalButtons = setUpFormButtons(cancelListener,okayListener);
        personalButtons.setWidth("100%");
        innerForm.addComponents(firstName, lastName, age);
        personalButtons.setVisible(false);
        editButton.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.BUTTON_BORDERLESS);
        personalInformationLayout.addComponent(innerForm);
        personalInformationLayout.addComponent(editButton);
        personalInformationLayout.setExpandRatio(innerForm, 1);
        personalInformationLayout.setComponentAlignment(editButton, Alignment.TOP_RIGHT);
        personalInformationLayout.setWidth("100%");
        blockLayoutPersonalInformation.addComponents(personalInformationLayout, personalButtons);


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
        Button addEmail = new Button("Add email");
        addEmail.setWidth("100%");
        addEmail.setVisible(false);
        Button editButton2 = new Button("edit");
        editButton2.addClickListener((Button.ClickListener) clickEvent -> {
            contactButtons.setVisible(true);
            addEmail.setVisible(true);
            editButton2.setVisible(false);
            mobile.setEnabled(true);
            home.setEnabled(true);
            email.setEnabled(true);
            location.setEnabled(true);
            homeAddress.setEnabled(true);
        });
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
        cancelListener = (LayoutEvents.LayoutClickListener) layoutClickEvent -> {
            contactButtons.setVisible(false);
            editButton2.setVisible(true);
            addEmail.setVisible(false);
            mobile.setEnabled(false);
            home.setEnabled(false);
            email.setEnabled(false);
            location.setEnabled(false);
            homeAddress.setEnabled(false);
        };
        okayListener = (LayoutEvents.LayoutClickListener) layoutClickEvent -> {

        };
        contactButtons = setUpFormButtons(cancelListener,okayListener);
        contactButtons.setWidth("100%");
        innerForm.addComponents(mobile, home, email, addEmail, location, homeAddress);
        contactButtons.setVisible(false);
        editButton2.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.BUTTON_BORDERLESS);
        personalInformationLayout.addComponent(innerForm);
        personalInformationLayout.addComponent(editButton2);
        personalInformationLayout.setExpandRatio(innerForm, 1);
        personalInformationLayout.setComponentAlignment(editButton2, Alignment.TOP_RIGHT);
        personalInformationLayout.setWidth("100%");
        blockLayoutContactInformation.addComponents(personalInformationLayout, contactButtons);


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
        Button editButton3 = new Button("edit");
        editButton3.addClickListener((Button.ClickListener) clickEvent -> {
            jobButtons.setVisible(true);
            editButton3.setVisible(false);
            organization.setEnabled(true);
            function.setEnabled(true);
        });
        organization = new TextField("Organization:");
        organization.setValue("Charite");
        organization.setEnabled(false);
        organization.setWidth("100%");
        function = new TextField("Function:");
        function.setValue("Ambulance Driver");
        function.setEnabled(false);
        function.setWidth("100%");
        cancelListener = (LayoutEvents.LayoutClickListener) layoutClickEvent -> {
            jobButtons.setVisible(false);
            editButton3.setVisible(true);
            organization.setEnabled(false);
            function.setEnabled(false);
        };
        okayListener = (LayoutEvents.LayoutClickListener) layoutClickEvent -> {

        };
        jobButtons = setUpFormButtons(cancelListener,okayListener);
        jobButtons.setWidth("100%");
        innerForm.addComponents(organization, function);
        jobButtons.setVisible(false);
        editButton3.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.BUTTON_BORDERLESS);
        personalInformationLayout.addComponent(innerForm);
        personalInformationLayout.addComponent(editButton3);
        personalInformationLayout.setExpandRatio(innerForm, 1);
        personalInformationLayout.setComponentAlignment(editButton3, Alignment.TOP_RIGHT);
        personalInformationLayout.setWidth("100%");
        blockLayoutJobInformation.addComponents(personalInformationLayout, jobButtons);



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

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {


        AppUI app = (AppUI) UI.getCurrent();
        String userName = app.getAccessControl().getUsername();
        usernameLabel.setValue(userName);



    }


    @Override
    public OutputStream receiveUpload(String fileName, String mimeType) {

        outputBuffer = new ByteArrayOutputStream();
        return outputBuffer;
    }

    @Override
    public void uploadSucceeded(Upload.SucceededEvent succeededEvent) {
        File file = new File("C:\\Users\\Riva\\Desktop\\" + succeededEvent.getFilename());

        image.setSource(new FileResource(file));
        //image.

    }

    private CssLayout setUpFormButtons(LayoutEvents.LayoutClickListener cancelListener, LayoutEvents.LayoutClickListener okayListener) {
        CssLayout buttonLayout = new CssLayout();
        CustomButton cancelButton = new CustomButton(VaadinIcons.CLOSE.getHtml(), cancelListener);
        cancelButton.addStyleNames("cancel-button","float-left");
        cancelButton.setHeight(40, Unit.PIXELS);
        cancelButton.setWidth(150, Unit.PIXELS);

        CustomButton okayButton = new CustomButton(MaterialIcons.SAVE.getHtml(), okayListener);
        okayButton.addStyleNames("save-button", "float-right");
        okayButton.setHeight(40, Unit.PIXELS);
        okayButton.setWidth(150, Unit.PIXELS);

        buttonLayout.addComponents(cancelButton,okayButton);
        return  buttonLayout;
    }


    @Override
    public void uploadStarted(Upload.StartedEvent startedEvent) {

        String contentType = startedEvent.getMIMEType();
        boolean allowed = false;
        for(int i=0;i<allowedMimeTypes.size();i++){
            if(contentType.equalsIgnoreCase(allowedMimeTypes.get(i))){
                allowed = true;
                break;
            }
        }
        if(allowed){

        }else{
            Notification.show("Error", "\nAllowed MIME: "+allowedMimeTypes, Notification.Type.ERROR_MESSAGE);
            uploader.interruptUpload();
        }
    }
}
