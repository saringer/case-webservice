package de.agdb.views.profile;


import com.github.scribejava.apis.GoogleApi20;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.Token;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.http.javanet.NetHttpTransport;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.vaadin.addon.calendar.Calendar;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.*;

@UIScope
@SpringView(name = ProfileView.VIEW_NAME)
public class ProfileView extends VerticalLayout implements View {
    public static final String VIEW_NAME = "ProfileView";
    /**
     * File for storing user credentials.
     */
    private static final java.io.File DATA_STORE_FILE =
            new File(System.getProperty("user.home"), ".credentials/user-credentials.json");

    /**
     * Global instance of the {DataStoreFactory}. The best practice is to make it a single
     * globally shared instance across your application.
     */
    private static FileDataStoreFactory dataStoreFactory;


    // Go to the Google API Console, open your application's
    // credentials page, and copy the client ID and client secret.
    // Then paste them into the following code.
    String clientId = "833038328979-iglf2mm0jap840b0vaq4jvo1miqvtkb4.apps.googleusercontent.com";
    String clientSecret = "Q_Pb1hvhGs1hH1CIsqYv1S1M";

    // Or your redirect URL for web based applications.
    String redirectUrl = "http://localhost:8080/Callback";

    String scope = "https://www.googleapis.com/auth/contacts.readonly";

    HttpTransport httpTransport = new NetHttpTransport();
    JacksonFactory jsonFactory = new JacksonFactory();
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
    GoogleCredential credential;

    @Autowired
    JdbcTemplate jdbcTemplate;


    @PostConstruct
    void init() {
        setSizeFull();
        setUpFormLayout();
        addComponent(formWrapper);
        setComponentAlignment(formWrapper, Alignment.MIDDLE_CENTER);

    }

    public void setUpFormLayout() {
        formWrapper = new VerticalLayout();
        formWrapper.setSizeFull();
        formWrapper.setSpacing(true);
        formWrapper.setMargin(true);
        formWrapper.addStyleName("white");


        // FormLayout
        form = new FormLayout();
        form.setMargin(true);
        //form.setWidth("800px");
        // form.addStyleName("light");

        formWrapper.addComponent(form);

        // form.addComponent(panelCaption);
        Label section = new Label("Personal Info");
        section.addStyleName("h3");
        section.addStyleName("colored");
        form.addComponent(section);
        name = new TextField("Name");
        name.setValue("Jim Saringer");
        name.setWidth("50%");
        form.addComponent(name);
        birthday = new DateField("Birthday");
        form.addComponent(birthday);
        username = new TextField("Username");
        username.setValue("Riva");

        // username.setRequired(true);
        form.addComponent(username);
        sex = new NativeSelect<>("Sex");
        sex.setItems("Male", "Female");
        sex.addStyleName("horizontal");
        sex.setSelectedItem("Male");
        form.addComponent(sex);

        VerticalLayout imageBox = new VerticalLayout();
        imageBox.setWidth(200, Unit.PIXELS);
        imageBox.setHeight(200, Unit.PIXELS);
        imageBox.setStyleName("wrapperPreview");
        // A theme resource in the current theme ("mytheme")
// Located in: VAADIN/themes/mytheme/img/themeimage.png
        ThemeResource resource = new ThemeResource("img/archetype-login-bg.jpg");

        Image image = new Image();
        image.setWidth("250px");
        image.setHeight("200px");
        imageBox.addComponent(image);


        //form.addComponent(imageBox);

        // uploader.addStyleName("horizontal");*/

        section = new Label("Contact Info");
        section.addStyleName("h3");
        section.addStyleName("colored");
        form.addComponent(section);
        email = new TextField("Email");

        email.setWidth("50%");
        email.setValue("jim.saringer@fu-berlin.de");
        // email.setRequired(true);
        form.addComponent(email);
        location = new TextField("Location");

        location.setWidth("50%");
        // location.setComponentError(new
        // UserError("This address doesn't exist"));
        form.addComponent(location);
        phone = new TextField("Phone");

        phone.setWidth("50%");
        form.addComponent(phone);

        section = new Label("Additional Info");
        section.addStyleName("h3");
        section.addStyleName("colored");
        form.addComponent(section);
        website = new TextField("Website");
        website.setDescription("http://");

        website.setWidth("100%");
        form.addComponent(website);
        shortbio = new TextArea("Short Bio");

        shortbio.setWidth("100%");
        shortbio.setRows(2);
        form.addComponent(shortbio);
        bio = new RichTextArea("Bio");
        bio.setWidth("100%");
        form.addComponent(bio);


        HorizontalLayout footer = new HorizontalLayout();
        footer.setMargin(new MarginInfo(true, false, true, false));
        footer.setSpacing(true);
        footer.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        form.addComponent(footer);

        section = new Label ("Calendar test");
        section.addStyleNames("h3");
        section.addStyleNames("colored");
        form.addComponent(section);
        Calendar cal = new Calendar();
        cal.setWidth("100%");
        form.addComponent(cal);

        form.addComponent(createBottomNav());

        section = new Label("Synchronize your Email-Accounts");
        section.addStyleName("h3");
        section.addStyleName("colored");
        form.addComponent(section);
        form.addComponent(setUpGoogleButton());






    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }


