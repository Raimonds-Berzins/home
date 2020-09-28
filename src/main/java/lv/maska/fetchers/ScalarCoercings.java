package lv.maska.fetchers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import lv.maska.domain.Email;
import lv.maska.domain.Phone;

/**
 * ScalarCoercings
 */
public class ScalarCoercings {

    public static Coercing<LocalDateTime, String> DATE_COERCING = new Coercing<LocalDateTime, String>() {

        DateTimeFormatter formater = DateTimeFormatter.ISO_DATE_TIME;

        @Override
        public String serialize(Object dataFetcherResult) throws CoercingSerializeException {
            if (dataFetcherResult instanceof LocalDateTime)
                return formater.format(LocalDateTime.class.cast(dataFetcherResult));
            /*
             * if (dataFetcherResult instanceof Timestamp) return
             * formater.format(Timestamp.class.cast(dataFetcherResult).toLocalDateTime());
             */
            else
                throw new CoercingSerializeException("Not the right type");
        }

        @Override
        public LocalDateTime parseValue(Object input) throws CoercingParseValueException {
            try {
                return LocalDateTime.from(formater.parse(String.valueOf(input)));
            } catch (Exception ex) {
                throw new CoercingParseValueException("Something went wrong", ex);
            }
        }

        @Override
        public LocalDateTime parseLiteral(Object input) throws CoercingParseLiteralException {
            try {
                if (input instanceof StringValue)
                    return LocalDateTime.from(formater.parse(String.valueOf(StringValue.class.cast(input).getValue())));
                else
                    throw new Exception("Wrong input type");
            } catch (Exception ex) {
                throw new CoercingParseLiteralException("Something went wrong", ex);
            }
        }

    };

    public static Coercing<Email, String> EMAIL_COERCING = new Coercing<Email, String>() {

        @Override
        public String serialize(Object dataFetcherResult) throws CoercingSerializeException {
            try {
                return ((Email) dataFetcherResult).getEmailAddress();
            } catch (Exception e) {
                throw new CoercingParseLiteralException("Something went wrong", e);
            }
        }

        @Override
        public Email parseValue(Object input) throws CoercingParseValueException {
            try {
                return new Email(String.class.cast(input));
            } catch (Exception e) {
                throw new CoercingParseValueException("Something went wrong", e);
            }
        }

        @Override
        public Email parseLiteral(Object input) throws CoercingParseLiteralException {
            try {
                if (input instanceof StringValue)
                    return new Email(String.valueOf(StringValue.class.cast(input).getValue()));
                else
                    throw new Exception("Wrong input type:" + input.getClass());
            } catch (Exception ex) {
                throw new CoercingParseLiteralException("Something went wrong", ex);
            }
        }
        
    };

    public static Coercing<Phone, String> PHONE_COERCING = new Coercing<Phone, String>() {

        @Override
        public String serialize(Object dataFetcherResult) throws CoercingSerializeException {
            try {
                return ((Phone) dataFetcherResult).getPhone();
            } catch (Exception e) {
                throw new CoercingParseLiteralException("Something went wrong", e);
            }
        }

        @Override
        public Phone parseValue(Object input) throws CoercingParseValueException {
            try {
                return new Phone(String.class.cast(input));
            } catch (Exception e) {
                throw new CoercingParseValueException("Something went wrong", e);
            }
        }

        @Override
        public Phone parseLiteral(Object input) throws CoercingParseLiteralException {
            try {
                if (input instanceof StringValue)
                    return new Phone(String.valueOf(StringValue.class.cast(input).getValue()));
                else
                    throw new Exception("Wrong input type:" + input.getClass());
            } catch (Exception ex) {
                throw new CoercingParseLiteralException("Something went wrong", ex);
            }
        }

    };
}