package lv.maska.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import lombok.Data;

/**
 * Person
 */
@Entity
@Data
public class Person extends BaseEntity{

    String name;
    String personalNumber;
    Phone phoneNumber;
    Email email;
    @Column(name = "is_verified")
    Boolean isVerified;
    @Column(name = "voice_group")
    @Enumerated(EnumType.STRING)
    VoiceType voiceType;
}