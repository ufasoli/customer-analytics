package com.trivadis.bds.customer.analytics;

import com.trivadis.bds.customer.analytics.util.json.JsonUtils;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.*;
import org.scribe.oauth.OAuthService;
import org.scribe.utils.Preconditions;

import java.io.IOException;

/**
 * User: ufasoli
 * Date: 26/01/2014
 * Time: 13:05
 * Project : customer-analytics
 */
public class SocialMediaWrapper {

    private Token accessToken = null;
    private Token requestToken = null;
    private String authorizationUrl;
    private OAuthService oAuthService;

    private String validationPin;

    public SocialMediaWrapper(OAuthService oAuthService) {
        Preconditions.checkNotNull(oAuthService, "oAuthService cannot be null");
        this.oAuthService = oAuthService;
        init();
    }


    public void init() {
        Preconditions.checkNotNull(oAuthService, "cannot perform reset if oAuthService is null");

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


    public String performQuery(Verb verb, String url) {
        OAuthRequest request = new OAuthRequest(Verb.GET, url);
        getoAuthService().signRequest(accessToken, request);
        Response response = request.send();
         String jsonResponse = response.getBody();
       try{

           jsonResponse = JsonUtils.prettifyJSON(response.getBody());
       } catch (IOException e) {
           System.out.println(String.format("An error occurred while formatting the JSON: [%s]", e.getLocalizedMessage()));
       }

        return jsonResponse;

    }
}
