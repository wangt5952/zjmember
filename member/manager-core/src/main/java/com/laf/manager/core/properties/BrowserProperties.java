package com.laf.manager.core.properties;

import static com.laf.manager.core.properties.LoginType.JSON;

public class BrowserProperties {
    private String loginPage = "/login.html";

    private LoginType loginType = JSON;

    private int rememberMeSeconds = 7 * 24 * 60 * 60;

    public String getLoginPage() {
        return loginPage;
    }

    public void setLoginPage(String loginPage) {
        this.loginPage = loginPage;
    }

    public LoginType getLoginType() {
        return loginType;
    }

    public void setLoginType(LoginType loginType) {
        this.loginType = loginType;
    }

    public int getRememberMeSeconds() {
        return rememberMeSeconds;
    }

    public void setRememberMeSeconds(int rememberMeSeconds) {
        this.rememberMeSeconds = rememberMeSeconds;
    }
}
