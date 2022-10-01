package com.digitalhouse.catalogservice.service;

import java.util.ArrayList;
import java.util.List;
import com.digitalhouse.catalogservice.client.MovieClient;
import com.digitalhouse.catalogservice.models.Catalog;
import com.digitalhouse.catalogservice.repository.CatalogRepository;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import com.digitalhouse.catalogservice.models.Movie;

@Service
public class MovieService {
    private String genre;
    private final Logger LOG = LoggerFactory.getLogger(MovieService.class);
    private final MovieClient movieClient;
    private final CatalogRepository repository;
    public MovieService(MovieClient movieClient, CatalogRepository repository) {
        this.movieClient = movieClient;
        this.repository = repository;
    }
    @CircuitBreaker(name="movies", fallbackMethod = "moviesFallBackMethod")
    public List<Movie> findMovieByGenre(String genre) {
        this.genre= genre;
        LOG.warn("Buscamos peliculas a [movie-service] por genero : "+genre);
        List<Movie> movies = movieClient.getMovieByGenre(genre).getBody();
        if(movies.isEmpty() || movies == null){
            LOG.error("No se encontraron peliculas en [movie-service]");
            return new ArrayList<Movie>();
        }
        LOG.warn("Buscamos catalogo por genero : "+genre);
        Catalog catalogo = repository.findByGenre(genre);
        if(catalogo == null){
            //creamos catalogo
            LOG.warn("No se encontro catalogo en BD");
            catalogo = new Catalog();
            catalogo.setGenre(genre);
            catalogo.setMovies(new ArrayList<Movie>());
        }
        LOG.info("Se encontro/creo catalogo por el genero : "+catalogo.getGenre());
        catalogo.setMovies(movies);
        LOG.warn("Persistiendo en BD");
        repository.save(catalogo);
        return movies;
    }
    private List<Movie> moviesFallBackMethod(CallNotPermittedException exception) {
        LOG.info("Se activó el circuitbreaker");
        LOG.warn("Se busca información en base de datos local");
        Catalog catalogo = repository.findByGenre(this.genre);
        LOG.info("Se encontro catalogo de genero : "+catalogo.getGenre());
        return catalogo.getMovies();
    }
    @RabbitListener(queues = {"${queue.movie.name}"})
    public void saveMovie(Movie movie){
        try{
            LOG.warn("Buscando catalogo por genero : "+movie.getGenre());
            Catalog catalogo = repository.findByGenre(movie.getGenre());
            if(catalogo == null){
                LOG.warn("Catalogo es null");
                catalogo = new Catalog();
                catalogo.setGenre(movie.getGenre());
            }
            if(catalogo.getMovies() == null){
                catalogo.setMovies(new ArrayList<Movie>());
            }
            List<Movie> movies = catalogo.getMovies();
            LOG.warn("Se agrega a catalogo la pelicula : "+movie.toString());
            movies.add(movie);
            catalogo.setMovies(movies);
            LOG.info("Se persisite catalogo en BD");
            repository.save(catalogo);
        }
        catch (Exception e){
            LOG.error(e.getMessage());
        }
    }

}