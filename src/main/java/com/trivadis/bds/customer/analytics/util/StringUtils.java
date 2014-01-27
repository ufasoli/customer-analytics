package com.trivadis.bds.customer.analytics.util;

import org.antlr.stringtemplate.StringTemplate;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Ulises Fasoli
 * Date: 27.01.14
 * Time: 21:34
 */
public class StringUtils {

    public static String formatString(String stringTemplate, Map<String, Object> values){

        StringTemplate template = new StringTemplate(stringTemplate);

        template.setArgumentContext(values);

        return template.toString();
    }
}
