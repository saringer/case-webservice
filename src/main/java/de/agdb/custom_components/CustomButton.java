package de.agdb.custom_components;

import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class CustomButton extends VerticalLayout {

    public CustomButton(String buttonCaption, LayoutEvents.LayoutClickListener listener) {
        setSizeFull();
        setSpacing(false);
        setMargin(false);
        Label caption = new Label(buttonCaption, ContentMode.HTML);
        caption.setSizeUndefined();
        addComponent(caption);
        setComponentAlignment(caption, Alignment.MIDDLE_CENTER);
        addLayoutClickListener(listener);

    }
    public CustomButton(String buttonCaption) {
        setSizeFull();
        setSpacing(false);
        setMargin(false);
        Label caption = new Label(buttonCaption, ContentMode.HTML);
        caption.setSizeUndefined();
        addComponent(caption);
        setComponentAlignment(caption, Alignment.MIDDLE_CENTER);

    }

}
