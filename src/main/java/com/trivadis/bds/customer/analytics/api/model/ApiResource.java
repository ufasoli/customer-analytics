package com.trivadis.bds.customer.analytics.api.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Ulises Fasoli
 * Date: 27.01.14
 * Time: 15:16
 */
public class ApiResource implements Serializable {

    private String title;
    private String url;
    private String description;
    private List<String> urlParams;

    public ApiResource(){}

    public ApiResource(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public ApiResource(String title, String url, String description) {
        this.title = title;
        this.url = url;
        this.description = description;
    }

    public ApiResource(String title, String url, String description, List<String> urlParams) {
        this.title = title;
        this.url = url;
        this.description = description;
        this.urlParams = urlParams;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getUrlParams() {
        return urlParams;
    }

    public void setUrlParams(List<String> urlParams) {
        this.urlParams = urlParams;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ApiResource that = (ApiResource) o;

        if (!url.equals(that.url)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return url.hashCode();
    }
}
