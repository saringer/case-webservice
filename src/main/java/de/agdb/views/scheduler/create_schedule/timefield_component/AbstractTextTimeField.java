package de.agdb.views.scheduler.create_schedule.timefield_component;

import com.vaadin.ui.Component;
import com.vaadin.v7.ui.TextField;


public abstract class AbstractTextTimeField<T> extends AbstractTimeField<T> {

	private static final long serialVersionUID = -7767159784950039512L;

	protected TextField field;

	public AbstractTextTimeField() {
		field = new TextField();
		field.setWidth("100%");
		//field.setsetNullRepresentation("");
	}

	@Override
	protected Component initContent() {
		return field;
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		field.setReadOnly(readOnly);
	}
}
