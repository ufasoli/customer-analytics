package com.trivadis.bds.customer.analytics.api;

import com.trivadis.bds.customer.analytics.util.json.JsonUtils;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.Api;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.*;
import org.scribe.oauth.OAuthService;
import org.scribe.utils.Preconditions;

import java.io.IOException;
import java.util.List;

/**
 * User: ufasoli
 * Date: 26/01/2014
 * Time: 13:05
 * Project : customer-analytics
 */
public abstract class SocialMediaWrapper {


    /** API COMMUNICATION **/
    protected Token accessToken = null;
    protected Token requestToken = null;
    protected String authorizationUrl;
    protected OAuthService oAuthService;
    protected String validationPin;


    /** API SPECIFIC FIELDS **/
    protected Class<? extends Api> apiClass;
    protected String apiKey;
    protected String apiSecret;
    protected String baseUrl;



    public SocialMediaWrapper(Class<? extends Api> apiClass, String apiKey, String apiSecret, String baseUrl) {
        this.apiClass = apiClass;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.baseUrl = baseUrl;
        init();

    }



    public void init() {

        Preconditions.checkNotNull(apiKey, "apiKey cannot be null");
        Preconditions.checkNotNull(apiSecret, "apiSecret cannot be null");
        Preconditions.checkNotNull(apiClass, "apiClass cannot be null");
        // Initializing OAuth - Service
        oAuthService = new ServiceBuilder()
                .provider(apiClass)
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .build();

        Preconditions.checkNotNull(oAuthService, "cannot perform initialization if oAuthService is null");
        this.requestToken = oAuthService.getRequestToken();
        this.authorizationUrl = oAuthService.getAuthorizationUrl(requestToken);
    }

    public void reset() {
        Preconditions.checkNotNull(oAuthService, "cannot perform reset if oAuthService is null");
        init();
    }

    public void generateAccessToken() throws OAuthException {
        Preconditions.checkNotNull(oAuthService, "cannot perform validation if validationPin is null");
        Verifier verifier = new Verifier(validationPin);
        accessToken = oAuthService.getAccessToken(requestToken, verifier);
        requestToken = null;


    }


    public Boolean initialized() {

        return (accessToken != null && oAuthService != null) && requestToken == null;
    }


    public String performQuery(Verb verb, String url) {
        OAuthRequest request = new OAuthRequest(Verb.GET, url);
        getoAuthService().signRequest(accessToken, request);
        Response response = request.send();
        String jsonResponse = response.getBody();
        try {

            jsonResponse = JsonUtils.prettifyJSON(response.getBody());
        } catch (IOException e) {
            System.out.println(String.format("An error occurred while formatting the JSON: [%s]", e.getLocalizedMessage()));
        }

        return jsonResponse;

    }


    public abstract List<ApiResource> getApiResources();

    public abstract String findUserUrl();


    public Token getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(Token accessToken) {
        this.accessToken = accessToken;
    }

    public Token getRequestToken() {
        return requestToken;
    }

    public void setRequestToken(Token requestToken) {
        this.requestToken = requestToken;
    }

    public OAuthService getoAuthService() {
        return oAuthService;
    }

    public void setoAuthService(OAuthService oAuthService) {
        this.oAuthService = oAuthService;
    }

    public String getAuthorizationUrl() {
        return authorizationUrl;
    }

    public void setAuthorizationUrl(String authorizationUrl) {
        this.authorizationUrl = authorizationUrl;
    }


    public String getValidationPin() {
        return validationPin;
    }

    public void setValidationPin(String validationPin) {
        this.validationPin = validationPin;
    }

    public Class<? extends Api> getApiClass() {
        return apiClass;
    }

    public void setApiClass(Class<? extends Api> apiClass) {
        this.apiClass = apiClass;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
    }
}
