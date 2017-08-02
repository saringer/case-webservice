package de.agdb.views.scheduler.create_schedule;

import com.vaadin.data.Binder;
import com.vaadin.event.LayoutEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import de.agdb.AppUI;
import de.agdb.backend.entities.Categories;
import de.agdb.backend.entities.Users;
import de.agdb.backend.entities.UsersRepository;
import de.agdb.views.scheduler.create_schedule.schedule_wrapper_objects.CategoriesWrapper;
import de.agdb.views.scheduler.create_schedule.schedule_wrapper_objects.DayWrapper;
import de.agdb.views.scheduler.create_schedule.schedule_wrapper_objects.GlobalWrapper;
import de.agdb.views.scheduler.create_schedule.schedule_wrapper_objects.TimeLocationWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.vaadin.ui.NumberField;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

@UIScope
@SpringView(name = SetCategoriesView.VIEW_NAME)
public class SetCategoriesView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "SetCategoriesView";
    private FormLayout content;
    private ComboBox selectCategory;
    private NumberField numberField;

    @Autowired
    UsersRepository repository;

    @PostConstruct
    void init() {
        setSizeFull();
        VerticalLayout formWrapper = new VerticalLayout();
        formWrapper.setWidth("80%");
        formWrapper.setHeight("80%");
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
    }

    private FormLayout buildContent(DayWrapper day) {

        FormLayout formLayout = new FormLayout();
        formLayout.setWidth("100%");

        Label header = new Label(day.getDay().toString());
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
                    Window setTimeLocationWindow = new Window();
                    setTimeLocationWindow.setModal(true);
                    setTimeLocationWindow.setResizable(false);
                    setTimeLocationWindow.setClosable(false);
                    setTimeLocationWindow.setWidth(400, Unit.PIXELS);
                    //  setTimeLocationWindow.setHeight(800, Unit.PIXELS);
                    setTimeLocationWindow.setCaption("Add a category");

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
                    numberField = new NumberField();
                    numberField.setLocale(Locale.getDefault());
                    numberField.setDecimalPrecision(2);
                    numberField.setDecimalSeparator(',');
                    numberField.setGroupingSeparator('.');
                    numberField.setDecimalSeparatorAlwaysShown(true);
                    numberField.setMinimumFractionDigits(2);
                    numberField.setMinValue(5);

                    Binder<Bean> binder = new Binder<>(Bean.class);
                    binder.forField(numberField)
                            .withConverter(NumberField.getConverter("Conversion error"))
                            .bind("number");
                    binder.setBean(new Bean());

                    selectLayout.addComponent(numberField);
                    selectLayout.setComponentAlignment(numberField, Alignment.MIDDLE_CENTER);
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


                    selectCategory = new ComboBox();
                    selectCategory.setWidth("80%");
                    selectCategory.setHeight("80%");
                    AppUI app = (AppUI) UI.getCurrent();
                    String userName = app.getAccessControl().getUsername();
                    Users thisUser = repository.findByUsername(userName).get(0);
                    List<String> list = new ArrayList();
                    Iterator iter = thisUser.getCategories().iterator();
                    while (iter.hasNext()) {
                        Categories category = (Categories) iter.next();
                        list.add(category.getTitle());

                    }




                    selectCategory.setItems(list);

                    selectCategoryLayout.addComponent(selectCategory);
                    selectCategoryLayout.setComponentAlignment(selectCategory, Alignment.MIDDLE_CENTER);

                    setCategoryLayout.addComponent(headerLayout);
                    setCategoryLayout.addComponent(selectCategoryLayout);


                /*
                    SET UP BUTTONLAYOUT

                 */

                    HorizontalLayout buttonLayout = new HorizontalLayout();
                    buttonLayout.setWidth("100%");
                    Button cancelButton = new Button("CANCEL");

                    cancelButton.addStyleName(ValoTheme.BUTTON_DANGER);
                    cancelButton.addClickListener(new Button.ClickListener() {
                        @Override
                        public void buttonClick(Button.ClickEvent clickEvent) {
                            setTimeLocationWindow.close();
                        }
                    });

                    Button okayButton = new Button("OKAY");
                    okayButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
                    okayButton.addClickListener(new Button.ClickListener() {
                        @Override
                        public void buttonClick(Button.ClickEvent clickEvent) {
                            CategoriesWrapper categoryWrapper = new CategoriesWrapper(Integer.parseInt(numberField.getValue()), (String) selectCategory.getValue());
                            day.getTimeAndLocationList().get(index).addCategory(categoryWrapper);
                            itemLayout.addComponent(buildItem(numberField.getValue(), (String) selectCategory.getValue(), itemLayout, categoryWrapper, day.getTimeAndLocationList().get(index)));
                            setTimeLocationWindow.close();
                        }
                    });

                    buttonLayout.addComponents(cancelButton, okayButton);
                    buttonLayout.setComponentAlignment(cancelButton, Alignment.MIDDLE_LEFT);
                    buttonLayout.setComponentAlignment(okayButton, Alignment.MIDDLE_RIGHT);
                    buttonLayout.addStyleNames("modal-window-margin");

                /*
                ADD COMPONENTS TO WRAPPERLAYOUT
                 */

                    wrapperLayout.addComponent(setNumberOfParticipants);
                    wrapperLayout.addComponent(setCategoryLayout);
                    wrapperLayout.addComponent(buttonLayout);

                    setTimeLocationWindow.setContent(wrapperLayout);

                    UI.getCurrent().addWindow(setTimeLocationWindow);


                }
            });
            plusButtonLayout.addComponent(plusButton);
            plusButtonLayout.addStyleName("add-button");
            plusButtonLayout.setHeight(52, Unit.PIXELS);
            plusButtonLayout.setWidth("24%");

        /* init already selected categories */
            for (int x = 0; x < day.getTimeAndLocationList().get(index).getCategoriesList().size(); x++) {
                TimeLocationWrapper timeLocationWrapper = day.getTimeAndLocationList().get(index);
                CategoriesWrapper object = timeLocationWrapper.getCategoriesList().get(x);
                itemLayout.addComponent(buildItem(Integer.toString(object.getNumberParticipants()), object.getCategory(), itemLayout, object, timeLocationWrapper));
            }

            HorizontalLayout horizontalWrapperLayout = new HorizontalLayout();
            horizontalWrapperLayout.setWidth("100%");
            horizontalWrapperLayout.setSpacing(false);
            horizontalWrapperLayout.setMargin(false);

            TimeLocationWrapper object = day.getTimeAndLocationList().get(i);
            String start = object.getStartTime();
            String end = object.getEndTime();
            String address = object.getLocation();

            VerticalLayout timeLocationHeader = new VerticalLayout();
            timeLocationHeader.setSizeUndefined();
            timeLocationHeader.setHeight("100%");
            timeLocationHeader.addStyleName("item-box");
            Label label = new Label(start + " - " + end + "<br>" + address);
            label.setSizeUndefined();
            label.setContentMode(ContentMode.HTML);
            timeLocationHeader.addComponent(label);
            timeLocationHeader.setComponentAlignment(label, Alignment.MIDDLE_CENTER);

            wrapperLayout.addComponent(itemLayout);
            wrapperLayout.addComponent(plusButtonLayout);


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
        GlobalWrapper globalScheduleWrapper = app.getGlobalScheduleWrapper();
        content.removeAllComponents();

        List<DayWrapper> days = globalScheduleWrapper.getDays();
        for (int i = 0; i < days.size(); i++) {
            content.addComponent(buildContent(days.get(i)));
        }


    }

    private CssLayout buildItem(String numberOfParticipants, String category, CssLayout itemLayout, CategoriesWrapper object, TimeLocationWrapper timeLocationWrapper) {
        CssLayout cssLayout = new CssLayout();
        Label textLabel = new Label(numberOfParticipants + "<br>" + category);
        textLabel.setSizeUndefined();
        textLabel.setContentMode(ContentMode.HTML);
        cssLayout.addComponent(textLabel);
        cssLayout.setStyleName("item-box");
        cssLayout.setHeight(52, Unit.PIXELS);
        cssLayout.setWidth("24%");

        CssLayout customDeleteButton = new CssLayout();
        customDeleteButton.setSizeUndefined();
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

        CssLayout dateBar = new CssLayout();
        dateBar.setWidth("100%");
        dateBar.setHeight(30, Unit.PIXELS);
        Label dateHeader = new Label("Step 2: Set date(s)");
        dateHeader.setSizeUndefined();
        dateBar.addComponent(dateHeader);
        dateBar.setStyleName("nav-top-passed");

        CssLayout timeLocationBar = new CssLayout();
        timeLocationBar.setWidth("100%");
        timeLocationBar.setHeight(30, Unit.PIXELS);
        Label timeLocationHeader = new Label("Step 3: Set time/location");
        timeLocationHeader.setSizeUndefined();
        timeLocationBar.addComponent(timeLocationHeader);
        timeLocationBar.setStyleName("nav-top-passed");

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
        Button createButton = new Button("CREATE SCHEDULE");
        createButton.setWidth(167, Unit.PIXELS);
        createButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                String callBackUrl = Page.getCurrent().getLocation().toString();
                if (callBackUrl.contains("#")) {
                    callBackUrl = callBackUrl.substring(0, callBackUrl.indexOf("#"));
                    UI.getCurrent().showNotification(callBackUrl);
                }

            }
        });
        createButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);

        Button backButton = new Button("BACK");
        backButton.setWidth(167, Unit.PIXELS);
        backButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                UI.getCurrent().getNavigator().navigateTo("TimeLocationView");
            }
        });
        backButton.addStyleName("back-button");


        nav.addComponent(backButton);
        nav.addComponent(createButton);
        //nav.addComponent(b);
        nav.setComponentAlignment(backButton, Alignment.MIDDLE_LEFT);
        nav.setComponentAlignment(createButton, Alignment.MIDDLE_RIGHT);
        // nav.setComponentAlignment(b, Alignment.MIDDLE_RIGHT);
        return nav;

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
