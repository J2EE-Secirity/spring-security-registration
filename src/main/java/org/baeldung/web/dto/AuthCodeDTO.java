package org.baeldung.web.dto;

import javax.validation.constraints.NotNull;

public class AuthCodeDTO {

    private static final long serialVersionUID = 1L;

    @NotNull
    private long code;

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "AuthCodeDTO{" +
                "code='" + code + '\'' +
                '}';
    }
}
