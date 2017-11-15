package de.agdb.custom_components.modal_windows;

import com.vaadin.event.LayoutEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import de.agdb.AppUI;
import de.agdb.backend.data_model.DailyEvent;
import de.agdb.backend.data_model.Users;
import de.agdb.backend.data_model.repositories.CategoriesWrapperRepository;
import de.agdb.backend.data_model.repositories.DateWrapperRepository;
import de.agdb.backend.data_model.repositories.TimeLocationWrapperRepository;
import de.agdb.backend.data_model.repositories.UsersRepository;
import de.agdb.backend.data_model.schedule_wrapper_objects.AssignedContact;
import de.agdb.backend.data_model.schedule_wrapper_objects.CategoriesWrapper;
import de.agdb.backend.data_model.schedule_wrapper_objects.DateWrapper;
import de.agdb.backend.data_model.schedule_wrapper_objects.TimeLocationWrapper;
import de.agdb.custom_components.CustomButton;

import java.time.format.DateTimeFormatter;

public class HandleInvitationsWindow extends Window {

    private TimeLocationWrapperRepository timeLocationWrapperRepository;
    private CategoriesWrapperRepository categoriesWrapperRepository;
    private UsersRepository usersRepository;
    private DailyEvent dailyEvent;
    private DateWrapperRepository dateWrapperRepository;


    public HandleInvitationsWindow(DailyEvent dailyEvent, TimeLocationWrapperRepository timeLocationWrapperRepository, CategoriesWrapperRepository categoriesWrapperRepository,
                                   UsersRepository usersRepository, DateWrapperRepository dateWrapperRepository) {
        this.timeLocationWrapperRepository = timeLocationWrapperRepository;
        this.categoriesWrapperRepository = categoriesWrapperRepository;
        this.usersRepository = usersRepository;
        this.dailyEvent = dailyEvent;
        this.dateWrapperRepository = dateWrapperRepository;
        addStyleName("set-window-style");
        setModal(true);
        center();
        setClosable(false);
        setResizable(false);

        VerticalLayout rootLayout = new VerticalLayout();
        rootLayout.setSizeFull();
        rootLayout.setMargin(false);
        rootLayout.setSpacing(false);

        CssLayout headerLayout = createWindowHeader();
        headerLayout.setWidth("100%");
        headerLayout.setHeight(40, Sizeable.Unit.PIXELS);

        Panel wrapperPanel = new Panel();
        wrapperPanel.setWidth("80%");
        wrapperPanel.setHeight("80%");
        wrapperPanel.addStyleName("overflow-auto");
        VerticalLayout contentLayout = new VerticalLayout();
        contentLayout.setMargin(true);
        contentLayout.setSpacing(true);
        contentLayout.addStyleName("overflow-auto");
        contentLayout.setSizeUndefined();
        contentLayout.setWidth("100%");
        AppUI app = (AppUI) UI.getCurrent();
        Users thisUser = usersRepository.findByUsername(app.getCurrentUsername()).get(0);
        for (int i = 0; i < dailyEvent.getTimeLocationList().size(); i++) {
            Long timeLocationID = dailyEvent.getTimeLocationList().get(i);
            Long categoriesWrapperId = dailyEvent.getCategoriesWrapperList().get(i);
            String currentUserEmail = thisUser.getEmail();
            Long dateWrapperId = dailyEvent.getDateWrapperId();
            contentLayout.addComponent(buildItem(dateWrapperId, currentUserEmail, timeLocationID, categoriesWrapperId));
        }

        wrapperPanel.setContent(contentLayout);


        CustomButton backButton = createBackButton();
        backButton.setWidth(167, Unit.PIXELS);
        backButton.setHeight(40, Unit.PIXELS);
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setHeight(60, Unit.PIXELS);
        buttonLayout.setWidth("80%");
        buttonLayout.setMargin(false);
        buttonLayout.setSpacing(false);
        buttonLayout.addComponent(backButton);
        buttonLayout.setComponentAlignment(backButton, Alignment.TOP_LEFT);

        rootLayout.addComponent(headerLayout);
        rootLayout.addComponent(wrapperPanel);
        rootLayout.addComponent(buttonLayout);

        rootLayout.setComponentAlignment(wrapperPanel, Alignment.MIDDLE_CENTER);
        rootLayout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_CENTER);
        rootLayout.setExpandRatio(wrapperPanel, 0.6f);
        //rootLayout.setExpandRatio(buttonLayout,0.1f);


