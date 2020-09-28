package lv.maska.fetchers;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import javax.persistence.EntityManager;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lv.maska.Repository;
import lv.maska.domain.Attendance;
import lv.maska.domain.BaseEntity;
import lv.maska.domain.Event;
import lv.maska.domain.Person;

public class MutationFetcher implements DataFetcher<BaseEntity> {

    private static Map<String, Class<? extends BaseEntity>> classMapping = new HashMap<>();
    {
        classMapping.put("person", Person.class);
        classMapping.put("event", Event.class);
        classMapping.put("attendance", Attendance.class);
    }
    
    @Override
    public BaseEntity get(DataFetchingEnvironment environment) throws Exception {
        EntityManager man = environment.getContext();
        Optional<Entry<String, Class<? extends BaseEntity>>> objEntry = classMapping.entrySet().stream()
                    .filter(entry -> environment.containsArgument(entry.getKey()))
                    .findFirst();
        
        if (!objEntry.isPresent())
            throw new Exception("Entity type not found");
        
        Map<String, Object> objectMap = environment.getArgument(objEntry.get().getKey());
        BaseEntity entity = null;
        if (objectMap.containsKey("id")){
            Integer id = Integer.valueOf(objectMap.get("id").toString());
            entity = Repository.getById(man, id, objEntry.get().getValue());

        } else
            entity = objEntry.get().getValue().getConstructor().newInstance();

        entity.updateWithMap(objectMap);
        Repository.save(man, entity);
        return entity;
    }
    
}