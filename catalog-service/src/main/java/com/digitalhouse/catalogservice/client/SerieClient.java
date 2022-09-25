package com.digitalhouse.catalogservice.client;

import com.digitalhouse.catalogservice.dto.SerieDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "serie-service")
public interface SerieClient {

    @GetMapping("/series/{genre}")
    ResponseEntity<List<SerieDTO>> getSerieByGenre(@PathVariable String genre);

}
