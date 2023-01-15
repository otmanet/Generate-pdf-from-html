package com.example.GeneratePdfHtml.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.GeneratedValue;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "Student")
public class Student {
    @Id

    private String id;
    private  String firstname;
    private String lastname;
    private String  birthday;
    private String city;

    private String cin;

    public Student() {
        super();
    }

    public Student(String firstname, String lastname, String birthday, String city, String cin) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthday = birthday;
        this.city = city;
        this.cin =cin;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCin() {

        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }
}