        setContent(rootLayout);
    }

    private HorizontalLayout buildItem(Long dateWrapperId, String currentUserEmail, Long timeLocationWrapperId, Long categoriesWrapperId) {
        CategoriesWrapper categoriesWrapperObject = categoriesWrapperRepository.findOne(categoriesWrapperId);
        AssignedContact assignedContactObject = categoriesWrapperObject.findAssignedContact(currentUserEmail);
        boolean isParticipating = assignedContactObject.isParticipating();

        TimeLocationWrapper timeLocationWrapper = timeLocationWrapperRepository.findOne(timeLocationWrapperId);


        HorizontalLayout item = new HorizontalLayout();
        item.setWidth("100%");
        item.setSpacing(false);
        item.setMargin(false);
        item.addStyleNames("solid-border", "invitation-item-layout");

        final CustomButton cancelButton = new CustomButton(VaadinIcons.CLOSE.getHtml());

        final CustomButton okayButton = new CustomButton(VaadinIcons.CHECK.getHtml());


        LayoutEvents.LayoutClickListener listener = new LayoutEvents.LayoutClickListener() {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                CategoriesWrapper categoriesWrapper = categoriesWrapperRepository.findOne(categoriesWrapperId);
                AssignedContact assignedContact = categoriesWrapper.findAssignedContact(currentUserEmail);
                assignedContact.setParticipating(false);
                categoriesWrapperRepository.save(categoriesWrapper);
                cancelButton.setStyleName("grey-button");
                okayButton.setStyleName("next-button");

            }
        };
        // cancelButton = new CustomButton(VaadinIcons.CLOSE.getHtml(), listener);
        cancelButton.addLayoutClickListener(listener);
        cancelButton.addStyleName("cancel-button");
        //cancelButton.setHeight(40, Unit.PIXELS);
        cancelButton.setWidth("100%");


        listener = (LayoutEvents.LayoutClickListener) event -> {

            CategoriesWrapper categoriesWrapper = categoriesWrapperRepository.findOne(categoriesWrapperId);
            AssignedContact assignedContact = categoriesWrapper.findAssignedContact(currentUserEmail);
            assignedContact.setParticipating(true);
            categoriesWrapperRepository.save(categoriesWrapper);
            okayButton.setStyleName("grey-button");
            cancelButton.setStyleName("cancel-button");

        };
       // okayButton = new CustomButton(VaadinIcons.CHECK.getHtml(), listener);
        okayButton.addLayoutClickListener(listener);
        okayButton.addStyleName("next-button");
        //okayButton.setHeight(40, Unit.PIXELS);
        okayButton.setWidth("100%");

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setWidth("100%");
        verticalLayout.setSpacing(false);
        verticalLayout.setMargin(false);

        Label timeLocationLabel = new Label(timeLocationWrapper.getFormattedStartTime() + "  - "
                + timeLocationWrapper.getFormattedEndTime() + "<br>" + timeLocationWrapper.getLocation());
        timeLocationLabel.setContentMode(ContentMode.HTML);
        timeLocationLabel.setWidth("70%");
        verticalLayout.addComponent(timeLocationLabel);
        verticalLayout.setComponentAlignment(timeLocationLabel, Alignment.MIDDLE_CENTER);


        item.addComponents(cancelButton, verticalLayout, okayButton);
        item.setComponentAlignment(cancelButton, Alignment.MIDDLE_LEFT);
        item.setComponentAlignment(verticalLayout, Alignment.MIDDLE_LEFT);
        item.setComponentAlignment(okayButton, Alignment.MIDDLE_RIGHT);
        item.setExpandRatio(okayButton, 0.25f);
        item.setExpandRatio(verticalLayout, 0.5f);
        item.setExpandRatio(cancelButton, 0.25f);

        CategoriesWrapper categoriesWrapper = categoriesWrapperRepository.findOne(categoriesWrapperId);
        AssignedContact assignedContact = categoriesWrapper.findAssignedContact(currentUserEmail);
        if (assignedContact.isParticipating()) {
            okayButton.setStyleName("grey-button");
        }
        else {
            cancelButton.setStyleName("grey-button");
        }

        return item;
    }

    private CssLayout createWindowHeader() {
        // day.getDay().format(DateTimeFormatter.ofPattern("EEEE, dd.MM.yyyy")
        CssLayout headerLayout = new CssLayout();
        DateWrapper dateWrapper = dateWrapperRepository.findOne(dailyEvent.getDateWrapperId());
        Label headerLabel = new Label("Select suitable appointments - " + dateWrapper.getDay().format(DateTimeFormatter.ofPattern("EEEE, dd.MM.yyyy")));
        headerLayout.setSizeUndefined();
        headerLayout.addComponent(headerLabel);
        headerLayout.addStyleName("invitations-window-header");
        return headerLayout;
    }

    private CustomButton createBackButton() {
        LayoutEvents.LayoutClickListener listener = (LayoutEvents.LayoutClickListener) layoutClickEvent -> {
            close();
        };

        CustomButton backButton = new CustomButton(VaadinIcons.ARROW_CIRCLE_LEFT_O.getHtml() + " " + "BACK", listener);
        backButton.addStyleNames("modal-window-back-button");
        return backButton;
    }

    private void setButtonState(CustomButton button) {
        button.setStyleName("grey-button");

    }
}
