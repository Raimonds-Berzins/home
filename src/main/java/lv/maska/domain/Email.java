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
        val = val.strip();
        if (val == null || !val.matches("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$"))
            throw new Exception("Incorect input format");

        emailAddress = val;
        domainName = val.substring(val.indexOf("@") + 1);
        userName = val.substring(0, val.indexOf("@"));
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    @Override
    public String getAddress() {
        return emailAddress;
    }
}