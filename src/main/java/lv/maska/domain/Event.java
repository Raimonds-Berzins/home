package lv.maska.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import lombok.Data;

/**
 * Event
 */
@Entity
@Data
public class Event extends BaseEntity{

    String name;
    
    LocalDateTime time;

    @Enumerated(EnumType.STRING)
    EventType type;
}