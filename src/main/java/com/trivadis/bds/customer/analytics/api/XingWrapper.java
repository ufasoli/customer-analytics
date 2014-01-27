package com.trivadis.bds.customer.analytics.api;

import org.scribe.builder.api.Api;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Ulises Fasoli
 * Date: 27.01.14
 * Time: 14:12
 */
public class XingWrapper extends SocialMediaWrapper {

    static final String API_KEY =  "220e0f61f21949635705";
    static final String API_SECRET = "73612286d1390a5d0a28b3f775feb1897ad66e99";
    static final String BASE_URL = "https://api.xing.com";

    protected List<ApiResource> apiResources = new ArrayList<>();


    public XingWrapper(Class<? extends Api> apiClass) {
        super(apiClass, API_KEY, API_SECRET, BASE_URL);

        // User resources
        apiResources.add(new ApiResource(
                "User by Id",
                baseUrl + "/v1/users/:user_id",
                "Shows a particular user’s profile. The data returned by this call will be checked against and filtered on the basis of the privacy settings of the requested user"));

        apiResources.add(
                new ApiResource(
                        "Current User (me)",
                        baseUrl + "/v1/users/me",
                        "Shows a particular user’s profile. The data returned by this call will be checked against and filtered on the basis of the privacy settings of the requested user"));


        apiResources.add(
                new ApiResource(
                        "Current User (me) id_card",
                        baseUrl + "/v1/users/me/id_card",
                        "Shows minimal profile information of the user that authorized the consumer. If you need more user details please also have a look at the get user details and the get app user's details call."
                ));

    }

    @Override
    public List<ApiResource> getApiResources() {
        return apiResources;
    }

    @Override
    public String findUserUrl(){
       return baseUrl + "/v1/users/:user_id";
    }


}
