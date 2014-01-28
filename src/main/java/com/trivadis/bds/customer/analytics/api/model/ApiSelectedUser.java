package com.trivadis.bds.customer.analytics.api.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Ulises Fasoli
 * Date: 28.01.14
 * Time: 09:25
 */
public class ApiSelectedUser implements Serializable{

    private String userId;
    private String name;
    private String lastname;
    private String email;

    private List<String> contactsId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getContactsId() {
        return contactsId;
    }

    public void setContactsId(List<String> contactsId) {
        this.contactsId = contactsId;
    }
}
