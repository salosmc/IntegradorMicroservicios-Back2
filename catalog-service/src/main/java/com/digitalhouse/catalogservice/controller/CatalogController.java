package com.digitalhouse.catalogservice.controller;

import com.digitalhouse.catalogservice.models.Catalog;
import com.digitalhouse.catalogservice.repository.CatalogRepository;
import com.digitalhouse.catalogservice.service.SerieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.digitalhouse.catalogservice.service.MovieService;
import com.digitalhouse.catalogservice.models.Movie;

@RestController
@RequestMapping("/catalogs")
public class CatalogController {

    private final MovieService movieService;
    private final SerieService serieService;
    private final CatalogRepository catalogoRepository;

    public CatalogController(MovieService movieService, CatalogRepository catalogoRepository, SerieService serieService){
        this.serieService = serieService;
        this.movieService = movieService;
        this.catalogoRepository = catalogoRepository;
    }

    /*crud de catalog*/
    @GetMapping("/{genre}")
    ResponseEntity<Catalog> getCatalog(@PathVariable String genre){
        Catalog catalogo = new Catalog();
        catalogo.setGenre(genre);
        //findSerieByGenre
        //catalogo.setSeries(serieService.findSerieByGenre(genre));
        //getMovieByGenre
        catalogo.setMovies(movieService.findMovieByGenre(genre));
        //Y setemos Catalogo
        return ResponseEntity.ok().body(catalogo);
    }


    /*Aca empezamos con movies*/
    /*
    @GetMapping("/movies/{genre}")
    ResponseEntity<List<Movie>> getCatalogMovie(@PathVariable String genre) {
        return movieService.findMovieByGenre(genre);
    }
    @GetMapping("/withErrors/{genre}")
    ResponseEntity<List<MovieDTO>> getGenre(@PathVariable String genre, @RequestParam("throwError") Boolean throwError) {
        return movieService.findMovieByGenre(genre, throwError);
    }
    */

    @PostMapping
    ResponseEntity<String> saveMovie(@RequestBody Movie movieDTO) {
        //consumimos el cliente para guardar la pelicula en movie-service
        movieService.saveMovie(movieDTO);

        return ResponseEntity.ok("La pelicula fue creada");
    }
}
