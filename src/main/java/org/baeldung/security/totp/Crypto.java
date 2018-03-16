package org.baeldung.security.totp;

public enum Crypto {

    HMAC_SHA1("HmacSHA1"), HMAC_SHA256("HmacSHA256"), HMAC_SHA512("HmacSHA512");

    private String value;

    Crypto(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
