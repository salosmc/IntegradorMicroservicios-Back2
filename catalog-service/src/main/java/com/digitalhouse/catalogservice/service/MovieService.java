package com.digitalhouse.catalogservice.service;

import java.util.ArrayList;
import java.util.List;

import com.digitalhouse.catalogservice.client.MovieClient;
import com.digitalhouse.catalogservice.client.SerieClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.digitalhouse.catalogservice.dto.MovieDTO;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class MovieService {

    @Value("${queue.movie.name}")
    private String movieQueue;
    private final Logger LOG = LoggerFactory.getLogger(MovieService.class);
    private final MovieClient movieClient;

    //Agregamos a serie como Cliente.
    private final RabbitTemplate rabbitTemplate;

    public MovieService(MovieClient movieClient, RabbitTemplate rabbitTemplate) {
        this.movieClient = movieClient;
        this.rabbitTemplate = rabbitTemplate;
    }

    /*Entiendo que aca serian los metodos de movie*/
    public ResponseEntity<List<MovieDTO>> findMovieByGenre(String genre) {
        LOG.info("Se va a incluir el llamado al movie-service...");
        return movieClient.getMovieByGenre(genre);
    }

    @CircuitBreaker(name = "movies", fallbackMethod = "moviesFallbackMethod")
    public ResponseEntity<List<MovieDTO>> findMovieByGenre(String genre, Boolean throwError) {
        LOG.info("Se va a incluir el llamado al movie-service...");
        return movieClient.getMovieByGenreWithThrowError(genre, throwError);
    }

    private ResponseEntity<List<MovieDTO>> moviesFallbackMethod(CallNotPermittedException exception) {
        LOG.info("se activó el circuitbreaker");
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
    }

    public void saveMovie(MovieDTO movieDTO) {
        //buscamos el movie en la base de datos de catalog por genero
       rabbitTemplate.convertAndSend(movieQueue, movieDTO);
    }

}
