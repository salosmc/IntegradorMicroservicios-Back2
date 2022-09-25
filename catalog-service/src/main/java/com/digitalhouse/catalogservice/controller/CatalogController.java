package com.digitalhouse.catalogservice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.digitalhouse.catalogservice.service.MovieService;
import com.digitalhouse.catalogservice.dto.MovieDTO;

@RestController
@RequestMapping("/catalogs")
public class CatalogController {

    private final MovieService catalogService;

    public CatalogController(MovieService catalogService){
        this.catalogService = catalogService;
    }

    /*Hasta aca esta hecho lo de movie*/
    @GetMapping("/{genre}")
    ResponseEntity<List<MovieDTO>> getGenre(@PathVariable String genre) {
        return catalogService.findMovieByGenre(genre);
    }
    @GetMapping("/withErrors/{genre}")
    ResponseEntity<List<MovieDTO>> getGenre(@PathVariable String genre, @RequestParam("throwError") Boolean throwError) {
        return catalogService.findMovieByGenre(genre, throwError);
    }
    @PostMapping
    ResponseEntity<String> saveMovie(@RequestBody MovieDTO movieDTO) {
        catalogService.saveMovie(movieDTO);
        return ResponseEntity.ok("La pelicula fue creada");
    }

    /*vemos ejemplos consumiendo serie*/


}
