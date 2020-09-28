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

public class RefreshToken {
    Integer personId;
    String tokenString;
    private Builder jwtBuilder;
    private Algorithm algorithm;
    public RefreshToken(String tokenString) {
        this.tokenString = tokenString;
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256("secret")).build();
        DecodedJWT jwt = verifier.verify(tokenString);
        personId = jwt.getClaim("person_id").asInt();
    }

    public RefreshToken(Integer personId) {
        jwtBuilder = JWT.create()
        .withIssuer("maska.lv")
        .withAudience("maska.lv")
        .withExpiresAt(Date.from(LocalDateTime.now().plus(1, ChronoUnit.MINUTES).atZone(ZoneId.systemDefault()).toInstant()));
        algorithm = Algorithm.HMAC256("secret");
    }

    public String getToken() {
        if(tokenString == null)
            tokenString = jwtBuilder.sign(algorithm);
        
        return tokenString;
    }

    public Integer getPersonId() {
        return personId;
    }
}
