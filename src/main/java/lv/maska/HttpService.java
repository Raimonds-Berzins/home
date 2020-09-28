package lv.maska;

import java.util.Map;

import javax.persistence.EntityManager;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import lv.maska.auth.Authenticator;
import lv.maska.auth.GrantType;
import lv.maska.fetchers.GraphQLImpl;
import spark.Request;
import spark.Response;
import spark.Service;
import spark.route.HttpMethod;

public class HttpService {
    
    HttpService(Integer port){
        Service http = Service.ignite();
        http.port(port);
        http.defaultResponseTransformer(new JsonMapper());
        initService(http);
    }

    private static void initService(Service service) {
        service.before("/graph", (Request request, Response response) -> {
            /* String token = request.headers("Authorization");
            if(!token.startsWith("Bearer "))
                service.halt(403);
            token = token.replace("Bearer ", "");
            AccessToken accesToken = new AccessToken(token);
            if (!accesToken.isValid())
                service.halt(403);
            request.attribute("accessToken", accesToken); */
        });

        service.before("/graph", (Request request, Response response) -> {

            if (HttpMethod.get(request.requestMethod()).equals(HttpMethod.put)) {
                String etag = request.headers("If-Match");
                if (etag == null || etag.isBlank())
                    service.halt(403, "\"If-Match\" header not present");
            }
        });

        service.before((Request request, Response response) -> {
            request.attribute("entityManager", Repository.getEntityManager());
        });

        service.get("/", (Request request, Response response) -> "maska");

        service.get("/token",  (Request request, Response response) -> {
            GrantType grantType = GrantType.valueOf(request.params("grant_type"));
            String authHeader = request.headers("Authorization");
            Map<String, String> authMap = Authenticator.authenticate(grantType, authHeader, request.attribute("entityManager"));
            if (authMap == null)
                service.halt(403);
            return authMap;
        });

        service.post("/graph", (Request request, Response response) -> {
            try{
                String query = null;
                Map<String, Object> variableMap = null;
                String operationName = null;
                if (request.contentType().equals("application/json")){
                    JsonNode json = JsonMapper.get().readTree(request.body());
                    operationName = json.get("operationName") != null ? json.get("operationName").asText() : null;
                    query = json.get("query").asText();
                    variableMap = JsonMapper.get().convertValue(json.get("variables"), new TypeReference<Map<String, Object>>(){});

                } else if (request.contentType().equals("application/graphql")) {
                    query = request.body();
                }
                EntityManager manager = request.attribute("entityManager");
                ExecutionInput.Builder input = ExecutionInput.newExecutionInput()
                        .context(manager)
                        .query(query);
                if (variableMap != null)
                    input.variables(variableMap);
                if (operationName != null)
                    input.operationName(operationName);
                
                ExecutionResult result = GraphQLImpl.executeQuerry(input.build());
                return result;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        });
        service.after((Request request, Response response) -> {
            EntityManager manager = request.attribute("entityManager");
            manager.close();
        });
        service.after((Request request, Response response) -> {

        });

    }

    public static void main(String[] args) {
        System.out.println(GrantType.valueOf("PASSWORD"));
    }
}