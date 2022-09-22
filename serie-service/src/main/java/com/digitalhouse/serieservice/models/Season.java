package com.digitalhouse.serieservice.models;

import javax.persistence.*;
import java.util.List;
@Entity
public class Season {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer seasonNumber;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "season_id")
    private List<Chapter> chapters;

    public Season() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(Integer seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }

    @Override
    public String toString() {
        return "{\"Season\":{"
                + "\"id\":\"" + id +"\", "
                + "\"seasonNumber\":\"" + seasonNumber+"\", "
                + "\"chapters\":\"" + chapters.toString() + "\"}}";
    }
}
