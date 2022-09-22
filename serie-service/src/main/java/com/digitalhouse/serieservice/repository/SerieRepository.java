package com.digitalhouse.serieservice.repository;

import com.digitalhouse.serieservice.models.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    List<Serie> findByGenre(String genre);
}
