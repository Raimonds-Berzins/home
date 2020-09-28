package lv.maska.domain;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class Phone implements Address {
    String phoneNumber;
    public Phone(String phone) throws Exception {
        if (phone == null)
            throw new Exception("Wrong format");
        
        phoneNumber = phone;
    }

    public String getPhone() {
        return phoneNumber;
    }

    @Override
    public String getAddress() {
        return phoneNumber;
    }
}
