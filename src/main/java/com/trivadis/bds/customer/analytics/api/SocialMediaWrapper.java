package com.trivadis.bds.customer.analytics.api;

import com.trivadis.bds.customer.analytics.api.exceptions.ApiResponseException;
import com.trivadis.bds.customer.analytics.api.model.ApiResource;
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
 *
 * Wrapper utility class to encapsulate the different steps of API communication and initialization
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


    /**
     * Initializes the Wrapper by :
     * <ul>
     *     <li>Initializing the ApiService corresponding to the selected provider</li>
     *     <li>Generates the request token</li>
     *     <li>Generates the authorization url</li>
     * </ul>
     */
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

    /**
     * Reinitializes the wrapper by basically recreating the request token, authorization url and oAuthService
     * @see SocialMediaWrapper#init()
     */
    public void reset() {
        Preconditions.checkNotNull(oAuthService, "cannot perform reset if oAuthService is null");
        init();
    }

    /**
     * Generates the access token required to perform queries on the underlying API
     * @throws OAuthException
     */
    public void generateAccessToken() throws OAuthException {

        Preconditions.checkNotNull(oAuthService, "cannot perform validation if validationPin is null");
        Verifier verifier = new Verifier(validationPin);
        accessToken = oAuthService.getAccessToken(requestToken, verifier);
        requestToken = null;


    }

    /**
     *
     * @return true if ( accessToken, oAuthService) != null && request token == null else false
     */
    public Boolean initialized() {

        return (accessToken != null && oAuthService != null) && requestToken == null;
    }


    /**
     * Runs the HTTP query on the url using the provided HTTP verb
     * @param verb HTTP verb to perform the query (GET, POST, etc.)
     * @param url the url to where run the query
     * @return the API response body
     * @throws ApiResponseException  if the API status code != 200
     */
    public String performQuery(Verb verb, String url) throws ApiResponseException {

        OAuthRequest request = new OAuthRequest(verb, url);
        getoAuthService().signRequest(accessToken, request);
        Response response = request.send();

        if(response.getCode() != 200){
            throw new ApiResponseException(
                    String.format("Error while performing API query. HTTP status [%s] on url [%s]. Error [%s]",
                            response.getCode(),
                            url,
                            response.getMessage()));
        }
        String jsonResponse = response.getBody();
        try {

            jsonResponse = JsonUtils.prettifyJSON(response.getBody());
        } catch (IOException e) {
            System.out.println(String.format("An error occurred while formatting the JSON: [%s]", e.getLocalizedMessage()));
        }

        return jsonResponse;

    }


    /**
     * Returns the list of API resources available for a given wrapper
     *
     * @return the list of resources (Urls) that can be consumed on a given Web servive
     */
    public abstract List<ApiResource> getApiResources();

    /**
     *
     * @return The url that can be used by the client to find a user by it's ID
     */
    public abstract String findUserByIdUrl();

    /**
     *
     * @return The url that can be used by the client to find a user by it's email
     */
    public abstract String findUserByEmailUrl();


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
