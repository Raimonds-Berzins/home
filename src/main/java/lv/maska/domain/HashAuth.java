package lv.maska.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Table;

import lv.maska.Repository;

@Entity
@Table(name = "hash_auth")
public class HashAuth extends BaseEntity {
    @Column(name = "hash")
    String hashCode;
    @Column(name = "person_id")
    Integer personId;
    @Column(name = "use_time")
    LocalDateTime useTime;

    HashAuth() {}

    public HashAuth(Integer personId) {
        this.personId = personId;
        hashCode = UUID.randomUUID().toString();
    }

    public Integer getPersonId() {
        return personId;
    }

    public void markAsUsed(EntityManager manager) {
        this.useTime = LocalDateTime.now();
        this.isActive = false;
        Repository.save(manager, this);
    }
}
