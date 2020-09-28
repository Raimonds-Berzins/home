package lv.maska.processors;

import java.util.List;

import javax.persistence.EntityManager;

import lv.maska.Repository;
import lv.maska.domain.Attendance;
import lv.maska.domain.Event;
import lv.maska.domain.Person;

public class ProcessorRunner implements Runnable{

    @Override
    public void run() {
        EntityManager manager = Repository.getEntityManager();
        List<Object[]> personEventList = Repository.getPersonsWithEventsWithoutAttendance(manager);
        for(Object[] personEventPair : personEventList) {
            try {
                Person person = (Person) personEventPair[1];
                Event event = (Event) personEventPair[0];
                Attendance attendance = new Attendance();
                attendance.setEventId(event.getId());
                attendance.setPersonId(person.getId());
                Repository.save(manager, attendance);
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}