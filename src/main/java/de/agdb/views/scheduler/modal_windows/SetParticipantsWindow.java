package de.agdb.views.scheduler.modal_windows;


import com.vaadin.addon.onoffswitch.OnOffSwitch;
import com.vaadin.event.LayoutEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

public class SetParticipantsWindow extends Window {

    public SetParticipantsWindow() {
        setModal(true);
        center();
        //setClosable(false);
        setResizable(false);

        VerticalLayout rootLayout = new VerticalLayout();
        rootLayout.setSizeFull();
        rootLayout.setMargin(false);
        rootLayout.setSpacing(false);

        CssLayout cssLayout = createWindowHeader();
        cssLayout.setWidth("100%");
        cssLayout.setHeight(50, Sizeable.Unit.PIXELS);

        VerticalLayout innerLayout = new VerticalLayout();
        innerLayout.setMargin(true);
        innerLayout.setSpacing(false);
        innerLayout.setHeight("80%");
        innerLayout.setWidth("90%");

        GridLayout gridLayout = setUpGridLayout();
        gridLayout.setSizeUndefined();
        gridLayout.setWidth("100%");
        innerLayout.addComponent(new Label("Monday, 12.02.17"));
        innerLayout.addComponent(gridLayout);
        //innerLayout.addComponent(new Button("BACK"));
        innerLayout.setExpandRatio(gridLayout, 1);
        innerLayout.addStyleName("overflow-auto");


        rootLayout.addComponent(cssLayout);
        rootLayout.addComponent(innerLayout);
        rootLayout.setComponentAlignment(innerLayout, Alignment.MIDDLE_CENTER);
        rootLayout.setExpandRatio(innerLayout, 1);


        setContent(rootLayout);

    }

    private GridLayout setUpGridLayout() {
        GridLayout gridLayout = new GridLayout(4,3);

        //gridLayout.setColumnExpandRatio(1, 0.2f);
        //gridLayout.setRowExpandRatio(1,1);
        gridLayout.setColumnExpandRatio(0, 0.15f);
        gridLayout.setColumnExpandRatio(1, 0.65f);
        gridLayout.setColumnExpandRatio(2, 0.1f);
        gridLayout.setColumnExpandRatio(3, 0.1f);


        VerticalLayout wrapperLayout;

        // Add some content
        String headerLabels [] = {
                "Time/Location",
                "Participants",
                "Status",
                "Select",};
        for (int i=0; i<headerLabels.length; i++) {
            wrapperLayout = new VerticalLayout();
            wrapperLayout.setSizeFull();
            wrapperLayout.setSpacing(false);
            wrapperLayout.setMargin(false);

            Label label = new Label(headerLabels[i]);
            label.addStyleNames(ValoTheme.LABEL_H3);
            label.addStyleNames(ValoTheme.LABEL_COLORED);
            label.setWidth(null); // Set width as undefined

            wrapperLayout.addComponent(label);
            wrapperLayout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
            wrapperLayout.addStyleName("solid-border-grey");
            gridLayout.addComponent(wrapperLayout);


        }

        wrapperLayout = new VerticalLayout();
        wrapperLayout.setSizeFull();
        wrapperLayout.setSpacing(false);
        wrapperLayout.setMargin(false);
        Label label = new Label("Time and Location hghzg hujhuhuh ");
        label.setWidth("90%");
        wrapperLayout.addComponent(label);
        wrapperLayout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
        gridLayout.addComponent(wrapperLayout);



        gridLayout.addComponent(createParticipantsLayout());

        wrapperLayout = new VerticalLayout();
        wrapperLayout.setSizeFull();
        wrapperLayout.setSpacing(false);
        wrapperLayout.setMargin(false);
        wrapperLayout.addComponent(new Label(VaadinIcons.REFRESH.getHtml(), ContentMode.HTML));
        gridLayout.addComponent(wrapperLayout);

        wrapperLayout = new VerticalLayout();
        wrapperLayout.setSizeFull();
        wrapperLayout.setSpacing(false);
        wrapperLayout.setMargin(false);
        wrapperLayout.addComponent(new OnOffSwitch(false));
        gridLayout.addComponent(wrapperLayout);

        wrapperLayout = new VerticalLayout();
        wrapperLayout.setSizeFull();
        wrapperLayout.setSpacing(false);
        wrapperLayout.setMargin(false);
        wrapperLayout.addComponent(new OnOffSwitch(false));
        gridLayout.addComponent(wrapperLayout);

        wrapperLayout = new VerticalLayout();
        wrapperLayout.setSizeFull();
        wrapperLayout.setSpacing(false);
        wrapperLayout.setMargin(false);
        wrapperLayout.addComponent(new OnOffSwitch(false));
        gridLayout.addComponent(wrapperLayout);


        return gridLayout;

    }

    private CssLayout createParticipantsLayout() {
        CssLayout cssLayout = new CssLayout();
        cssLayout.setSizeUndefined();
        cssLayout.setWidth("100%");

        for (int i=0; i<15;i++) {
            VerticalLayout wrapperLayout = new VerticalLayout();
            wrapperLayout.setHeight(50, Unit.PIXELS);
            wrapperLayout.setWidth("15%");
            wrapperLayout.setSpacing(false);
            wrapperLayout.setMargin(false);
            wrapperLayout.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
                @Override
                public void layoutClick(LayoutEvents.LayoutClickEvent layoutClickEvent) {
                    Notification.show("AHA");
                }
            });
            wrapperLayout.addStyleName("nav-top-active");
            Label l = new Label("Christian <br> Arnold", ContentMode.HTML);
            wrapperLayout.addComponent(l);
            wrapperLayout.setComponentAlignment(l, Alignment.MIDDLE_CENTER);
            cssLayout.addComponent(wrapperLayout);
        }
        return cssLayout;
    }

    private CssLayout createWindowHeader() {
        CssLayout headerLayout = new CssLayout();
        Label headerLabel = new Label("Find participants");
        headerLayout.setSizeUndefined();
        headerLayout.addComponent(headerLabel);
        headerLayout.addStyleName("nav-top-active");
        return  headerLayout;
    }
}
