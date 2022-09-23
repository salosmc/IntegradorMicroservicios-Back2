package com.digitalhouse.serieservice.repository;

import com.digitalhouse.serieservice.models.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SerieRepository extends JpaRepository<Serie, Long> {
    List<Serie> findByGenre(String genre);
}
