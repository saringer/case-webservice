package de.agdb.custom_components.modal_windows;

import com.vaadin.event.LayoutEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import de.agdb.backend.data_model.schedule_wrapper_objects.DateWrapper;
import de.agdb.custom_components.CustomButton;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class InvitationWindow extends Window{
    private DateWrapper day;

    public InvitationWindow(DateWrapper day, ArrayList<String> timeLocationList) {
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
        for (int i=0;i<timeLocationList.size();i++) {
            Label label = new Label();
            label.setContentMode(ContentMode.HTML);
            label.setValue(timeLocationList.get(i));
            contentLayout.addComponent(label);
        }


        wrapperPanel.setContent(contentLayout);


        CustomButton backButton = createBackButton();
        backButton.setWidth(167, Sizeable.Unit.PIXELS);
        backButton.setHeight(40, Sizeable.Unit.PIXELS);
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setHeight(60, Sizeable.Unit.PIXELS);
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

    private CssLayout createWindowHeader() {
        CssLayout headerLayout = new CssLayout();
        Label headerLabel = new Label(day.getDay().format(DateTimeFormatter.ofPattern("EEEE, dd.MM.yyyy")));
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
}
