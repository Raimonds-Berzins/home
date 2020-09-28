package lv.maska.dbconverter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import lv.maska.domain.Email;

@Converter(autoApply = true)
public class EmailConverter implements AttributeConverter<Email, String> {

    @Override
    public String convertToDatabaseColumn(Email attribute) {
        return attribute.getEmailAddress();
    }

    @Override
    public Email convertToEntityAttribute(String dbData) {
        Email email = null;
        try {
            email = new Email(dbData);
        } catch (Exception e) {
            //TODO: handle exception
        }
        return email;
    }
    
}