package lv.maska.fetchers;

import java.util.HashMap;
import java.util.Map;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lv.maska.Repository;
import lv.maska.domain.Attendance;
import lv.maska.domain.BaseEntity;
import lv.maska.domain.Event;
import lv.maska.domain.Person;

public class IdFetcher implements DataFetcher<BaseEntity> {

    private static Map<String, Class<? extends BaseEntity>> classMapping = new HashMap<>();
    {
        classMapping.put("person", Person.class);
        classMapping.put("event", Event.class);
        classMapping.put("attendance", Attendance.class);
    }
    @Override
    public BaseEntity get(DataFetchingEnvironment environment) throws Exception {
        String fieldName = environment.getFieldDefinition().getName();
        BaseEntity parentObject = environment.getSource();
        Integer id = environment.containsArgument("id") ? Integer.valueOf(environment.getArgument("id")) : null;
        id = parentObject == null ? id : parentObject.getId();
        return Repository.getById(environment.getContext(), id, classMapping.get(fieldName));
    }
    
}