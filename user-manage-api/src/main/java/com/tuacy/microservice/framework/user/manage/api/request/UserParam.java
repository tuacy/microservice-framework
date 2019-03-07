package com.tuacy.microservice.framework.user.manage.api.request;

public class UserParam {

    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "UserParam{" +
                "userName='" + userName + '\'' +
                '}';
    }
}
