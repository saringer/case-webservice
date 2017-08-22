package de.agdb.views.scheduler.modal_windows;


import com.vaadin.addon.onoffswitch.OnOffSwitch;
import com.vaadin.data.HasValue;
import com.vaadin.event.LayoutEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import de.agdb.backend.entities.Categories;
import de.agdb.backend.entities.repositories.CategoriesRepository;
import de.agdb.backend.entities.Contact;
import de.agdb.backend.entities.repositories.CategoriesWrapperRepository;
import de.agdb.backend.entities.repositories.TimeLocationWrapperRepository;
import de.agdb.backend.entities.schedule_wrapper_objects.CategoriesWrapper;
import de.agdb.backend.entities.schedule_wrapper_objects.DayWrapper;
import de.agdb.backend.entities.schedule_wrapper_objects.TimeLocationWrapper;
import de.agdb.views.scheduler.CustomButton;
import org.vaadin.addons.popupextension.PopupExtension;

import java.time.format.DateTimeFormatter;
import java.util.List;


public class SetParticipantsWindow extends Window {
    private DayWrapper day;
    private CategoriesRepository categoriesRepository;
    private Contact contact;
    private CategoriesWrapperRepository categoriesWrapperRepository;
    private TimeLocationWrapperRepository timeLocationWrapperRepository;


