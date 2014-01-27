package com.trivadis.bds.customer.analytics.util.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * User: ufasoli
 * Date: 26/01/2014
 * Time: 18:45
 * Project : customer-analytics
 */
public class JsonUtils {

    private static ObjectMapper mapper;


    public static String prettifyJSON(String unformattedJson) throws IOException {


        JsonNode rootNode = (JsonNode) getMapper().readValue(unformattedJson, JsonNode.class);

        return getMapper().writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
    }


    public static JsonNode stringToJson(String jsonString){

        try {
            return getMapper().readValue(jsonString, JsonNode.class);
        } catch (IOException e) {
            return null;
        }
    }

    private static ObjectMapper getMapper(){

        if(mapper == null){
            mapper = new ObjectMapper();

        }

        return mapper;
    }
}
