package lv.maska;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import lv.maska.domain.Attendance;
import lv.maska.domain.BaseEntity;
import lv.maska.domain.Email;
import lv.maska.domain.PasswordAuth;
import lv.maska.domain.Person;

/**
 * Repository
 */
public class Repository {

    private static Repository instance;
    public static EntityManager getEntityManager() {
        if (instance == null)
            instance = new Repository();
        return instance.getEMFactory().createEntityManager();
    }

    private EntityManagerFactory emfactory;
    public EntityManagerFactory getEMFactory() {
        return emfactory;
    }
    public Repository() {
        Map<String, String> properties = new HashMap<>();
        // properties.put("hibernate.dialect",
        // "org.hibernate.dialect.Dialect.PostgreSQL95Dialect");
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.put("hibernate.connection.driver_class", "org.postgresql.Driver");
        properties.put("hibernate.connection.username", "pi");
        properties.put("hibernate.connection.password", "customraspberry");
        properties.put("hibernate.connection.url", "jdbc:postgresql://192.168.88.3:5432/maska");
        //properties.put("show_sql", "true");
        //properties.put("hibernate.format_sql", "true");

        emfactory = Persistence.createEntityManagerFactory("persistence", properties);
    }

    public static <T extends BaseEntity> T getById(EntityManager manager, Integer id, Class<T> clazz) {
        return manager.find(clazz, id);
    }

    public static PasswordAuth getPasswordForClient(EntityManager manager, Email email) {
        manager.createNativeQuery("SELECT p FROM " + PasswordAuth.class.getSimpleName() + " p LEFT JOIN " + Person.class.getSimpleName() + " pers ON pers.id=p.personId WHERE p.email=:email")
            .setParameter("email", email.getAddress())
            .getSingleResult();
        return null;
    }

    public static <T extends BaseEntity> T getSingle(EntityManager manager, Map<String, Object> whereClauseMap, Class<T> clazz) {
        String queryString = "SELECT i FROM " + clazz.getSimpleName() + " i";
        for (Entry<String, Object> entry : whereClauseMap.entrySet()){
            queryString += "i." + entry.getKey() + "=:" + entry.getKey();
        }
        Query query = manager.createQuery(queryString);
        for (Entry<String, Object> entry : whereClauseMap.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return clazz.cast(query.getSingleResult());
    }

    public static <T extends BaseEntity> Collection<T> getList(EntityManager manager, Class<T> clazz) {
        return castList(clazz, manager.createQuery("SELECT i FROM " + clazz.getSimpleName() + " i").getResultList());
    }

    public static <T extends BaseEntity> Collection<T> getListByFK(EntityManager manager, Class<T> clazz, Integer id, String fkColumn) {
        return castList(clazz, manager.createQuery("SELECT i FROM " + clazz.getSimpleName() + " i WHERE i." + fkColumn + "=:id").setParameter("id", id).getResultList());
    }

    private static <T extends BaseEntity> Collection<T> castList(Class<T> clazz, Collection<?> list) {
        List<T> r = new ArrayList<T>(list.size());
        for(Object o: list)
            r.add(clazz.cast(o));
        return r;
    }

    public static void save(EntityManager manager, BaseEntity object) {
        EntityTransaction transaction = manager.getTransaction();
        transaction.begin();
        if (object.getId() != null)
            manager.merge(object);
        else
            manager.persist(object);
        transaction.commit();
    }

    public static List<Object[]> getPersonsWithEventsWithoutAttendance(EntityManager manager) {
        Collection<Attendance> attCol = castList(Attendance.class, manager.createQuery("SELECT a FROM Attendance a").getResultList());
        Collection<String> map = attCol.stream().map(att -> att.getPersonId() + "-" + att.getEventId()).collect(Collectors.toList());

        return manager.createQuery("SELECT e, p FROM Event e, Person p WHERE CONCAT(p.id, '-', e.id) NOT IN :pers_ev_pairs AND !e.isActive AND !p.isActive").setParameter("pers_ev_pairs", map).getResultList();
    }
}