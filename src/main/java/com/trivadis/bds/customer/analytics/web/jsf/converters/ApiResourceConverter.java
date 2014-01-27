package com.trivadis.bds.customer.analytics.web.jsf.converters;

import com.trivadis.bds.customer.analytics.api.ApiResource;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.lang.annotation.Annotation;

/**
 * Created with IntelliJ IDEA.
 * User: Ulises Fasoli
 * Date: 27.01.14
 * Time: 17:33
 */
//@FacesConverter(value = "apiResouceConverter", forClass = ApiResource.class)
public class ApiResourceConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return ((ApiResource)value).getUrl();
    }
}
