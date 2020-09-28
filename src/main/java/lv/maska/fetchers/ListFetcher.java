package lv.maska.fetchers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lv.maska.Repository;
import lv.maska.domain.Attendance;
import lv.maska.domain.BaseEntity;
import lv.maska.domain.Event;
import lv.maska.domain.Person;

public class ListFetcher implements DataFetcher<Collection<? extends BaseEntity>> {

    private static Map<String, Class<? extends BaseEntity>> classMapping = new HashMap<>();
    private static Map<Class<? extends BaseEntity>, String> fkMap = new HashMap<>();
    {
        classMapping.put("person", Person.class);
        classMapping.put("event", Event.class);
        classMapping.put("attendances", Attendance.class);

        fkMap.put(Person.class, "personId");
        fkMap.put(Event.class, "eventId");
    }

    @Override
    public Collection<? extends BaseEntity> get(DataFetchingEnvironment environment) throws Exception {
        BaseEntity parentObj = environment.getSource();
        
        return Repository.getListByFK(environment.getContext()
            , classMapping.get(environment.getFieldDefinition().getName())
            , parentObj.getId()
            , fkMap.get(parentObj.getClass()));
    }
    
}