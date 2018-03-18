package org.baeldung.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class AuthUserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private String login;

    @NotNull
    private String password;

    @NotNull
    private TypeNotify notifyBy;

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

    public TypeNotify getNotifyBy() {
        return notifyBy;
    }

    public void setNotifyBy(TypeNotify notifyBy) {
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
        return "AuthUserDTO{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", notifyBy=" + notifyBy +
                ", secret=" + secret +
                '}';
    }
}
