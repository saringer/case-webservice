package de.agdb.views.scheduler.create_schedule;

import com.vaadin.event.LayoutEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import de.agdb.AppUI;
import de.agdb.backend.entities.Categories;
import de.agdb.backend.entities.Users;
import de.agdb.backend.entities.repositories.UsersRepository;
import de.agdb.backend.entities.schedule_wrapper_objects.CategoriesWrapper;
import de.agdb.backend.entities.schedule_wrapper_objects.DayWrapper;
import de.agdb.backend.entities.schedule_wrapper_objects.ScheduleWrapper;
import de.agdb.backend.entities.schedule_wrapper_objects.TimeLocationWrapper;
import de.agdb.views.scheduler.CustomButton;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.addons.*;
import org.vaadin.addons.builder.ToastBuilder;
import org.vaadin.risto.stepper.IntStepper;


import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.vaadin.addons.builder.ToastOptionsBuilder.having;

@UIScope
@SpringView(name = SetCategoriesView.VIEW_NAME)
public class SetCategoriesView extends VerticalLayout implements View, ToastrListener {

    public static final String VIEW_NAME = "SetCategoriesView";
    private FormLayout content;
    Toastr toastr = new Toastr();


    @Autowired
    UsersRepository usersRepository;


    @PostConstruct
    void init() {
        toastr.registerToastrListener(this);

        setSizeFull();
        VerticalLayout formWrapper = new VerticalLayout();
        formWrapper.setWidth(1150, Unit.PIXELS);
        formWrapper.setHeight(650, Unit.PIXELS);
        addComponent(formWrapper);
        setComponentAlignment(formWrapper, Alignment.MIDDLE_CENTER);


        content = new FormLayout();
        content.setHeight("80%");
        content.setWidth("90%");
        content.addStyleNames("overflow-auto");

        // content.addComponent(buildContent("sdsd"));
        // content.addComponent( buildContent("String"));


        HorizontalLayout topNavBar = createTopNavBar();

        HorizontalLayout bottomNavBar = createBottomNav();

        formWrapper.addComponent(topNavBar);
        //centeringLayout.setExpandRatio(topNavBar, 1);
        formWrapper.addStyleName("solid-border");
        //formWrapper.addStyleName("overflow-auto");
        formWrapper.setSpacing(false);
        formWrapper.setMargin(false);
        formWrapper.addComponent(content);
        //centeringLayout.addComponent(new Label("sadsdd"));
        formWrapper.addComponent(bottomNavBar);
        formWrapper.setComponentAlignment(content, Alignment.MIDDLE_CENTER);
        //formWrapper.setComponentAlignment(button,Alignment.BOTTOM_RIGHT);
        formWrapper.setExpandRatio(content, 1);
        formWrapper.addComponent(toastr);

    }

