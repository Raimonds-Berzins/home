package lv.maska.auth;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import lv.maska.domain.Email;

public class BasicAuth {

    Email email;
    String password;
    
    public BasicAuth(String authHeader) throws Exception {
        if (authHeader != null && authHeader.startsWith("Basic")) {
            // Authorization: Basic base64credentials
            String base64Credentials = authHeader.substring("Basic".length()).trim();
            byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(credDecoded, StandardCharsets.UTF_8);
            // credentials = username:password
            final String[] values = credentials.split(":", 2);
            email = new Email(values[0]);
            password = values[1];
        }
    }

    public Email getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
}
