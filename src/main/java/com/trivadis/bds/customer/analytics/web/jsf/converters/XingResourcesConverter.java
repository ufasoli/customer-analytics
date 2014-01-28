package com.trivadis.bds.customer.analytics.web.jsf.converters;

import com.trivadis.bds.customer.analytics.api.model.ApiResource;
import com.trivadis.bds.customer.analytics.api.XingWrapper;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Ulises Fasoli
 * Date: 27.01.14
 * Time: 17:33
 */
@FacesConverter(value = "xingResourcesConverter")
public class XingResourcesConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {

        if(value == null){
            return null;
        }

        List<ApiResource> xingResources = XingWrapper.apiResources;

        for(ApiResource apiResource : xingResources){
            if(apiResource.getUrl().equals(value)){
                return apiResource;
            }

        }

        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {

        if(value != null && value instanceof ApiResource){
            return ((ApiResource)value).getUrl();
        }
        else{
            return null;
        }

    }
}
