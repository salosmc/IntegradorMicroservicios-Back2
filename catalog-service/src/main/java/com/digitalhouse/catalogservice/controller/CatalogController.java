package com.digitalhouse.catalogservice.controller;

import java.util.List;

import com.digitalhouse.catalogservice.entities.Catalog;
import com.digitalhouse.catalogservice.repository.CatalogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final MovieService movieService;
    private final CatalogRepository catalogoRepository;

    public CatalogController(MovieService movieService, CatalogRepository catalogoRepository){
        this.movieService = movieService;
        this.catalogoRepository = catalogoRepository;
    }

    /*crud de catalog*/
    @GetMapping("/{genre}")
    ResponseEntity<Catalog> getCatalog(@PathVariable String genre){
        //estamos supiniendo que no hay mas de un catalogo por genero.
        return ResponseEntity.ok().body(catalogoRepository.findByGenre(genre));
    }

    // faltaria crear, eliminar y actualizar catalog. pero no me parece que hace al modelo de negocio.

    /*Aca empezamos con movies*/
    @GetMapping("/movies/{genre}")
    ResponseEntity<List<MovieDTO>> getGenre(@PathVariable String genre) {
        return movieService.findMovieByGenre(genre);
    }
    /*
    @GetMapping("/withErrors/{genre}")
    ResponseEntity<List<MovieDTO>> getGenre(@PathVariable String genre, @RequestParam("throwError") Boolean throwError) {
        return movieService.findMovieByGenre(genre, throwError);
    }
    */

    @PostMapping
    ResponseEntity<String> saveMovie(@RequestBody MovieDTO movieDTO) {
        //consumimos el cliente para guardar la pelicula en movie-service
        movieService.saveMovie(movieDTO);

        //tener una validacion de saveMovie.
        //capaz que el otro servicio complete una cola para catalog y persistir los datos como un string.

        //persisto esta pelicula


        return ResponseEntity.ok("La pelicula fue creada");
    }

    /*vemos ejemplos consumiendo serie*/

}
