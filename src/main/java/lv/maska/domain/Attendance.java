package lv.maska.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import lombok.Data;

/**
 * Attendance
 */
@Entity
@Data
public class Attendance extends BaseEntity{

    @Column(name = "person_id")
    Integer personId;
    @Column(name = "event_id")
    Integer eventId;

    @Enumerated(EnumType.STRING)
    AttendanceStatus status;

    public Attendance() {
        status = AttendanceStatus.UNKNOWN;
    }
}