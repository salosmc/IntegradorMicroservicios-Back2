package com.digitalhouse.catalogservice.repository;


import com.digitalhouse.catalogservice.entities.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatalogoRepository extends JpaRepository<Catalog, Long> {

}