    public SetParticipantsWindow(DayWrapper day, CategoriesRepository categoriesRepository,
                                 CategoriesWrapperRepository categoriesWrapperRepository,
                                 TimeLocationWrapperRepository timeLocationWrapperRepository) {
        this.timeLocationWrapperRepository = timeLocationWrapperRepository;
        this.categoriesRepository = categoriesRepository;
        this.categoriesWrapperRepository = categoriesWrapperRepository;
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

        GridLayout gridLayout = setUpGridLayout();
        gridLayout.setSizeUndefined();
        gridLayout.setWidth("100%");
        Label dayLabel = new Label(day.getDay().format(DateTimeFormatter.ofPattern("EEEE, dd.MM.yyyy")));
        dayLabel.addStyleName("day-header");
        contentLayout.addComponent(dayLabel);
        contentLayout.addComponent(gridLayout);


        contentLayout.setExpandRatio(gridLayout, 1);
        contentLayout.addStyleName("overflow-auto");

        CustomButton backButton = createBackButton();
        backButton.setWidth(167, Unit.PIXELS);
        backButton.setHeight(40, Unit.PIXELS);
        CssLayout buttonLayout = new CssLayout();
        buttonLayout.setWidth("90%");
        buttonLayout.addComponent(backButton);


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
            gridLayout.addComponent(createParticipantsLayout(day.getTimeAndLocationList().get(i)));

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
            OnOffSwitch onOffSwitch = new OnOffSwitch(false);
            onOffSwitch.addValueChangeListener(new HasValue.ValueChangeListener<Boolean>() {
                @Override
                public void valueChange(HasValue.ValueChangeEvent<Boolean> event) {
                            if (event.getValue() == true) {

                                calendarIconLabel.setStyleName("status-green");
                            }
                            else {

                                calendarIconLabel.setStyleName("status-grey");
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

    private VerticalLayout createParticipantsLayout(TimeLocationWrapper timeLocationWrapper) {
        VerticalLayout addParticipantsLayout = new VerticalLayout();
        addParticipantsLayout.addStyleName("solid-border-grey");
        addParticipantsLayout.setMargin(true);
        addParticipantsLayout.setSpacing(false);
        addParticipantsLayout.setSizeUndefined();
        addParticipantsLayout.setWidth("100%");
        List<CategoriesWrapper> categoriesList = timeLocationWrapper.getCategoriesList();

        for (int x = 0; x < categoriesList.size(); x++) {
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
            Label categoryTitle = new Label(categoriesList.get(x).getCategoryTitle());
            categoryTitle.setSizeUndefined();
            headerLayout.addComponent(categoryTitle);
            headerLayout.setComponentAlignment(categoryTitle, Alignment.MIDDLE_CENTER);
            headerLayout.addStyleNames("solid-border-grey", "category-header");

            // Participants Layout
            CssLayout participantsLayout = new CssLayout();
            participantsLayout.setSizeUndefined();
            participantsLayout.setWidth("100%");

            for (int y = 0; y < categoriesList.get(x).getNumberParticipants(); y++) {

                int categoriesListIndex = x;

                VerticalLayout clickableFieldLayout = new VerticalLayout();
                clickableFieldLayout.addStyleNames("solid-border-grey", "add-participant-field-blue");
                clickableFieldLayout.setHeight(50, Unit.PIXELS);
                clickableFieldLayout.setWidth("20%");
                clickableFieldLayout.setSpacing(false);
                clickableFieldLayout.setMargin(false);
                Label user = new Label(FontAwesome.USER_PLUS.getHtml(), ContentMode.HTML);
                user.addStyleName("add-participant-field-clickable");
                clickableFieldLayout.addComponent(user);
                clickableFieldLayout.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
                    @Override
                    public void layoutClick(LayoutEvents.LayoutClickEvent layoutClickEvent) {
                        PopupExtension popupExtension = PopupExtension.extend(user);
                        List<Categories> list = categoriesRepository.findByTitle(categoriesList.get(categoriesListIndex).getCategoryTitle());
                        Categories category = list.get(0);

                        VerticalLayout wrapperLayout = new VerticalLayout();
                        wrapperLayout.setSizeFull();
                        wrapperLayout.setSpacing(false);
                        wrapperLayout.setMargin(false);

                        Grid<Contact> contactsGrid = new Grid<>(Contact.class);

                        contactsGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
                        contactsGrid.setColumns("firstName", "lastName");
                        contactsGrid.setItems(category.getContacts());
                        contactsGrid.setWidth(300, Unit.PIXELS);
                        contactsGrid.setHeight(300, Unit.PIXELS);
                        contactsGrid.getSelectionModel().addSelectionListener(event -> {

                            boolean somethingSelected = !contactsGrid.getSelectedItems().isEmpty();
                            if (somethingSelected) {
                                contact = event.getFirstSelectedItem().get();
                            }

                        });
                        CssLayout buttonsLayout = new CssLayout();
                        buttonsLayout.setWidth("100%");
                        LayoutEvents.LayoutClickListener listener = (LayoutEvents.LayoutClickListener) layoutClickEvent12 -> {
                            popupExtension.close();
                        };
                        CustomButton cancelButton = new CustomButton("Cancel", listener);
                        cancelButton.setWidth("50%");
                        cancelButton.setHeight("100%");
                        cancelButton.addStyleNames("cancel-button");
                        listener = (LayoutEvents.LayoutClickListener) layoutClickEvent1 -> {
                            System.out.println(contact.getFirstName());
                            if (contact == null) {
                                Notification.show("No contact selected",
                                        "Please select a contact before you continue",
                                        Notification.Type.WARNING_MESSAGE);
                            } else {
                                user.setValue(contact.getFirstName() + "<br>" + contact.getLastName());
                                /**
                                 * We are fetching the categoriesWrapperObject from the db in order to ensure
                                 * that we are always working with an up to date object
                                 */
                                CategoriesWrapper categoriesWrapperObject = categoriesWrapperRepository.findById(categoriesList.get(categoriesListIndex).getId()).get(0);

                                categoriesWrapperObject.addAssignedContact(contact);
                                categoriesWrapperRepository.save(categoriesWrapperObject);
                                //timeLocationWrapperRepository.save(timeLocationWrapper);
                                popupExtension.close();

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

                clickableFieldLayout.setComponentAlignment(user, Alignment.MIDDLE_CENTER);
                participantsLayout.addComponent(clickableFieldLayout);
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
        backButton.addStyleNames("back-button", "back-button-margin");
        return backButton;
    }


}
