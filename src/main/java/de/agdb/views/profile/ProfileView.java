package de.agdb.views.profile;


import com.github.scribejava.apis.GoogleApi20;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.Token;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.vaadin.event.LayoutEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.*;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;

import com.google.api.client.auth.oauth2.Credential;


import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;


import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import com.vaadin.ui.themes.ValoTheme;
import de.agdb.backend.entities.Categories;
import de.agdb.backend.entities.Contact;
import de.agdb.backend.entities.Users;
import de.agdb.backend.entities.UsersRepository;
import de.agdb.views.scheduler.CustomButton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.vaadin.addon.calendar.Calendar;
import org.vaadin.alump.scaleimage.ScaleImage;
import org.vaadin.alump.scaleimage.css.*;

import javax.annotation.PostConstruct;
import javax.swing.text.html.CSS;
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
    private ScaleImage image;
    private Upload uploader;
    private CssLayout personalButtons;
    private CssLayout contactButtons;

    File file;
    ByteArrayOutputStream outputBuffer = null;
    ArrayList<String> allowedMimeTypes = new ArrayList<String>();


    @PostConstruct
    void init() {
        allowedMimeTypes.add("image/jpeg");
        allowedMimeTypes.add("image/png");

        setSizeFull();
        VerticalLayout formWrapper = new VerticalLayout();
        formWrapper.setWidth(1150, Unit.PIXELS);
        formWrapper.setHeight(700, Unit.PIXELS);
        formWrapper.addStyleName("solid-border");

        addComponent(formWrapper);
        setComponentAlignment(formWrapper, Alignment.MIDDLE_CENTER);


        HorizontalLayout content = setUpFormLayout();
        content.addStyleName("overflow-auto");
        content.setSpacing(true);
        content.setMargin(true);
        content.setHeight("95%");
        content.setWidth("80%");


        formWrapper.setSpacing(false);
        formWrapper.setMargin(false);
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
        imageLayout.addComponents(image, uploader);




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
        Button editButton2 = new Button("edit");
        editButton2.addClickListener((Button.ClickListener) clickEvent -> {
            contactButtons.setVisible(true);
            editButton2.setVisible(false);
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
        cancelListener = (LayoutEvents.LayoutClickListener) layoutClickEvent -> {
            contactButtons.setVisible(false);
            editButton2.setVisible(true);
            firstName.setEnabled(false);
            lastName.setEnabled(false);
            age.setEnabled(false);
        };
        okayListener = (LayoutEvents.LayoutClickListener) layoutClickEvent -> {

        };
        contactButtons = setUpFormButtons(cancelListener,okayListener);
        contactButtons.setWidth("100%");
        innerForm.addComponents(firstName, lastName, age);
        contactButtons.setVisible(false);
        editButton2.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.BUTTON_BORDERLESS);
        personalInformationLayout.addComponent(innerForm);
        personalInformationLayout.addComponent(editButton2);
        personalInformationLayout.setExpandRatio(innerForm, 1);
        personalInformationLayout.setComponentAlignment(editButton2, Alignment.TOP_RIGHT);
        personalInformationLayout.setWidth("100%");
        blockLayoutContactInformation.addComponents(personalInformationLayout, personalButtons);




        formLayout.addComponent(personalSection);
        formLayout.addComponents(blockLayoutPersonalInformation);
        formLayout.addComponent(contactSection);
        formLayout.addComponent(blockLayoutContactInformation);

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

        CustomButton okayButton = new CustomButton(VaadinIcons.CHECK.getHtml(), okayListener);
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
