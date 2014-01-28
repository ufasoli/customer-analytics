package com.trivadis.bds.customer.analytics.web.beans.xing;

import com.trivadis.bds.customer.analytics.api.SocialMediaWrapper;
import com.trivadis.bds.customer.analytics.api.XingWrapper;
import com.trivadis.bds.customer.analytics.api.model.ApiSelectedUser;
import com.trivadis.bds.customer.analytics.util.json.JsonUtils;
import com.trivadis.bds.customer.analytics.util.string.StringUtils;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

    private String currentUserQuery;

    private ApiSelectedUser apiSelectedUser = new ApiSelectedUser();

    private List<String> searchMethods = Arrays.asList("BY_ID", "BY_EMAIL");
    private String userSearchMethod = "BY_ID";

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


    public void resetWrapper(){
        socialMediaWrapper.reset();

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "A new validation url was generated ", "A new validation url was generated"));
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

            if (currentUserQuery == null || currentUserQuery.isEmpty()) {
                String msg = "Cannot Perform Query since current user is null";
                facesContext.addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));

                return;
            }


            if (userSearchMethod.equals("BY_ID")) {

                Map<String, Object> values = new HashMap<>();
                values.put("user_id", currentUserQuery);


                apiResponse = socialMediaWrapper.performQuery(Verb.GET,
                        StringUtils.formatString(socialMediaWrapper.findUserByIdUrl(), values));


                if (apiResponse != null && !apiResponse.isEmpty()) {

                    // override "me" with the user's real id
                    apiSelectedUser.setUserId(JsonUtils.stringToJson(apiResponse).findValue("users").findValue("id").asText());
                    apiSelectedUser.setName(JsonUtils.stringToJson(apiResponse).findValue("users").findValue("first_name").asText());
                    apiSelectedUser.setLastname(JsonUtils.stringToJson(apiResponse).findValue("users").findValue("last_name").asText());
                    apiSelectedUser.setEmail(JsonUtils.stringToJson(apiResponse).findValue("active_email").asText());

                    String msg = "User found";
                    facesContext.addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg));
                }

            } else if (userSearchMethod.equals("BY_EMAIL")) {

                apiResponse = socialMediaWrapper.performQuery(
                        Verb.GET,
                        socialMediaWrapper.findUserByEmailUrl() + "?emails=" + currentUserQuery
                );


                if (apiResponse != null && !apiResponse.isEmpty()) {

                    apiSelectedUser.setUserId(JsonUtils.stringToJson(apiResponse).findValue("results").findValue("items").findValue("user").findValue("id").asText());
                    apiSelectedUser.setName(null);
                    apiSelectedUser.setLastname(null);
                    apiSelectedUser.setEmail(currentUserQuery);

                    String msg = String.format("User found using email [%s]", currentUserQuery);
                    facesContext.addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg));
                } else {
                    String msg = String.format("Unable to find user with email [%s]", currentUserQuery);
                    facesContext.addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
                }
            }
        } catch (Exception e) {
            String msg = String.format(
                    "An error occurred while performing the query. error : [%s] ", e.getMessage());
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
        }

    }


    public void runQuery() {


        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {


            if (!socialMediaWrapper.initialized()) {
                String msg = "Cannot Perform Query since you are not authenticated or your authentication token has expired. Please sign in again into the API";
                facesContext.addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
                socialMediaWrapper.reset();
                return;
            }

            if (currentUserQuery == null || currentUserQuery.isEmpty()) {
                String msg = "Cannot Perform Query since current user is null";
                facesContext.addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));

                return;
            }


            if (currentResource == null || currentResource.isEmpty()) {
                String msg = "Cannot Perform Query no resource has been selected";
                facesContext.addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));

                return;
            }

            Map<String, Object> values = new HashMap<>();
            values.put("user_id", apiSelectedUser.getUserId());

            apiResponse = socialMediaWrapper.performQuery(Verb.GET,
                    StringUtils.formatString(currentResource, values));


            if (apiResponse != null && !apiResponse.isEmpty() && currentResource.equals(socialMediaWrapper.findUserByIdUrl())) {


                apiSelectedUser.setName(JsonUtils.stringToJson(apiResponse).findValue("users").findValue("first_name").asText());
                apiSelectedUser.setLastname(JsonUtils.stringToJson(apiResponse).findValue("users").findValue("last_name").asText());
                apiSelectedUser.setEmail(JsonUtils.stringToJson(apiResponse).findValue("active_email").asText());


            }


        } catch (Exception e) {
            String msg = String.format(
                    "An error occurred while performing the query. error : [%s] ", e.getMessage());

            apiResponse = null;
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

    public String getCurrentUserQuery() {
        return currentUserQuery;
    }

    public void setCurrentUserQuery(String currentUserQuery) {
        this.currentUserQuery = currentUserQuery;
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

    public ApiSelectedUser getApiSelectedUser() {
        return apiSelectedUser;
    }

    public void setApiSelectedUser(ApiSelectedUser apiSelectedUser) {
        this.apiSelectedUser = apiSelectedUser;
    }

    public String getUserSearchMethod() {
        return userSearchMethod;
    }

    public void setUserSearchMethod(String userSearchMethod) {
        this.userSearchMethod = userSearchMethod;
    }

    public List<String> getSearchMethods() {
        return searchMethods;
    }

    public void setSearchMethods(List<String> searchMethods) {
        this.searchMethods = searchMethods;
    }
}
