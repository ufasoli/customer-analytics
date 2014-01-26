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

        if(mapper == null){
            mapper = new ObjectMapper();

        }
        JsonNode rootNode = (JsonNode) mapper.readValue(unformattedJson, JsonNode.class);
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode));
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
    }
}
