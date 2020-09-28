package lv.maska.auth;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;

import lv.maska.Repository;
import lv.maska.domain.HashAuth;
import lv.maska.domain.PasswordAuth;

public class Authenticator {
    
    public static Map<String, String> authenticate(GrantType grantType, String Authenticator, EntityManager manager) throws Exception {
        Integer personId = null;
        Boolean includeRefreshToken = false;
        Map<String, String> tokenMap = new HashMap<String, String>();
        if (GrantType.HASH_CODE.equals(grantType)){
            Map<String, Object> whereClauseMap = new HashMap<String, Object>();
            whereClauseMap.put("hashCode", Authenticator);
            HashAuth hashAuth = Repository.getSingle(manager, whereClauseMap, HashAuth.class);
            if (hashAuth.getIsActive()){
                personId = hashAuth.getPersonId();
                hashAuth.markAsUsed(manager);
                includeRefreshToken = true;
            } else
                return null;//TODO return 403
        } else if (GrantType.PASSWORD.equals(grantType)) {
            BasicAuth basicAuth = new BasicAuth(Authenticator);
            PasswordAuth auth =  Repository.getPasswordForClient(manager, basicAuth.getEmail());
            if (auth.checkPassword(basicAuth.getPassword())){
                personId = auth.getPersonId();
                includeRefreshToken = true;
            }
            else
                return null;//TODO return 403
        } else if (GrantType.REFRESH_TOKEN.equals(grantType)) {
            RefreshToken token = new RefreshToken(Authenticator);
            personId = token.getPersonId();
        }

        if (includeRefreshToken)
            tokenMap.put("RefreshToken", new RefreshToken(personId).getToken());
        tokenMap.put("AccessToken", new AccessToken(personId).getToken());
        return tokenMap;
    }
}
