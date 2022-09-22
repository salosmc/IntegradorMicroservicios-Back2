package com.digitalhouse.serieservice.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Chapter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer number;
    private String urlStream;

    public Chapter() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getUrlStream() {
        return urlStream;
    }

    public void setUrlStream(String urlStream) {
        this.urlStream = urlStream;
    }

    @Override
    public String toString() {
        return "{\"Chapter\":{"
                + "\"id\":\"" + id +"\" ,"
                + "\"name\":\"" + name +"\" ,"
                + "\"number\":\"" + number + "\" ,"
                + "\"urlStream\":\"" + urlStream + "\"}}";
    }
}
