package com.trivadis.bds.customer.analytics.web.beans.xing;

import com.fasterxml.jackson.databind.JsonNode;
import com.trivadis.bds.customer.analytics.api.SocialMediaWrapper;
import com.trivadis.bds.customer.analytics.api.XingWrapper;
import com.trivadis.bds.customer.analytics.util.StringUtils;
import com.trivadis.bds.customer.analytics.util.json.JsonUtils;
import com.trivadis.bds.customer.analytics.web.beans.sessions.ApiManager;
import org.scribe.builder.api.XingApi;
import org.scribe.model.Verb;

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

    private String currentResource;


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


    public void findUser() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {



            if (!socialMediaWrapper.initialized()) {
                String msg = "Cannot Perform Query since you are not authenticated or your authentication token has expired. Please sign in again into the API";
                facesContext.addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
                socialMediaWrapper.reset();
                return;
            }

            if(currentUser == null || currentUser.isEmpty()){
                String msg = "Cannot Perform Query since current user is null";
                facesContext.addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));

                return;
            }

            String findUserUrl = socialMediaWrapper.findUserUrl();

            Map<String, Object> values = new HashMap<>();
            values.put("user_id", currentUser);

            apiResponse = socialMediaWrapper.performQuery(Verb.GET,
                    StringUtils.formatString(socialMediaWrapper.findUserUrl(), values));


            if(currentUser.equals("me") && apiResponse != null && !apiResponse.isEmpty() ){

                JsonNode node = JsonUtils.stringToJson(apiResponse);

                // override "me" with the user's real id
                currentUser = JsonUtils.stringToJson(apiResponse).findValue("users").findValue("id").asText();


            }



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

    public String getCurrentResource() {
        return currentResource;
    }

    public void setCurrentResource(String currentResource) {
        this.currentResource = currentResource;
    }
}
