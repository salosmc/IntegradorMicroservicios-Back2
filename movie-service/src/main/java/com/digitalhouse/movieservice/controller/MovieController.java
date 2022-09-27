package com.digitalhouse.movieservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.digitalhouse.movieservice.service.MovieService;
import com.digitalhouse.movieservice.models.Movie;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieService service;
    @Autowired
    public MovieController(MovieService movieService) {
        this.service = movieService;
    }

    @GetMapping("/{genre}")
    ResponseEntity<List<Movie>> getMovieByGenre(@PathVariable String genre) {
        return ResponseEntity.ok().body(service.findByGenre(genre));
    }


    /*Creo que este metodo se usaba para probar el circuitbreaket, por eso se le mandaba un error*/
    @GetMapping("/withErrors/{genre}")
    ResponseEntity<List<Movie>> getMovieByGenre(@PathVariable String genre, @RequestParam("throwError") boolean throwError) {
        return ResponseEntity.ok().body(service.findByGenre(genre, throwError));
    }
}
