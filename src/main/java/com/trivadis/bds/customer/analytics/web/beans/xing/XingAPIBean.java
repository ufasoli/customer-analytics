package com.trivadis.bds.customer.analytics.web.beans.xing;

import com.trivadis.bds.customer.analytics.api.XingWrapper;
import com.trivadis.bds.customer.analytics.web.beans.sessions.ApiManager;
import com.trivadis.bds.customer.analytics.api.SocialMediaWrapper;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.XingApi;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Ulises Fasoli
 * Date: 24.01.14
 * Time: 12:38
 */
@ManagedBean
@ViewScoped
public class XingAPIBean implements Serializable {




    @ManagedProperty(value = "#{apiManager}")
    private ApiManager apiManager;

    private String apiResponse;

    private String currentUser;


    private SocialMediaWrapper socialMediaWrapper;


    @PostConstruct
    public void init() {


        socialMediaWrapper = apiManager.getSocialMediaWrapper(XingApi.class);

        if (socialMediaWrapper == null) {
            socialMediaWrapper = new XingWrapper(XingApi.class);

        } else if (!socialMediaWrapper.initialized()) {

            socialMediaWrapper.init();
        }


    }

    public void validateAccess() {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {

            socialMediaWrapper.generateAccessToken();

            String msg = String.format("Successfully authenticated with PIN : [%s]", socialMediaWrapper.getValidationPin());
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg));

        } catch (Exception e) {

            String msg = String.format("An error occurred while authentication. " +
                    "A new Url has been generated, please retry. Error : [%s]", e.getLocalizedMessage());
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));

            socialMediaWrapper.reset();
        }

        socialMediaWrapper.setValidationPin(null);
        apiManager.updateSocialMediaWrapper(XingApi.class, socialMediaWrapper);


    }


    public void runQuery(String query) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {



            if (!socialMediaWrapper.initialized()) {
                String msg = "Cannot Perform Query since you are not authenticated or your authentication token has expired. Please sign in again into the API";
                facesContext.addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
                socialMediaWrapper.reset();
                return;
            }

            apiResponse = socialMediaWrapper.performQuery(Verb.GET, query);

        } catch (Exception e) {
            String msg = String.format(
                    "An error occurred while performing the query. error : [%s] ", e.getLocalizedMessage());
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
        }

    }


    public String getApiResponse() {
        return apiResponse;
    }

    public void setApiResponse(String apiResponse) {
        this.apiResponse = apiResponse;
    }




    public ApiManager getApiManager() {
        return apiManager;
    }

    public void setApiManager(ApiManager apiManager) {
        this.apiManager = apiManager;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public SocialMediaWrapper getSocialMediaWrapper() {
        return socialMediaWrapper;
    }

    public void setSocialMediaWrapper(SocialMediaWrapper socialMediaWrapper) {
        this.socialMediaWrapper = socialMediaWrapper;
    }


    private Map<String,String> apiResources = new HashMap<>();

    public Map<String, String> getApiResources() {
        if(apiResources.isEmpty()){
                                 apiResources.put("Test", "http://test");
            apiResources.put("Test2", "http://test2");
        }
        return apiResources;
    }

    public void setApiResources(Map<String, String> apiResources) {
        this.apiResources = apiResources;
    }
}
