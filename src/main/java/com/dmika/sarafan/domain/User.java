package com.dmika.sarafan.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    private String id;

    private String name;

    private String userpic;

    private String email;

    private String gender;

    private String locale;

    private LocalDateTime lastVisit;

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserpic(String userpic) {
        this.userpic = userpic;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public void setLastVisit(LocalDateTime lastVisit) {
        this.lastVisit = lastVisit;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUserpic() {
        return userpic;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getLocale() {
        return locale;
    }

    public LocalDateTime getLastVisit() {
        return lastVisit;
    }
}
