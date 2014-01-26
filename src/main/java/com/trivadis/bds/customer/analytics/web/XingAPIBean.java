package com.trivadis.bds.customer.analytics.web;

import com.trivadis.bds.customer.analytics.ApiManager;
import com.trivadis.bds.customer.analytics.SocialMediaWrapper;
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

/**
 * Created with IntelliJ IDEA.
 * User: Ulises Fasoli
 * Date: 24.01.14
 * Time: 12:38
 */
@ManagedBean
@ViewScoped
public class XingAPIBean implements Serializable {

    final String API_KEY = "220e0f61f21949635705";
    final String API_SECRET = "73612286d1390a5d0a28b3f775feb1897ad66e99";
    final String API_BASE_URL = "https://api.xing.com";


    @ManagedProperty(value = "#{apiManager}")
    private ApiManager apiManager;

    private String apiResponse;

    private String currentUser;


    private SocialMediaWrapper socialMediaWrapper;


    @PostConstruct
    public void init() {


        socialMediaWrapper = apiManager.getSocialMediaWrapper(XingApi.class);

        if (socialMediaWrapper == null) {


            // Initializing OAuth - Service
            OAuthService service = new ServiceBuilder()
                    .provider(XingApi.class)
                    .apiKey(API_KEY)
                    .apiSecret(API_SECRET)
                    .build();


            socialMediaWrapper = new SocialMediaWrapper(service);

        } else if (!socialMediaWrapper.initialized()) {

            socialMediaWrapper.init();
        }


    }

    public void validateAccess() {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {

            socialMediaWrapper.generateAccessToken();

            String msg = String.format("Successfully authentified with PIN : [%s]", socialMediaWrapper.getValidationPin());
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


    public void runQuery() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {

            Token accessToken = socialMediaWrapper.getAccessToken();

            if (!socialMediaWrapper.initialized()) {
                String msg = "Cannot Perform Query since you are not authentified or your authentication token has expired. Please sign in again into the API";
                facesContext.addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
                socialMediaWrapper.reset();
                return;
            }

            String url =API_BASE_URL + "/v1/users/me";
            apiResponse = socialMediaWrapper.performQuery(Verb.GET, url);

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
}
