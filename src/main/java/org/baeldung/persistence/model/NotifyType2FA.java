package org.baeldung.persistence.model;

public enum NotifyType {

    PHONE_SMS("phone sms"),
    MAIL_EMAIL("mail email"),
    GOOGLE_EMAIL("google email"),
    SKYPE("skype"),
    TELEGRAM("telegram"),
    VIBER("viber"),
    UNKNOWN("");

    private String type;

    NotifyType(String type) {
        this.type = type;
    }

    public String type() {
        return type;
    }

}
