package lv.maska.auth;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

public class AccessToken {//TODO create JWT superclass

    private Builder jwtBuilder;
    private Algorithm algorithm;
    private String tokenString;
    private static JWTVerifier verifier = JWT.require(Algorithm.HMAC256("secret")).build();
    public AccessToken() {
        jwtBuilder = JWT.create()
        .withIssuer("maska.lv")
        .withAudience("maska.lv")
        .withExpiresAt(Date.from(LocalDateTime.now().plus(1, ChronoUnit.MINUTES).atZone(ZoneId.systemDefault()).toInstant()));
        algorithm = Algorithm.HMAC256("secret");
    }

    public AccessToken(Integer personId) {
        jwtBuilder = JWT.create()
        .withIssuer("maska.lv")
        .withAudience("maska.lv")
        .withExpiresAt(Date.from(LocalDateTime.now().plus(1, ChronoUnit.MINUTES).atZone(ZoneId.systemDefault()).toInstant()));
        algorithm = Algorithm.HMAC256("secret");
    }

    public AccessToken(String tokenString) {
        this.tokenString = tokenString;
    }

    public Boolean isValid() {
        try {
            DecodedJWT jwt = verifier.verify(tokenString);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public String getToken() {
        if(tokenString == null)
            tokenString = jwtBuilder.sign(algorithm);
        
        return tokenString;
    }
}
