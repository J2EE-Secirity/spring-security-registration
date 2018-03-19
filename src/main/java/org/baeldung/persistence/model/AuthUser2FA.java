package org.baeldung.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class AuthUser2FA implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private String login;

    @NotNull
    private String password;

    @NotNull
    private NotifyType2FA notifyBy;

    @JsonIgnore
    private String secret;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public NotifyType2FA getNotifyBy() {
        return notifyBy;
    }

    public void setNotifyBy(NotifyType2FA notifyBy) {
        this.notifyBy = notifyBy;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Override
    public String toString() {
        return "AuthUser2FA{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", notifyBy=" + notifyBy +
                ", secret=" + secret +
                '}';
    }
}
