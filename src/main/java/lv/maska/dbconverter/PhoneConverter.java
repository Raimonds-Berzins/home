package lv.maska.dbconverter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import lv.maska.domain.Phone;

@Converter(autoApply = true)
public class PhoneConverter implements AttributeConverter<Phone, String> {

    @Override
    public String convertToDatabaseColumn(Phone attribute) {
        return attribute.getPhone();
    }

    @Override
    public Phone convertToEntityAttribute(String dbData) {
        Phone phone = null;
        try {
            phone = new Phone(dbData);
        } catch (Exception e) {
            //TODO: handle exception
        }
        return phone;
    }
    
}