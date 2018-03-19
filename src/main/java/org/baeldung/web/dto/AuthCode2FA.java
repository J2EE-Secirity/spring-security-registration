package org.baeldung.web.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class AuthCode2FA implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private long verficationCode;

    public long getVerficationCode() {
        return verficationCode;
    }

    public void setVerficationCode(long verficationCode) {
        this.verficationCode = verficationCode;
    }

    @Override
    public String toString() {
        return "AuthCode2FA{" +
                "verficationCode='" + verficationCode + '\'' +
                '}';
    }
}
