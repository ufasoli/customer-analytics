package com.trivadis.bds.customer.analytics.web.beans.sessions;

import com.trivadis.bds.customer.analytics.api.SocialMediaWrapper;
import org.scribe.builder.api.DefaultApi10a;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * User: ufasoli
 * Date: 26/01/2014
 * Time: 11:39
 * Project : customer-analytics
 */
@ManagedBean
@SessionScoped
public class ApiManager implements Serializable{

    private Map<Class<? extends DefaultApi10a>, SocialMediaWrapper> apis = new HashMap<>();


    public  <T extends DefaultApi10a> SocialMediaWrapper getSocialMediaWrapper(Class<T> provider)  {

        return apis.get(provider);
    }

    public  <T extends DefaultApi10a> void updateSocialMediaWrapper(Class<T> provider, SocialMediaWrapper socialMediaWrapper)  {

       apis.put(provider, socialMediaWrapper);
    }




}