    private FormLayout buildContent(DayWrapper day) {

        FormLayout formLayout = new FormLayout();
        formLayout.setWidth("100%");

        Label header = new Label(day.getDay().format(DateTimeFormatter.ofPattern("EEEE, dd.MM.yyyy")));
        header.addStyleNames("h3", "colored");
        formLayout.addComponent(header);

        for (int i = 0; i < day.getTimeAndLocationList().size(); i++) {
            final int index = i;


            CssLayout wrapperLayout = new CssLayout();
            wrapperLayout.setWidth("100%");


            CssLayout itemLayout = new CssLayout();
            itemLayout.setWidth("100%");


            CssLayout plusButtonLayout = new CssLayout();
            Button plusButton = new Button();
            plusButton.setSizeUndefined();
            plusButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
            plusButton.setIcon(VaadinIcons.PLUS);
            plusButton.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    Window setCategoriesWindow = new Window();
                    setCategoriesWindow.setModal(true);
                    setCategoriesWindow.setResizable(false);
                    setCategoriesWindow.setClosable(false);
                    setCategoriesWindow.setWidth(400, Unit.PIXELS);
                    //  setTimeLocationWindow.setHeight(800, Unit.PIXELS);
                    setCategoriesWindow.setCaption("Add a category");

                    VerticalLayout wrapperLayout = new VerticalLayout();
                    wrapperLayout.setSpacing(false);
                    wrapperLayout.setMargin(true);
                    wrapperLayout.setSizeUndefined();
                    wrapperLayout.setWidth(400, Unit.PIXELS);

                /* SET UP NUMBERPICKER
                */

                    VerticalLayout setNumberOfParticipants = new VerticalLayout();
                    setNumberOfParticipants.setSpacing(false);
                    setNumberOfParticipants.setMargin(false);

                    VerticalLayout headerLayout = new VerticalLayout();
                    headerLayout.setMargin(false);
                    headerLayout.setSpacing(false);
                    headerLayout.setWidth("100%");
                    headerLayout.setHeight(30, Unit.PIXELS);
                    headerLayout.addComponent(new Label("Set the number of participants"));
                    headerLayout.addStyleNames("modal-window-header");
                    headerLayout.addStyleName("solid-border");
                    VerticalLayout selectLayout = new VerticalLayout();
                    selectLayout.setWidth("100%");
                    selectLayout.setHeight(60, Unit.PIXELS);
                    selectLayout.setMargin(false);
                    selectLayout.setSpacing(false);

                    IntStepper intStepper = new IntStepper();
                    intStepper.setWidth(50, Unit.PIXELS);
                    intStepper.setValue(1);
                    intStepper.setMinValue(1);
                    intStepper.setMaxValue(99);
                    intStepper.setStepAmount(1);
                    //intStepper.setMinValue(1);
                    //intStepper.setMaxValue(100);


                    selectLayout.addComponent(intStepper);
                    selectLayout.setComponentAlignment(intStepper, Alignment.MIDDLE_LEFT);
                    selectLayout.addStyleNames("modal-window-content");
                    selectLayout.addStyleNames("solid-border");


                    setNumberOfParticipants.addComponent(headerLayout);
                    setNumberOfParticipants.addComponent(selectLayout);


                /*
                SET UP CATEGORYPICKER
                 */

                    VerticalLayout setCategoryLayout = new VerticalLayout();
                    setCategoryLayout.setSpacing(false);
                    setCategoryLayout.setMargin(false);

                    headerLayout = new VerticalLayout();
                    headerLayout.setMargin(false);
                    headerLayout.setSpacing(false);
                    headerLayout.setWidth("100%");
                    headerLayout.setHeight(30, Unit.PIXELS);
                    headerLayout.addComponent(new Label("Select category"));
                    headerLayout.addStyleNames("modal-window-header");
                    headerLayout.addStyleName("solid-border");


                    VerticalLayout selectCategoryLayout = new VerticalLayout();
                    selectCategoryLayout.setSizeFull();
                    selectCategoryLayout.addStyleNames("modal-window-content", "solid-border");


                    Grid selectCategory = new Grid(Categories.class);
                    selectCategory.setSelectionMode(Grid.SelectionMode.SINGLE);
                    selectCategory.setColumns("title");
                    selectCategory.removeHeaderRow(0);
                    selectCategory.setWidth("90%");
                    selectCategory.setHeight(400, Unit.PIXELS);
                    AppUI app = (AppUI) UI.getCurrent();
                    String userName = app.getAccessControl().getUsername();
                    Users thisUser = usersRepository.findByUsername(userName).get(0);
                    selectCategory.setItems(thisUser.getCategories());


                    selectCategoryLayout.addComponent(selectCategory);
                    selectCategoryLayout.setComponentAlignment(selectCategory, Alignment.MIDDLE_CENTER);
                    selectCategoryLayout.setMargin(true);
                    selectCategoryLayout.setSpacing(false);

                    setCategoryLayout.addComponent(headerLayout);
                    setCategoryLayout.addComponent(selectCategoryLayout);

                    LayoutEvents.LayoutClickListener listener = (LayoutEvents.LayoutClickListener) layoutClickEvent -> {
                        setCategoriesWindow.close();
                        UI.getCurrent().getNavigator().navigateTo("AddCategoryView");
                        //Notification.show("asdsd");

                    };
                    CustomButton createCategoryButton = new CustomButton("Create new category", listener);
                    createCategoryButton.addStyleName("next-button");
                    createCategoryButton.setHeight(30, Unit.PIXELS);
                    // createCategoryButton.setWidth(120, Unit.PIXELS);
                    createCategoryButton.setWidth("90%");
                    selectCategoryLayout.addComponent(createCategoryButton);
                    selectCategoryLayout.setExpandRatio(selectCategory,1);


                /*
                    SET UP BUTTONLAYOUT

                 */

                    HorizontalLayout buttonLayout = new HorizontalLayout();
                    buttonLayout.setSpacing(false);
                    buttonLayout.setMargin(false);
                    buttonLayout.setWidth("100%");

                    listener = (LayoutEvents.LayoutClickListener) layoutClickEvent -> {
                        setCategoriesWindow.close();
                       /* toastr.toast(
                                ToastBuilder.of(ToastType.valueOf("Info"), "9 unread invitations")
                                        //.caption("Title")
                                        .options(having()
                                                .closeButton(true)
                                                .debug(false)
                                                .preventDuplicates(false)
                                                .newestOnTop(true)
                                                .tapToDismiss(false)
                                                .position(ToastPosition.Top_Right)
                                                .rightToLeft(false)
                                                //.showEasing(ToastEasing.find(showEasingField.getValue()).orElse(ToastEasing.Swing))
                                                //.hideEasing(ToastEasing.find(hideEasingField.getValue()).orElse(ToastEasing.Linear))
                                                //.showDuration(Integer.valueOf(showDurationField.getValue()))
                                                //.hideDuration(Integer.valueOf(hideDurationField.getValue()))
                                                .showMethod(ToastDisplayMethod.find("fadeIn").orElse(ToastDisplayMethod.FadeIn))
                                                .hideMethod(ToastDisplayMethod.find("fadeOut").orElse(ToastDisplayMethod.FadeOut))
                                                //.timeOut(Integer.valueOf(timeOutField.getValue()))
                                                //.extendedTimeOut(Integer.valueOf(extendedTimeOutField.getValue()))
                                                .build())
                                        .build());
                     */
                    };
                    CustomButton cancelButton = new CustomButton(VaadinIcons.CLOSE.getHtml(), listener);
                    cancelButton.addStyleName("cancel-button");
                    cancelButton.setHeight(40, Unit.PIXELS);
                    //cancelButton.setWidth(120, Unit.PIXELS);
                    cancelButton.setWidth("100%");

                    listener = (LayoutEvents.LayoutClickListener) layoutClickEvent -> selectCategory.getSelectionModel().getFirstSelectedItem().ifPresent(item -> {
                        Categories categoryItem = (Categories) item;
                        String categoryTitle = categoryItem.getTitle();
                        CategoriesWrapper categoryWrapper = new CategoriesWrapper(intStepper.getValue(), categoryTitle);
                        day.getTimeAndLocationList().get(index).addCategory(categoryWrapper);
                        itemLayout.addComponent(buildItem(intStepper.getValue(), categoryTitle, itemLayout, categoryWrapper, day.getTimeAndLocationList().get(index)));
                        setCategoriesWindow.close();
                    });
                    CustomButton okayButton = new CustomButton(VaadinIcons.CHECK.getHtml(), listener);
                    okayButton.addStyleName("next-button");
                    okayButton.setHeight(40, Unit.PIXELS);
                    //okayButton.setWidth(120, Unit.PIXELS);
                    okayButton.setWidth("100%");




                    buttonLayout.addComponents(cancelButton, okayButton);
                  //  buttonLayout.setComponentAlignment(cancelButton, Alignment.MIDDLE_LEFT);
                   // buttonLayout.setComponentAlignment(createCategoryButton, Alignment.MIDDLE_CENTER);
                   // buttonLayout.setComponentAlignment(okayButton, Alignment.MIDDLE_RIGHT);
                    buttonLayout.addStyleNames("modal-window-margin");

                /*
                ADD COMPONENTS TO WRAPPERLAYOUT
                 */

                    wrapperLayout.addComponent(setNumberOfParticipants);
                    wrapperLayout.addComponent(setCategoryLayout);
                    wrapperLayout.addComponent(buttonLayout);

                    setCategoriesWindow.setContent(wrapperLayout);

                    UI.getCurrent().addWindow(setCategoriesWindow);


                }
            });
            plusButtonLayout.addComponent(plusButton);
            plusButtonLayout.addStyleName("add-button");
            //plusButtonLayout.setHeight(52, Unit.PIXELS);
            plusButtonLayout.setHeight("100%");
            plusButtonLayout.setWidth("24%");

        /* init already selected categories */
            for (int x = 0; x < day.getTimeAndLocationList().get(index).getCategoriesList().size(); x++) {
                TimeLocationWrapper timeLocationWrapper = day.getTimeAndLocationList().get(index);
                CategoriesWrapper object = timeLocationWrapper.getCategoriesList().get(x);
                itemLayout.addComponent(buildItem(object.getNumberParticipants(), object.getCategoryTitle(), itemLayout, object, timeLocationWrapper));
            }

            HorizontalLayout horizontalWrapperLayout = new HorizontalLayout();
            horizontalWrapperLayout.setWidth("100%");
            horizontalWrapperLayout.setSpacing(false);
            horizontalWrapperLayout.setMargin(false);

            TimeLocationWrapper object = day.getTimeAndLocationList().get(i);
            String startTime = object.getFormattedStartTime();
            String endTime = object.getFormattedEndTime();
            String street = object.getStreet();
            String streetNumber = object.getStreetNumber();

            VerticalLayout timeLocationHeader = new VerticalLayout();
            timeLocationHeader.setSizeUndefined();
            timeLocationHeader.setHeight("100%");
            timeLocationHeader.addStyleName("item-box-blue");
            Label label = new Label(startTime + " - " + endTime + "<br>" + street + " " + streetNumber);
            label.setSizeUndefined();
            label.setContentMode(ContentMode.HTML);
            timeLocationHeader.addComponent(label);
            timeLocationHeader.setComponentAlignment(label, Alignment.MIDDLE_CENTER);


            wrapperLayout.addComponent(itemLayout);
            wrapperLayout.addComponent(plusButtonLayout);
            //itemLayout.addComponent(plusButtonLayout);


            horizontalWrapperLayout.addComponents(timeLocationHeader);
            horizontalWrapperLayout.addComponents(wrapperLayout);
            horizontalWrapperLayout.setExpandRatio(wrapperLayout, 1);

            //  formLayout.addComponent(wrapperLayout);
            formLayout.addComponent(horizontalWrapperLayout);

        }


        return formLayout;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        initContent();

    }

    private void initContent() {
        AppUI app = (AppUI) UI.getCurrent();
        ScheduleWrapper globalScheduleWrapper = app.getGlobalScheduleWrapper();
        content.removeAllComponents();

        List<DayWrapper> days = globalScheduleWrapper.getDays();
        for (int i = 0; i < days.size(); i++) {
            content.addComponent(buildContent(days.get(i)));
        }


    }

    private CssLayout buildItem(int numberOfParticipants, String category, CssLayout itemLayout, CategoriesWrapper object, TimeLocationWrapper timeLocationWrapper) {
        CssLayout cssLayout = new CssLayout();
        Label textLabel = new Label(numberOfParticipants + "<br>" + category);
        textLabel.setSizeUndefined();
        textLabel.setContentMode(ContentMode.HTML);
        cssLayout.addComponent(textLabel);
        cssLayout.setStyleName("item-box");
        //cssLayout.setHeight(52, Unit.PIXELS);
        cssLayout.setHeight("100%");
        cssLayout.setWidth("24%");

        CssLayout customDeleteButton = new CssLayout();
        customDeleteButton.setWidth(20, Unit.PIXELS);
        customDeleteButton.setHeight(20, Unit.PIXELS);
        Label test = new Label(VaadinIcons.CLOSE_SMALL.getHtml());
        test.setSizeUndefined();
        test.setContentMode(ContentMode.HTML);
        customDeleteButton.addComponent(test);
        customDeleteButton.addStyleNames("topcorner-delete-button", "solid-border");
        customDeleteButton.addLayoutClickListener((LayoutEvents.LayoutClickListener) layoutClickEvent -> {
            itemLayout.removeComponent(cssLayout);
            timeLocationWrapper.removeCategory(object);

        });


        cssLayout.addComponent(customDeleteButton);
        return cssLayout;
    }

    private HorizontalLayout createTopNavBar() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setWidth("100%");
        horizontalLayout.setHeight(30, Unit.PIXELS);

        CssLayout generalBar = new CssLayout();
        generalBar.setWidth("100%");
        generalBar.setHeight(30, Unit.PIXELS);
        Label generalHeader = new Label("Step 1: General");
        generalHeader.setSizeUndefined();
        generalBar.addComponent(generalHeader);
        generalBar.setStyleName("nav-top-passed");
        generalBar.addLayoutClickListener((LayoutEvents.LayoutClickListener) layoutClickEvent -> UI.getCurrent().getNavigator().navigateTo("GeneralView"));


        CssLayout dateBar = new CssLayout();
        dateBar.setWidth("100%");
        dateBar.setHeight(30, Unit.PIXELS);
        Label dateHeader = new Label("Step 2: Set date(s)");
        dateHeader.setSizeUndefined();
        dateBar.addComponent(dateHeader);
        dateBar.setStyleName("nav-top-passed");
        dateBar.addLayoutClickListener((LayoutEvents.LayoutClickListener) layoutClickEvent -> UI.getCurrent().getNavigator().navigateTo("DateView"));


        CssLayout timeLocationBar = new CssLayout();
        timeLocationBar.setWidth("100%");
        timeLocationBar.setHeight(30, Unit.PIXELS);
        Label timeLocationHeader = new Label("Step 3: Set time/location");
        timeLocationHeader.setSizeUndefined();
        timeLocationBar.addComponent(timeLocationHeader);
        timeLocationBar.setStyleName("nav-top-passed");
        timeLocationBar.addLayoutClickListener((LayoutEvents.LayoutClickListener) layoutClickEvent -> UI.getCurrent().getNavigator().navigateTo("TimeLocationView"));

        CssLayout categoriesBar = new CssLayout();
        categoriesBar.setWidth("100%");
        categoriesBar.setHeight(30, Unit.PIXELS);
        Label categoriesHeader = new Label("Step 4: Set categories");
        categoriesHeader.setSizeUndefined();
        categoriesBar.addComponent(categoriesHeader);
        categoriesBar.setStyleName("nav-top-active");

        horizontalLayout.addComponent(generalBar);
        horizontalLayout.addComponent(dateBar);
        horizontalLayout.addComponent(timeLocationBar);
        horizontalLayout.addComponent(categoriesBar);
        horizontalLayout.setSpacing(false);


        //horizontalLayout.setExpandRatio(generalBar, 1);
        return horizontalLayout;
    }

    public HorizontalLayout createBottomNav() {
        HorizontalLayout nav = new HorizontalLayout();
        nav.setWidth("100%");
        nav.setSpacing(false);
        nav.setMargin(false);
        LayoutEvents.LayoutClickListener listener = (LayoutEvents.LayoutClickListener) layoutClickEvent -> {
            AppUI app = (AppUI) UI.getCurrent();
            List<DayWrapper> days = app.getGlobalScheduleWrapper().getDays();

            Boolean flag = true;

            for (int x = 0; x < days.size(); x++) {
                if (flag == false) {
                    break;
                }
                List<TimeLocationWrapper> timeLocation = days.get(x).getTimeAndLocationList();
                for (int i = 0; i < timeLocation.size(); i++) {
                    if (timeLocation.get(i).getCategoriesList().isEmpty()) {
                        Notification.show("Missing category entry", "Please set the categories before creating the schedule", Notification.Type.WARNING_MESSAGE);
                        flag = false;
                        break;
                    }
                }
            }

            if (flag) {
                String userName = app.getAccessControl().getUsername();
                Users thisUser = usersRepository.findByUsername(userName).get(0);
                thisUser.addSchedule(app.getGlobalScheduleWrapper());
                usersRepository.save(thisUser);

                // Clear the ScheduleWrapper
                app.resetGlobalScheduleWrapper();
                app.getNavigator().navigateTo(""+ '/' + "true");
            }
        };

        CustomButton createButton = new CustomButton("CREATE SCHEDULE", listener);
        createButton.setWidth(167, Unit.PIXELS);
        createButton.addStyleName("create-button");


        listener = (LayoutEvents.LayoutClickListener) layoutClickEvent -> {
            UI.getCurrent().getNavigator().navigateTo("TimeLocationView");
        };

        CustomButton backButton = new CustomButton(VaadinIcons.ARROW_CIRCLE_LEFT_O.getHtml() + " " + "BACK", listener);
        backButton.setWidth(167, Unit.PIXELS);
        backButton.setHeight(40, Unit.PIXELS);
        backButton.addStyleName("back-button");


        nav.addComponent(backButton);
        nav.addComponent(createButton);
        //nav.addComponent(b);
        nav.setComponentAlignment(backButton, Alignment.MIDDLE_LEFT);
        nav.setComponentAlignment(createButton, Alignment.MIDDLE_RIGHT);
        // nav.setComponentAlignment(b, Alignment.MIDDLE_RIGHT);
        return nav;

    }

    @Override
    public void onShown() {

    }

    @Override
    public void onHidden() {

    }

    @Override
    public void onClick() {
        Notification.show("ANGEKLICKT YIPPI");
    }

    @Override
    public void onCloseButtonClick() {

    }


    public class Bean implements Serializable {
        Double number = 1d;

        public Double getNumber() {
            return number;
        }

        public void setNumber(Double number) {
            this.number = number;
        }


    }


}