    private Button setUpGoogleButton() {
        Button google = new Button("google");
        return  google;
    }


    /**
     * Authorizes the installed application to access user's protected data.
     */
    private static Credential authorize() throws Exception {

        String scope = "https://www.googleapis.com/auth/contacts.readonly";
        dataStoreFactory = new FileDataStoreFactory(DATA_STORE_FILE);


        HttpTransport httpTransport = new NetHttpTransport();
        JacksonFactory jsonFactory = new JacksonFactory();
        // load client secrets
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory,
                new InputStreamReader(ProfileView.class.getResourceAsStream("/client_secret.json")));
        // set up authorization code flow
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, clientSecrets,
                Collections.singleton(scope)).setDataStoreFactory(dataStoreFactory)
                .setAccessType("offline").build();


        LocalServerReceiver localReceiver = new LocalServerReceiver.Builder().setHost("localhost").setPort(8090).build();


        return new AuthorizationCodeInstalledApp(flow, localReceiver).authorize("user");

    }

    private HorizontalLayout createBottomNav() {


        HorizontalLayout nav = new HorizontalLayout();
        nav.setWidth("100%");


        VerticalLayout createSchedulesButton = new VerticalLayout();
        //createSchedulesButton.setSizeFull();
        createSchedulesButton.setWidth("100%");
        createSchedulesButton.addStyleName("left-menu-style");
        //createSchedulesButton.addComponent(leftMenuIcon);
        //createSchedulesButton.addComponent(leftMenuCaption);
        Button buttonLeft = new Button("Create schedule");
        buttonLeft.setSizeUndefined();
        buttonLeft.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        //buttonLeft.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
        //buttonLeft.setIcon(VaadinIcons.CALENDAR_O);
        buttonLeft.addStyleName(ValoTheme.BUTTON_LARGE);
        createSchedulesButton.addComponent(buttonLeft);

        VerticalLayout manageSchedulesButton = new VerticalLayout();
        //manageSchedulesButton.setSizeFull();
        manageSchedulesButton.setWidth("100%");
        manageSchedulesButton.addStyleName("right-menu-style");
        Button buttonRight = new Button("Manage schedules");
        buttonRight.setSizeUndefined();
        buttonRight.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        //buttonRight.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
        //buttonRight.setIcon(VaadinIcons.CALENDAR);
        buttonRight.addStyleName(ValoTheme.BUTTON_LARGE);
        manageSchedulesButton.addComponent(buttonRight);

        nav.addComponent(createSchedulesButton);
        nav.addComponent(manageSchedulesButton);
        nav.setMargin(false);
        nav.setSpacing(false);
        //nav.setExpandRatio(createSchedulesButton, 1);
        // nav.setExpandRatio(manageSchedulesButton, 1);

        return nav;

    }








}
