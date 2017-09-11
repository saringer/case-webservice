package de.agdb.custom_components.modal_windows;


import com.vaadin.addon.onoffswitch.OnOffSwitch;
import com.vaadin.data.HasValue;
import com.vaadin.event.LayoutEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import de.agdb.AppUI;
import de.agdb.backend.broadcaster.Broadcaster;
import de.agdb.backend.data_model.DailyEvent;
import de.agdb.backend.data_model.Users;
import de.agdb.backend.data_model.repositories.*;
import de.agdb.backend.data_model.Contact;
import de.agdb.backend.data_model.schedule_wrapper_objects.AssignedContact;
import de.agdb.backend.data_model.schedule_wrapper_objects.CategoriesWrapper;
import de.agdb.backend.data_model.schedule_wrapper_objects.DateWrapper;
import de.agdb.backend.data_model.schedule_wrapper_objects.TimeLocationWrapper;
import de.agdb.custom_components.CustomButton;
import org.vaadin.addons.popupextension.PopupExtension;


import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class SetParticipantsWindow extends Window {
    private DateWrapper day;
    private CategoriesWrapperRepository categoriesWrapperRepository;
    private UsersRepository usersRepository;
    private DailyEventRepository dailyEventRepository;
    private TimeLocationWrapperRepository timeLocationWrapperRepository;
    private List<List<List<AssignedContact>>> gridContacts = new ArrayList<>();
    private Contact selectedContact;
    private GridLayout gridLayout;
    private String eventTitle;
    private String eventDescription;


    public SetParticipantsWindow(DateWrapper day,
                                 CategoriesWrapperRepository categoriesWrapperRepository,

                                 UsersRepository usersRepository, DailyEventRepository dailyEventRepository, TimeLocationWrapperRepository timeLocationWrapperRepository, String title, String description) {
        this.categoriesWrapperRepository = categoriesWrapperRepository;
        this.usersRepository = usersRepository;
        this.dailyEventRepository = dailyEventRepository;
        this.timeLocationWrapperRepository = timeLocationWrapperRepository;
        this.eventTitle = title;
        this.eventDescription = description;

        this.day = day;
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
        headerLayout.setHeight(50, Sizeable.Unit.PIXELS);

        VerticalLayout contentLayout = new VerticalLayout();
        contentLayout.setMargin(true);
        contentLayout.setSpacing(true);
        contentLayout.setHeight("80%");
        contentLayout.setWidth("90%");

        gridLayout = setUpGridLayout();
        gridLayout.setSizeUndefined();
        gridLayout.setWidth("100%");
        Label dayLabel = new Label(day.getDay().format(DateTimeFormatter.ofPattern("EEEE, dd.MM.yyyy", Locale.UK)));
        dayLabel.addStyleName("day-header");
        contentLayout.addComponent(dayLabel);
        contentLayout.addComponent(gridLayout);


        contentLayout.setExpandRatio(gridLayout, 1);
        contentLayout.addStyleName("overflow-auto");

        CustomButton backButton = createBackButton();
        backButton.setWidth(167, Unit.PIXELS);
        backButton.setHeight(40, Unit.PIXELS);
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setHeight(60, Unit.PIXELS);
        buttonLayout.setWidth("90%");
        buttonLayout.setMargin(false);
        buttonLayout.setSpacing(false);
        buttonLayout.addComponent(backButton);
        buttonLayout.setComponentAlignment(backButton, Alignment.TOP_LEFT);


        rootLayout.addComponent(headerLayout);
        rootLayout.addComponent(contentLayout);
        rootLayout.addComponent(buttonLayout);

        rootLayout.setComponentAlignment(contentLayout, Alignment.MIDDLE_CENTER);
        rootLayout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_CENTER);
        rootLayout.setExpandRatio(contentLayout, 1);


        setContent(rootLayout);

    }

    private GridLayout setUpGridLayout() {
        int numberRows = day.getTimeAndLocationList().size();
        // row number plus one for the grid column headers
        GridLayout gridLayout = new GridLayout(4, numberRows + 1);

        //gridLayout.setColumnExpandRatio(1, 0.2f);
        //gridLayout.setRowExpandRatio(1,1);
        gridLayout.setColumnExpandRatio(0, 0.15f);
        gridLayout.setColumnExpandRatio(1, 0.65f);
        gridLayout.setColumnExpandRatio(2, 0.1f);
        gridLayout.setColumnExpandRatio(3, 0.1f);


        VerticalLayout wrapperLayout;

        /**
         * SET UP COLUMN HEADERS FOR THE GRID
         */
        String headerLabels[] = {
                "Time/Location",
                "Participants",
                "Status",
                "Select",};
        for (int i = 0; i < headerLabels.length; i++) {
            wrapperLayout = new VerticalLayout();
            wrapperLayout.setSizeFull();
            wrapperLayout.setSpacing(false);
            wrapperLayout.setMargin(false);

            Label label = new Label(headerLabels[i]);
            label.addStyleNames("column-header");
            label.setWidth(null); // Set width as undefined

            wrapperLayout.addComponent(label);
            wrapperLayout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
            wrapperLayout.addStyleName("solid-border-grey");
            gridLayout.addComponent(wrapperLayout);


        }

        /**
         * FILL GRID WITH DATA
         */

        for (int i = 0; i < day.getTimeAndLocationList().size(); i++) {
            TimeLocationWrapper timeLocationWrapper = day.getTimeAndLocationList().get(i);


            /**
             * Time/Location
             */
            wrapperLayout = new VerticalLayout();
            wrapperLayout.setSizeFull();
            wrapperLayout.setSpacing(false);
            wrapperLayout.setMargin(false);
            String startTime = day.getTimeAndLocationList().get(i).getFormattedStartTime();
            String endTime = day.getTimeAndLocationList().get(i).getFormattedEndTime();
            String location = day.getTimeAndLocationList().get(i).getLocation();
            Label timeLocationLabel = new Label(startTime + "-" + endTime + " " + location);
            timeLocationLabel.addStyleName("location-time-margin");
            timeLocationLabel.setWidth("90%");
            wrapperLayout.addComponent(timeLocationLabel);
            wrapperLayout.setComponentAlignment(timeLocationLabel, Alignment.MIDDLE_CENTER);
            wrapperLayout.addStyleNames("solid-border-grey", "location-time-description");
            gridLayout.addComponent(wrapperLayout);
            /**
             * Participants
             */
            gridContacts.add(new ArrayList<>());
            gridLayout.addComponent(createParticipantsLayout(timeLocationWrapper, i));
            // gridContacts.add

            /**
             * Event status
             */
            wrapperLayout = new VerticalLayout();
            wrapperLayout.setSizeFull();
            wrapperLayout.setSpacing(false);
            wrapperLayout.setMargin(false);
            Label calendarIconLabel = new Label(FontAwesome.CALENDAR_CHECK_O.getHtml(), ContentMode.HTML);
            wrapperLayout.addComponent(calendarIconLabel);
            wrapperLayout.setComponentAlignment(calendarIconLabel, Alignment.MIDDLE_CENTER);
            wrapperLayout.addStyleNames("solid-border-grey", "status-grey");
            gridLayout.addComponent(wrapperLayout);

            /**
             * Set event status
             */
            wrapperLayout = new VerticalLayout();
            wrapperLayout.setSizeFull();
            wrapperLayout.setSpacing(false);
            wrapperLayout.setMargin(false);
            OnOffSwitch onOffSwitch;
            if (timeLocationWrapper.isActive()) {
                onOffSwitch = new OnOffSwitch(true);
                calendarIconLabel.setStyleName("status-green");

            } else {
                onOffSwitch = new OnOffSwitch(false);
                calendarIconLabel.setStyleName("status-grey");

            }
            onOffSwitch.addValueChangeListener(new HasValue.ValueChangeListener<Boolean>() {
                @Override
                public void valueChange(HasValue.ValueChangeEvent<Boolean> event) {
                    if (event.getValue() == true) {

                        calendarIconLabel.setStyleName("status-green");
                        timeLocationWrapper.setActive(true);
                        timeLocationWrapperRepository.save(timeLocationWrapper);


                    } else {

                        calendarIconLabel.setStyleName("status-grey");
                        timeLocationWrapper.setActive(false);
                        timeLocationWrapperRepository.save(timeLocationWrapper);
                    }
                }
            });
            wrapperLayout.addComponent(onOffSwitch);
            wrapperLayout.setComponentAlignment(onOffSwitch, Alignment.MIDDLE_CENTER);
            wrapperLayout.addStyleName("solid-border-grey");
            gridLayout.addComponent(wrapperLayout);

        }

        return gridLayout;

    }

    private VerticalLayout createParticipantsLayout(TimeLocationWrapper timeLocationWrapper, int timeLocationIndex) {
        VerticalLayout addParticipantsLayout = new VerticalLayout();
        addParticipantsLayout.addStyleName("solid-border-grey");
        addParticipantsLayout.setMargin(true);
        addParticipantsLayout.setSpacing(false);
        addParticipantsLayout.setSizeUndefined();
        addParticipantsLayout.setWidth("100%");
        List<CategoriesWrapper> categoriesWrapperList = timeLocationWrapper.getCategoriesList();

        for (int x = 0; x < categoriesWrapperList.size(); x++) {
            int categoriesListIndex = x;


            // CategoryLayout
            VerticalLayout wrapperLayout = new VerticalLayout();
            wrapperLayout.setWidth("100%");
            wrapperLayout.setSpacing(false);
            wrapperLayout.setMargin(true);

            // Title layout
            VerticalLayout headerLayout = new VerticalLayout();
            headerLayout.setMargin(false);
            headerLayout.setSpacing(false);
            headerLayout.setWidth("100%");
            Label categoryTitle = new Label(categoriesWrapperList.get(x).getCategoryTitle());
            categoryTitle.setSizeUndefined();
            headerLayout.addComponent(categoryTitle);
            headerLayout.setComponentAlignment(categoryTitle, Alignment.MIDDLE_CENTER);
            headerLayout.addStyleNames("solid-border-grey", "category-header");

            // Participants Layout
            CssLayout participantsLayout = new CssLayout();
            participantsLayout.setSizeUndefined();
            participantsLayout.setWidth("100%");

            gridContacts.get(timeLocationIndex).add(new ArrayList<>());
            for (int y = 0; y < categoriesWrapperList.get(categoriesListIndex).getNumberParticipants(); y++) {
                AssignedContact placeHolderObject = null;
                gridContacts.get(timeLocationIndex).get(categoriesListIndex).add(placeHolderObject);
//                    gridContacts.get(x).set(y, placeHolderObject);


                VerticalLayout clickableUserField = new VerticalLayout();
                clickableUserField.addStyleNames("solid-border-grey", "add-participant-field-blue");
                clickableUserField.setHeight(50, Unit.PIXELS);
                clickableUserField.setWidth("20%");
                clickableUserField.setSpacing(false);
                clickableUserField.setMargin(false);
                Label userField = new Label(FontAwesome.USER_PLUS.getHtml(), ContentMode.HTML);
                userField.addStyleName("add-participant-field-clickable");
                /**
                 * init already set users fields from the db
                 */
                int numberParticipantsIndex = y;

                try {
                    CategoriesWrapper categoriesWrapperObject = categoriesWrapperRepository.findOne(categoriesWrapperList.get(categoriesListIndex).getId());
                    AssignedContact assignedContact = categoriesWrapperObject.getAssignedContacts().get(y);
                    gridContacts.get(timeLocationIndex).get(categoriesListIndex).set(y, assignedContact);
                    if (assignedContact.isParticipating()) {
                        clickableUserField.removeStyleName("add-participant-field-blue");
                        clickableUserField.addStyleName("add-participant-field-green");
                    }
                    userField.setValue(assignedContact.getContact().getFirstName() + "<br>" + assignedContact.getContact().getLastName());

                } catch (Exception e) {
                    /**
                     * reaching this exception means not all user field have been set up yet
                     */
                }


                clickableUserField.addComponent(userField);

                clickableUserField.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
                    @Override
                    public void layoutClick(LayoutEvents.LayoutClickEvent layoutClickEvent) {
                        PopupExtension popupExtension = PopupExtension.extend(userField);
                        popupExtension.addPopupVisibilityListener(new PopupExtension.PopupVisibilityListener() {
                            @Override
                            public void visibilityChanged(boolean isOpened) {
                                if (!isOpened) {
                                    selectedContact = null;
                                }
                            }
                        });

                        VerticalLayout wrapperLayout = new VerticalLayout();
                        wrapperLayout.setSizeFull();
                        wrapperLayout.setSpacing(false);
                        wrapperLayout.setMargin(false);

                        AppUI app = (AppUI) UI.getCurrent();
                        Users currentUser = usersRepository.findByUsername(app.getAccessControl().getUsername()).get(0);
                        List<Contact> contactList = new ArrayList<>();
                        /**
                         * Iterate through user contacts and only select contacts which are assigned to current category
                         */

                        List<Contact> contacts = currentUser.getContacts();

                        for (int k = 0; k < contacts.size(); k++) {
                            if (contacts.get(k).getAssignedCategory() != null) {
                                if (contacts.get(k).getAssignedCategory().getId() == categoriesWrapperList.get(categoriesListIndex).getCategoryId()) {


                                    /**
                                     * filter contacts which are already selected
                                     */
                                    boolean flag = true;
                                    CategoriesWrapper categoriesWrapperObject = categoriesWrapperRepository.findOne(categoriesWrapperList.get(categoriesListIndex).getId());
                                    List<AssignedContact> alreadyAssignedContacts = categoriesWrapperObject.getAssignedContacts();
                                    for (int a = 0; a < alreadyAssignedContacts.size(); a++) {
                                        if (contacts.get(k).getId().equals(alreadyAssignedContacts.get(a).getContact().getId())) {

                                            flag = false;
                                            break;
                                        }
                                    }
                                    if (flag) {
                                        contactList.add(contacts.get(k));
                                    }

                                }
                            }
                        }

                        Grid<Contact> contactsGrid = setUpContactsGrid();
                        contactsGrid.setItems(contactList);
                        contactsGrid.getSelectionModel().addSelectionListener(event -> {

                            boolean somethingSelected = !contactsGrid.getSelectedItems().isEmpty();
                            if (somethingSelected) {
                                selectedContact = event.getFirstSelectedItem().get();
                            }
                        });


                        CssLayout buttonsLayout = new CssLayout();
                        buttonsLayout.setWidth("100%");
                        LayoutEvents.LayoutClickListener listener = (LayoutEvents.LayoutClickListener) layoutClickEvent12 -> {
                            selectedContact = null;
                            popupExtension.close();

                        };
                        CustomButton cancelButton = new CustomButton("Cancel", listener);
                        cancelButton.setWidth("50%");
                        cancelButton.setHeight("100%");
                        cancelButton.addStyleNames("cancel-button");

                        listener = (LayoutEvents.LayoutClickListener) layoutClickEvent1 -> {

                            if (selectedContact == null) {
                                Notification.show("No contact selected",
                                        "Please select a contact before you continue",
                                        Notification.Type.WARNING_MESSAGE);
                            } else {

                                if (usersRepository.findByEmail(selectedContact.getEmail()) != null) {
                                    /**
                                     * We are fetching the categoriesWrapperObject from the db in order to ensure
                                     * that we are always working with an up to date object
                                     */
                                    CategoriesWrapper categoriesWrapperObject = categoriesWrapperRepository.findOne(categoriesWrapperList.get(categoriesListIndex).getId());
                                    System.out.println("CategoriesWrapperID = " + categoriesWrapperObject.getId());

                                    /**
                                     * If a participant was already picked for this slot we have to remove his
                                     * link to the event.
                                     */
                                    if (gridContacts.get(timeLocationIndex).get(categoriesListIndex).get(numberParticipantsIndex) != null) {
                                        System.out.println("GridContact Name + Assigned ID = " + gridContacts.get(timeLocationIndex).get(categoriesListIndex).get(numberParticipantsIndex).getContact().getFirstName() + " " + gridContacts.get(timeLocationIndex).get(categoriesListIndex).get(numberParticipantsIndex).getId());

                                        Users updatedUserObject = usersRepository.findByEmail(gridContacts.get(timeLocationIndex).get(categoriesListIndex).get(numberParticipantsIndex).getContact().getEmail());
                                        DailyEvent dailyEvent = updatedUserObject.findDailyEvent(day.getId());
                                        /**
                                         * When timeLocationList isn't of size 1 user is still added to a meeting at this day
                                         * TODO: REMOVE ASSIGNEDCONTACT ENTRY FROM DB
                                         */
                                        if (dailyEvent.getTimeLocationList().size() > 1) {
                                            dailyEvent.removeTimeLocationID(timeLocationWrapper.getId());
                                            dailyEvent.removeCategoryID(categoriesWrapperObject.getId());
                                            categoriesWrapperObject.removeAssignedContact(gridContacts.get(timeLocationIndex).get(categoriesListIndex).get(numberParticipantsIndex).getContact().getId());
                                            usersRepository.save(updatedUserObject);

                                        }
                                        /**
                                         * When timeLocationList is of size 1 user isn't added to a single meeting and the event
                                         * for the user can be removed from the user and the db
                                         */
                                        else {
                                            updatedUserObject.removeDailyEvent(dailyEvent.getId());
                                            categoriesWrapperObject.removeAssignedContact(gridContacts.get(timeLocationIndex).get(categoriesListIndex).get(numberParticipantsIndex).getContact().getId());
                                            usersRepository.save(updatedUserObject);
                                            dailyEventRepository.delete(dailyEvent.getId());


                                        }

                                    }
                                    AssignedContact newAssignedContact = new AssignedContact(selectedContact);
                                    categoriesWrapperObject.addAssignedContact(newAssignedContact);
                                    categoriesWrapperRepository.save(categoriesWrapperObject);


                                    /**
                                     *
                                     */
                                    userField.setValue(selectedContact.getFirstName() + "<br>" + selectedContact.getLastName());
                                    System.out.println("CategoryListIndex: " + categoriesListIndex + " NumberParticipantsIndex: " + numberParticipantsIndex);
                                    gridContacts.get(timeLocationIndex).get(categoriesListIndex).set(numberParticipantsIndex, newAssignedContact);
                                    /**
                                     * Create the event for the selected user so that he receives the invitation
                                     */
                                    Users targetUser = usersRepository.findByEmail(selectedContact.getEmail());
                                    if (targetUser != null) {
                                        DailyEvent dailyEvent = targetUser.findDailyEvent(day.getId());

                                        /**
                                         * User not part of any timeLocationWrapper of this daily event
                                         */
                                        if (dailyEvent == null) {
                                            DailyEvent newDailyEvent = new DailyEvent();
                                            newDailyEvent.setDateWrapperId(day.getId());
                                            newDailyEvent.addTimeLocationId(timeLocationWrapper.getId());
                                            newDailyEvent.addCategoriesWrapperId(categoriesWrapperObject.getId());
                                            newDailyEvent.setTitle(eventTitle);
                                            newDailyEvent.setDescription(eventDescription);
                                            targetUser.addDailyEvent(newDailyEvent);
                                            usersRepository.save(targetUser);
                                            Broadcaster.broadcast("newevent");
                                        }

                                        /**
                                         * User already part of this dailyevent with at least one timeLocationWrapper
                                         */
                                        else {
                                            dailyEvent.addTimeLocationId(timeLocationWrapper.getId());
                                            dailyEvent.addCategoriesWrapperId(categoriesWrapperObject.getId());
                                            dailyEvent.setHasBeenReadOnce(false);
                                            usersRepository.save(targetUser);
                                            Broadcaster.broadcast("newevent");

                                        }
                                    }

                                    /**
                                     * Reset the selected contact placeholder object and close the popup
                                     */
                                    selectedContact = null;
                                    popupExtension.close();
                                } else {
                                    Notification.show("No such user",
                                            "This contact is not yet registered",
                                            Notification.Type.WARNING_MESSAGE);
                                }

                            }
                        };
                        CustomButton okayButton = new CustomButton("Okay", listener);
                        okayButton.setWidth("50%");
                        okayButton.setHeight("100%");
                        okayButton.addStyleName("next-button");
                        buttonsLayout.addComponents(cancelButton, okayButton);
                        wrapperLayout.addComponents(contactsGrid, buttonsLayout);

                        popupExtension.setContent(wrapperLayout);
                        popupExtension.closeOnOutsideMouseClick(true);
                        popupExtension.open();

                    }
                });

                clickableUserField.setComponentAlignment(userField, Alignment.MIDDLE_CENTER);
                participantsLayout.addComponent(clickableUserField);
            }


            wrapperLayout.addComponent(headerLayout);
            wrapperLayout.addComponent(participantsLayout);
            addParticipantsLayout.addComponent(wrapperLayout);

        }
        return addParticipantsLayout;
    }

    private CssLayout createWindowHeader() {
        CssLayout headerLayout = new CssLayout();
        Label headerLabel = new Label("Select participants");
        headerLayout.setSizeUndefined();
        headerLayout.addComponent(headerLabel);
        headerLayout.addStyleName("select-participants-window-header");
        return headerLayout;
    }

    private CustomButton createBackButton() {
        LayoutEvents.LayoutClickListener listener = (LayoutEvents.LayoutClickListener) layoutClickEvent -> {
            close();
        };

        CustomButton backButton = new CustomButton(VaadinIcons.ARROW_CIRCLE_LEFT_O.getHtml() + " " + "BACK", listener);
        backButton.addStyleNames("modal-window-back-button");
        //back-button-margin
        return backButton;
    }

    private Grid setUpContactsGrid() {
        Grid<Contact> contactsGrid = new Grid<>(Contact.class);

        contactsGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        contactsGrid.removeAllColumns();
        String id = contactsGrid.addColumn(contact -> {
            // put your calculations for the column here
            return contact.getFirstName() + " " + contact.getLastName();
        }).getId();
        contactsGrid.removeHeaderRow(0);
//        contactsGrid.sort(id);
        contactsGrid.setWidth(200, Unit.PIXELS);
        contactsGrid.setHeight(200, Unit.PIXELS);

        return contactsGrid;
    }


}
