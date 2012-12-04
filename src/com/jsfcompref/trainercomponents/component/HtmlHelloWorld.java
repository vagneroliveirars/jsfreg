package com.jsfcompref.trainercomponents.component;

import java.io.IOException;
import java.util.Date;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

@FacesComponent(value="HtmlHelloWorld")
public class HtmlHelloWorld extends UIComponentBase {

	@Override
	public String getFamily() {
		return null;
	}
	
	@Override
	public void encodeAll(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("div", this);
		writer.writeAttribute("style", "color: red", null);
		
		String message = (String) this.getAttributes().get("hellomsg");
		if (message == null) {
			writer.writeText("HelloWorld! Today is: " +
					new Date(), null);
		} else {
			writer.writeText(message, null);
		}
		writer.endElement("div");
	}

}
