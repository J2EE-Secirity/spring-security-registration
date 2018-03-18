package org.baeldung.web.dto;

public enum  TypeNotify {

    PHONE_SMS("phone sms"),
    MAIL_EMAIL("mail email"),
    GOOGLE_EMAIL("google email"),
    SKYPE("skype"),
    TELEGRAM("telegram"),
    VIBER("viber"),
    UNKNOWN("");

    private String type;

    TypeNotify(String type) {
        this.type = type;
    }

    public String type() {
        return type;
    }

}
