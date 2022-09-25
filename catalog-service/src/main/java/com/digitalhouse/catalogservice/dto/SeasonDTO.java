package com.digitalhouse.catalogservice.dto;

import java.util.List;

public class SeasonDTO {
    private Long id;
    private Integer seasonNumber;

    private List<ChapterDTO> chapters;

    public SeasonDTO() {
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

    public List<ChapterDTO> getChapters() {
        return chapters;
    }

    public void setChapters(List<ChapterDTO> chapters) {
        this.chapters = chapters;
    }

}
