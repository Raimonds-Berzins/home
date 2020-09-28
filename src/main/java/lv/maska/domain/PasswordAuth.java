package lv.maska.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "password_auth")
public class PasswordAuth extends BaseEntity {
    @Column(name = "person_id")
    Integer personId;
    @Column(name = "password_hash")
    String passwordHashCode;

    PasswordAuth() {}
    public PasswordAuth(String password, Integer personId){
        this.personId = personId;
        this.setPassword(password);
    }
    public void setPassword(String password) {
        passwordHashCode = password;
    }

    public Boolean checkPassword(String password) {
        return this.passwordHashCode.equals(password);
    }

    public Integer getPersonId() {
        return personId;
    }
}
