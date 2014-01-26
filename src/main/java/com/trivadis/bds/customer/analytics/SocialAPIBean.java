package com.trivadis.bds.customer.analytics;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.XingApi;
import org.scribe.model.*;
import org.scribe.oauth.OAuthService;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Ulises Fasoli
 * Date: 24.01.14
 * Time: 12:38
 */
@ManagedBean
@SessionScoped
public class SocialAPIBean implements Serializable {

    final String API_KEY = "220e0f61f21949635705";
    final String API_SECRET = "73612286d1390a5d0a28b3f775feb1897ad66e99";
    final String API_BASE_URL = "https://api.xing.com";


    private String apiResponse;
    private String acceptToken;
    String authorisationUrl;
    OAuthService service;
    Token requestToken;

    public void validateAccess() {

        // Initializing OAuth - Service
        service = new ServiceBuilder()
                .provider(XingApi.class)
                .apiKey(API_KEY)
                .apiSecret(API_SECRET)
                .build();


        requestToken = service.getRequestToken();
        authorisationUrl = service.getAuthorizationUrl(requestToken);
    }

    public void testXing() {


        Verifier verifier = new Verifier(acceptToken);
        Token accessToken = service.getAccessToken(requestToken, verifier);

        OAuthRequest request = new OAuthRequest(Verb.GET, API_BASE_URL + "/v1/users/me");
        service.signRequest(accessToken, request);
        Response response = request.send();


        apiResponse = response.getBody();

    }

    public String getApiResponse() {
        return apiResponse;
    }

    public void setApiResponse(String apiResponse) {
        this.apiResponse = apiResponse;
    }

    public String getAcceptToken() {
        return acceptToken;
    }

    public void setAcceptToken(String acceptToken) {
        this.acceptToken = acceptToken;
    }

    public String getAuthorisationUrl() {
        return authorisationUrl;
    }

    public void setAuthorisationUrl(String authorisationUrl) {
        this.authorisationUrl = authorisationUrl;
    }
}
