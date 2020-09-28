package lv.maska.fetchers;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.execution.instrumentation.SimpleInstrumentation;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLScalarType;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import lv.maska.domain.BaseEntity;

public class GraphQLImpl {

    static GraphQLImpl instance;

    public static GraphQLImpl getInstance() {
        if (instance == null)
            instance = new GraphQLImpl();
        return instance;
    }

    public static ExecutionResult executeQuerry(ExecutionInput input) {
        return getInstance().getGraphQL().execute(input);
    }

    public GraphQL getGraphQL() {
        return graphQL;
    }

    GraphQL graphQL;

    private GraphQLImpl() {
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("schema.graphql");

        SchemaParser schemaParser = new SchemaParser();
        
        TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(is);

        Map<String, DataFetcher> fetcherMap = new HashMap<>();
        DataFetcher<BaseEntity> idFetcher = new IdFetcher();
        DataFetcher<BaseEntity> mutationFetcher = new MutationFetcher();
        fetcherMap.put("person", idFetcher);
        fetcherMap.put("event", idFetcher);
        fetcherMap.put("attendance", idFetcher);
        DataFetcher<Collection<? extends BaseEntity>> listFetcher = new ListFetcher();

        RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring()
                .scalar(GraphQLScalarType.newScalar().coercing(ScalarCoercings.DATE_COERCING).name("DateTime").build())
                .scalar(GraphQLScalarType.newScalar().coercing(ScalarCoercings.EMAIL_COERCING).name("Email").build())
                .scalar(GraphQLScalarType.newScalar().coercing(ScalarCoercings.PHONE_COERCING).name("Phone").build())
                .type("Query", builder -> builder.dataFetchers(fetcherMap))
                .type("Person", builder -> builder.dataFetcher("attendances", listFetcher))
                .type("Event", builder -> builder.dataFetcher("attendances", listFetcher))
                .type("Attendance", builder -> builder.dataFetcher("person", idFetcher))
                .type("Attendance", builder -> builder.dataFetcher("event", idFetcher))
                .type("Mutation", builder -> builder.dataFetcher("event", mutationFetcher))
                .type("Mutation", builder -> builder.dataFetcher("person", mutationFetcher))
                .directive("auth", new AuthDirective())
                .build();

        GraphQLSchema graphQLSchema = new SchemaGenerator().makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);

        //GraphQLSchema.newSchema().
        graphQL = GraphQL.newGraphQL(graphQLSchema).instrumentation(new SimpleInstrumentation()).build();
    }
}