package lv.maska.auth;

import graphql.execution.instrumentation.InstrumentationState;
import graphql.execution.instrumentation.SimpleInstrumentation;
import graphql.execution.instrumentation.parameters.InstrumentationFieldFetchParameters;
import graphql.schema.DataFetcher;

public class AuthInstrumentation extends SimpleInstrumentation {
    @Override
    public InstrumentationState createState() {
        //
        // instrumentation state is passed during each invocation of an Instrumentation method
        // and allows you to put stateful data away and reference it during the query execution
        //
        return null;
    }

    @Override
    public DataFetcher<?> instrumentDataFetcher(DataFetcher<?> dataFetcher, InstrumentationFieldFetchParameters parameters) {
        //
        // this allows you to intercept the data fetcher used to fetch a field and provide another one, perhaps
        // that enforces certain behaviours or has certain side effects on the data
        //
        return dataFetcher;
    }
}
