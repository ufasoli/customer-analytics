package com.trivadis.bds.customer.analytics.api;

import com.trivadis.bds.customer.analytics.api.model.ApiResource;
import org.scribe.builder.api.Api;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ufasoli
 * Date: 26/01/2014
 * Time: 13:05
 * Project : customer-analytic
 */
public class XingWrapper extends SocialMediaWrapper {

    static final String API_KEY =  "220e0f61f21949635705";
    static final String API_SECRET = "73612286d1390a5d0a28b3f775feb1897ad66e99";
    static final String BASE_URL = "https://api.xing.com";




    public XingWrapper(Class<? extends Api> apiClass) {
        super(apiClass, API_KEY, API_SECRET, BASE_URL);




    }


    @Override
    public List<ApiResource> getApiResources() {
        return apiResources;
    }

    @Override
    public String findUserByIdUrl(){
       return baseUrl + "/v1/users/$user_id$";
    }

    @Override
    public String findUserByEmailUrl() {
        return baseUrl +"/v1/users/find_by_emails";
    }

    public static List<ApiResource> apiResources = new ArrayList<>();

    static{
        // User resources
        apiResources.add(new ApiResource(
                "Current user infos",
                BASE_URL + "/v1/users/$user_id$",
                "Shows a particular user’s profile. The data returned by this call will be checked against and filtered on the basis of the privacy settings of the requested user"));

        apiResources.add(
                new ApiResource(
                        "Current User API user(me) infos",
                        BASE_URL + "/v1/users/me",
                        "Shows a particular user’s profile. The data returned by this call will be checked against and filtered on the basis of the privacy settings of the requested user"));


        apiResources.add(
                new ApiResource(
                        "Current User API user id_card",
                        BASE_URL + "/v1/users/me/id_card",
                        "Shows minimal profile information of the user that authorized the consumer. If you need more user details please also have a look at the get user details and the get app user's details call."
                ));


        apiResources.add(
                new ApiResource(
                        "Current User legal information",
                        BASE_URL + 	"/v1/users/$user_id$/legal_information",
                        "Fetch legal information of a user"
                ));


        apiResources.add(
                new ApiResource(
                        "Current User's contacts ids",
                        BASE_URL + 	"/v1/users/$user_id$/contacts",
                        "Returns the requested user's contacts. The nested user data this call returns are the same as the get user details call. You can't request more than 100 contacts at once (see limit parameter), but you can perform several requests in parallel. If you execute this call with limit=0, it will tell you how many contacts the user has without returning any user data"
                ));


        apiResources.add(
                new ApiResource(
                        "Current API User's contacts ids",
                        BASE_URL + 	"/v1/users/me/contact_ids",
                        "Returns all contact IDs of the current user"
                ));

        apiResources.add(
                new ApiResource(
                        "Shared contacts (between api and current user)",
                        BASE_URL + 	"/v1/users/$user_id$/contacts/shared",
                        "Returns the list of contacts who are direct contacts of both the given and the current user. The nested user data this call returns are the same as the get user details call. You can't request more than 100 shared contacts at once (see limit parameter), but you can perform several requests in parallel. If you execute this call with limit=0, it will tell you how many contacts the user has without returning any user data"
                ));

        apiResources.add(
                new ApiResource(
                        "Current user contact requests",
                        BASE_URL + 	"/v1/users/$user_id$/contact_requests",
                        "Lists all pending incoming contact requests the specified user has received from other users."
                ));

        apiResources.add(
                new ApiResource(
                        "Current user contact requests sent",
                        BASE_URL + 	"/v1/users/$user_id$/contact_requests/sent",
                        "Lists all pending contact requests the specified user has sent"
                ));

        apiResources.add(
                new ApiResource(
                        "Current user bookmarks",
                        BASE_URL + 	"/v1/users/$user_id$/bookmarks",
                        "Returns a list of bookmarked users for the given user_id. This list is sorted by the creation date of the bookmarks."
                ));

        apiResources.add(
                new ApiResource(
                        "Current user network feed",
                        BASE_URL + 	"/v1/users/$user_id$/network_feed",
                        "Returns the stream of activities recently performed by the user's network"
                ));

        apiResources.add(
                new ApiResource(
                        "Current user profile visits",
                        BASE_URL + 	"/v1/users/$user_id$/visits",
                        "Returns a list of users who recently visited the specified user's profile. Entries with a value of null in the user_id attribute indicate anonymous (non-XING) users (e.g. resulting from Google searches)"
                ));

        apiResources.add(
                new ApiResource(
                        "Current user recommendations",
                        BASE_URL + 	"/v1/users/$user_id$/network/recommendations",
                        "Returns a list of users the specified user might know."
                ));

        apiResources.add(
                new ApiResource(
                        "GeoLocation",
                        BASE_URL + 	"/v1/users/$user_id$/nearby_users",
                        "Get the users that are near your current location"
                ));


    }


}
