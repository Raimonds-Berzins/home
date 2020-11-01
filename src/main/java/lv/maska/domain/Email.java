package lv.maska.domain;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class Email implements Address {
    String domainName;
    String userName;
    String emailAddress;
    public Email(String val) throws Exception {
        String value = val.trim();
        if (value == null || !value.matches("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$"))
            throw new Exception("Incorect input format");

        emailAddress = value;
        domainName = value.substring(value.indexOf("@") + 1);
        userName = value.substring(0, value.indexOf("@"));
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    @Override
    public String getAddress() {
        return emailAddress;
    }
}