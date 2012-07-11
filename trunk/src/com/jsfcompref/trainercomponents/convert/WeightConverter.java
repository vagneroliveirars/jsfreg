package com.jsfcompref.trainercomponents.convert;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 * This converter looks up the value of the weightUnits preference in the
 * preferences object and uses it to convert the weight from the domain model
 * unit, which is kilograms, to the user's preference, which may be kilograms or
 * pounds
 * 
 * @author vagner
 * 
 */
@FacesConverter(value = "com.jsfcompref.Weight")
public class WeightConverter implements Converter {
	
	public static final int UNIT_KILOGRAMS = 0;
	public static final int UNIT_POUNDS = 1;
	
	private Locale locale = null;
	
	private NumberFormat formatter = null;
	
	public WeightConverter() {
		formatter = null;
		locale = null;
	}
	
	private int getUnitFromUserPreferences(FacesContext context) {
		if (null == context) {
			return -1;
		}
		
		Integer result = null;
		int unit = -1;
		Application application = context.getApplication();
		
		if (null == application) {
			return unit;
		}
		
		// look up the user's preference
		ValueExpression ve = application.getExpressionFactory().
				createValueExpression(context.getELContext(), 
						"#{userBean.preferences.weightUnits}", Integer.class);
		
		try {
			result = (Integer) ve.getValue(context.getELContext());
		} catch (Throwable e) {
		
		}
		
		if (null == result) {
			result = new Integer(UNIT_KILOGRAMS);
		}
		
		if (null != result) {
			unit = result.intValue();
		}
		
		return unit;
	}
	
	private NumberFormat getNumberFormat(FacesContext context) {
		if (null == formatter) {
			formatter = NumberFormat.getNumberInstance(getLocale(context));
		}
		return formatter;
	}

	private Locale getLocale(FacesContext context) {
		Locale locale = this.locale;
		if (locale == null) {
			UIViewRoot root = null;
			if (null != (root = context.getViewRoot())) {
				locale = root.getLocale();
			} else {
				locale = Locale.getDefault();
			}
		}
		return (locale);
	}
	
	public Locale getLocale() {
		if (this.locale == null) {
			this.locale = getLocale(FacesContext.getCurrentInstance());
		}
		return (this.locale);
	}
	
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if (context == null || component == null) {
			throw new NullPointerException();
		}
		
		/*
		 * this example doesn't use the component parameter but we will
		 * use it later in the chapter when we explore conversion
		 * messages
		 */
		if (null == value || 0 == value.length()) {
			return null;
		}
		
		int units = getUnitFromUserPreferences(context);
		float floatValue;
		
		try {
			floatValue = getNumberFormat(context).parse(value).floatValue();
		} catch (ParseException e) {
			throw new ConverterException(e.getMessage());
		}
		
		/*
		 * if the user's preference is English, this String is in
		 * pounds. Get the float value of the pounds
		 */
		if (UNIT_POUNDS == units) {
			floatValue /= 2.2; // convert to kilograms
		}
		
		return new Float(floatValue);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (context == null || component == null) {
			throw new NullPointerException();
		}
		
		/*
		 * this example doesn't use the component parameter but we will
		 * use it later in the chapter when we explore conversion
		 * messages
		 */
		if (null == value || 0 == value.toString().length()) {
			return null;
		}
		
		String result = null;
		
		float floatValue = ((Float)value).floatValue();
		int units = getUnitFromUserPreferences(context);
		
		if (UNIT_POUNDS == units) {
			floatValue *= 2.2; // convert to pounds 
		}
		
		result = getNumberFormat(context).format(new Float(floatValue));
		
		if (UNIT_POUNDS == units) {
			result = result + " lbs.";
		} else {
			result = result + " kg.";
		}
		
		return result;
	}

}
