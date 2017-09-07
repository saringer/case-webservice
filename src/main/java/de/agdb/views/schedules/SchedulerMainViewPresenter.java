package de.agdb.views.schedules;

import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import de.agdb.AppUI;
import de.agdb.backend.data_model.DailyEvent;
import de.agdb.backend.data_model.Users;
import de.agdb.backend.data_model.repositories.RepositoryService;
import de.agdb.backend.data_model.schedule_wrapper_objects.*;
import de.agdb.custom_components.calendar_component.CalendarComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.vaadin.addons.ToastEasing;
import org.vaadin.addons.ToastPosition;
import org.vaadin.addons.ToastType;
import org.vaadin.addons.Toastr;
import org.vaadin.addons.builder.ToastBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.vaadin.addons.builder.ToastOptionsBuilder.having;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SchedulerMainViewPresenter implements Button.ClickListener{

    @Autowired
    RepositoryService repositoryService;
    private SchedulerMainView schedulerMainView;

    public CalendarComponent getCalendar() {
        return calendar;
    }

    public void setCalendar(CalendarComponent calendar) {
        this.calendar = calendar;
    }

    private CalendarComponent calendar;


    public Toastr getToastr() {
        return toastr;
    }

    public void setToastr(Toastr toastr) {
        this.toastr = toastr;
    }

    private Toastr toastr;

    public void bind() {

    }

    public SchedulerMainView getSchedulerMainView() {
        return schedulerMainView;
    }

    public void setSchedulerMainView(SchedulerMainView schedulerMainView) {
        this.schedulerMainView = schedulerMainView;
    }


    @Override
    public void buttonClick(Button.ClickEvent event) {

    }

    public void loadInvitationsNotification() {
        AppUI app = (AppUI) UI.getCurrent();
        if (app.getCurrentUsername() != null) {
            Users thisUser = repositoryService.getUsersRepository().findByUsername(app.getCurrentUsername()).get(0);
            int unreadInvitations = 0;
            for (int i = 0; i < thisUser.getEvents().size(); i++) {
                if (thisUser.getEvents().get(i).isHasBeenReadOnce() == false) {
                    unreadInvitations++;
                }
            }
            if (unreadInvitations > 0) {
                toastr.toast(
                        ToastBuilder.of(ToastType.valueOf("Info"), unreadInvitations + " unread invitations ")
                                //.caption("Title")
                                .options(having()
                                        .closeButton(false)
                                        .newestOnTop(true)
                                        .tapToDismiss(true)
                                        .position(ToastPosition.Top_Right)
                                        .rightToLeft(false)
                                        .timeOut(0)
                                        .extendedTimeOut(0)
                                        .hideDuration(0)
                                        .showEasing(ToastEasing.Swing)
                                        .build())
                                .build());
            }
        }

    }

    public void initCalendarEvents() {

        AppUI app = (AppUI) UI.getCurrent();

        if (!repositoryService.getUsersRepository().findByUsername(app.getCurrentUsername()).isEmpty()) {
            calendar.clearEvents(true);
            Users user = repositoryService.getUsersRepository().findByUsername(app.getCurrentUsername()).get(0);
            List<ScheduleWrapper> schedules = user.getSchedules();
            for (int i = 0; i < schedules.size(); i++) {
                for (int x = 0; x < schedules.get(i).getDays().size(); x++) {
                    calendar.addEvent(schedules.get(i).getDays().get(x), schedules.get(i).getTitle(), schedules.get(i).getDescription());
                }
            }
            for (int i = 0; i < user.getEvents().size(); i++) {
                DailyEvent dailyEvent = user.getEvents().get(i);
                DateWrapper day = repositoryService.getDateWrapperRepository().findOne(dailyEvent.getDateWrapperId());
                ArrayList<String> timeLocationHTMLStringList = new ArrayList<>();
                for (int x = 0; x < dailyEvent.getTimeLocationList().size(); x++) {
                    TimeLocationWrapper timeLocationWrapper = repositoryService.getTimeLocationWrapperRepository().findOne(dailyEvent.getTimeLocationList().get(x));
                    CategoriesWrapper categoriesWrapper = repositoryService.getCategoriesWrapperRepository().findOne(dailyEvent.getCategoriesWrapperList().get(x));
                    AssignedContact assignedContact = categoriesWrapper.findAssignedContact(user.getEmail());
                    if (timeLocationWrapper.isActive() && assignedContact.isParticipating()) {
                        String timeLocation = timeLocationWrapper.getFormattedStartTime() + " - " + timeLocationWrapper.getFormattedEndTime() +
                                "<br>" + timeLocationWrapper.getLocation();
                        timeLocationHTMLStringList.add(timeLocation);

                    }
                }
                if (timeLocationHTMLStringList.size() > 0) {
                    calendar.addInvitationEvent(day, timeLocationHTMLStringList, dailyEvent.getTitle(),dailyEvent.getDescription());
                }
            }
        }
    }
}
