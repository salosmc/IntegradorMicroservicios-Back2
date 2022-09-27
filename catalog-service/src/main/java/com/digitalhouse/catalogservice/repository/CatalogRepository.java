package com.digitalhouse.catalogservice.repository;


import com.digitalhouse.catalogservice.models.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatalogRepository extends JpaRepository<Catalog, Long> {
    Catalog findByGenre(String genre);
}
