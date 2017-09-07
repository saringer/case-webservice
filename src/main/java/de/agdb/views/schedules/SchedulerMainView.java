package de.agdb.views.schedules;


import com.vaadin.event.LayoutEvents;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;

import com.vaadin.ui.themes.ValoTheme;
import de.agdb.AppUI;
import de.agdb.backend.data_model.DailyEvent;
import de.agdb.backend.data_model.Users;
import de.agdb.backend.data_model.repositories.*;
import de.agdb.backend.data_model.schedule_wrapper_objects.*;
import de.agdb.custom_components.calendar_component.CalendarComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.addons.*;
import org.vaadin.addons.builder.ToastBuilder;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static org.vaadin.addons.builder.ToastOptionsBuilder.having;

@UIScope
@SpringView(name = SchedulerMainView.VIEW_NAME)
public class SchedulerMainView extends VerticalLayout implements View, ToastrListener {
    // Empty view name as this will be the initially loaded view
    public static final String VIEW_NAME = "";
    private Toastr toastr;
    private CalendarComponent calendar;

    @Autowired
    CategoriesRepository categoriesRepository;
    @Autowired
    CategoriesWrapperRepository categoriesWrapperRepository;
    @Autowired
    TimeLocationWrapperRepository timeLocationWrapperRepository;
    @Autowired
    DailyEventRepository dailyEventRepository;
    @Autowired
    DateWrapperRepository dateWrapperRepository;
    @Autowired
    RepositoryService repositoryService;
    @Autowired
    SchedulerMainViewPresenter schedulerMainViewPresenter;


    @PostConstruct
    void init() {
        AppUI app = (AppUI) UI.getCurrent();
        this.toastr = app.getGlobalToastr();

        CssLayout breadCrumbs = createBreadCrumbs();
        breadCrumbs.setSizeUndefined();
        breadCrumbs.setWidth("80%");
        breadCrumbs.setStyleName("breadcrumbs");
        addComponent(breadCrumbs);
        setComponentAlignment(breadCrumbs, Alignment.MIDDLE_CENTER);

        setSizeFull();
        VerticalLayout formWrapper = new VerticalLayout();
        formWrapper.setSizeFull();
        addComponent(formWrapper);
        setComponentAlignment(formWrapper, Alignment.MIDDLE_CENTER);
        setExpandRatio(formWrapper, 0.95f);


        HorizontalLayout bottomNav = createBottomNav();
        bottomNav.setSizeUndefined();
        bottomNav.setWidth("100%");


        VerticalLayout form = new VerticalLayout();
        form.setWidth("80%");
        form.setHeight("80%");
        form.setMargin(false);
        form.setSpacing(false);
        //form.addStyleNames("solid-border");

        calendar = new CalendarComponent(categoriesWrapperRepository, repositoryService.getUsersRepository(), dailyEventRepository, timeLocationWrapperRepository);
        calendar.setSizeUndefined();
        calendar.setWidth("100%");
        calendar.setHeight("95%");
        calendar.setMargin(true);
        calendar.setSpacing(true);
        Panel calendarPanel = new Panel();
        calendarPanel.addStyleName("solid-border");
        calendarPanel.setSizeFull();
        calendarPanel.setContent(calendar);
        // calendar.setHeight(500, Unit.PIXELS);
        form.addComponent(calendarPanel);
        form.addComponent(bottomNav);
        form.setExpandRatio(calendarPanel, 1);
        //form.setExpandRatio(bottomNav, 0.2f);

//        form.setComponentAlignment(bottomNav, Alignment.TOP_CENTER);


        formWrapper.setMargin(false);
        formWrapper.setSpacing(false);
        formWrapper.addComponent(form);
        formWrapper.setComponentAlignment(form, Alignment.TOP_CENTER);
        setExpandRatio(formWrapper, 1);

        toastr.registerToastrListener(this);

        form.addComponent(toastr);

        //initCalendarEvents();
        initPresenter(schedulerMainViewPresenter);


    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        toastr.remove();
        if (event.getParameters().length() > 0) {
            toastr.toast(ToastBuilder.success("Event created").build());
        }
        schedulerMainViewPresenter.loadInvitationsNotification();
        schedulerMainViewPresenter.initCalendarEvents();
    }


    @Autowired
    public void initPresenter(SchedulerMainViewPresenter schedulerMainViewPresenter) {
        schedulerMainViewPresenter.setSchedulerMainView(this);
        schedulerMainViewPresenter.setToastr(toastr);
        schedulerMainViewPresenter.setCalendar(calendar);
        schedulerMainViewPresenter.bind();
    }


    private HorizontalLayout createBottomNav() {


        HorizontalLayout nav = new HorizontalLayout();


        VerticalLayout createSchedulesButton = new VerticalLayout();
        //createSchedulesButton.setSizeFull();
        createSchedulesButton.setWidth("100%");
        createSchedulesButton.addStyleName("left-menu-style");
        //createSchedulesButton.addComponent(leftMenuIcon);
        //createSchedulesButton.addComponent(leftMenuCaption);
        Label buttonLeft = new Label("Add event");
        buttonLeft.setSizeUndefined();
        buttonLeft.addStyleName(ValoTheme.LABEL_LARGE);
        createSchedulesButton.addComponent(buttonLeft);
        createSchedulesButton.addLayoutClickListener((LayoutEvents.LayoutClickListener) layoutClickEvent -> {
            UI.getCurrent().getNavigator().navigateTo("GeneralView");
        });

        VerticalLayout manageSchedulesButton = new VerticalLayout();
        //manageSchedulesButton.setSizeFull();
        manageSchedulesButton.setWidth("100%");
        manageSchedulesButton.addStyleName("right-menu-style");
        Label buttonRight = new Label("Manage schedules");
        buttonRight.setSizeUndefined();
        buttonRight.addStyleName(ValoTheme.LABEL_LARGE);
        manageSchedulesButton.addComponent(buttonRight);
        manageSchedulesButton.addLayoutClickListener((LayoutEvents.LayoutClickListener) layoutClickEvent -> {
            UI.getCurrent().getNavigator().navigateTo("ManageSchedulesView");
        });

        nav.addComponent(createSchedulesButton);
        nav.addComponent(manageSchedulesButton);
        nav.setMargin(false);
        nav.setSpacing(false);
        //nav.setExpandRatio(createSchedulesButton, 1);
        // nav.setExpandRatio(manageSchedulesButton, 1);
        return nav;
    }


    @Override
    public void onShown() {

    }

    @Override
    public void onHidden() {

        AppUI app = (AppUI) UI.getCurrent();


    }

    @Override
    public void onClick() {
        UI.getCurrent().getNavigator().navigateTo("InvitationsView");
    }

    @Override
    public void onCloseButtonClick() {

    }
    private CssLayout createBreadCrumbs() {
        CssLayout wrapperLayout = new CssLayout();
        Button topNavButton =  new Button("Schedule");
        topNavButton.addStyleName(ValoTheme.BUTTON_LINK);
        topNavButton.addClickListener((Button.ClickListener) event -> {
            UI.getCurrent().getNavigator().navigateTo("");
        });
        wrapperLayout.addComponent(topNavButton);
        Button placeHolder = new Button("> ...");
        placeHolder.setStyleName(ValoTheme.BUTTON_BORDERLESS);
        wrapperLayout.addComponent(placeHolder);
        return wrapperLayout;
    }



}
