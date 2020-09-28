package lv.maska.fetchers;

import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;

public class AuthDirective implements SchemaDirectiveWiring {
    
    @Override
    public GraphQLFieldDefinition onField(SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> environment) {
        
        // TODO Auto-generated method stub
        return SchemaDirectiveWiring.super.onField(environment);
    }
}
