package com.digitalhouse.catalogservice.service;


import com.digitalhouse.catalogservice.client.SerieClient;
import com.digitalhouse.catalogservice.dto.SerieDTO;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SerieService {
    @Value("${queue.serie.name}")
    private String serieQueue;
    private final Logger LOG = LoggerFactory.getLogger(SerieService.class);
    private final SerieClient serieClient;

    private final RabbitTemplate rabbitTemplate;

    public SerieService(SerieClient serieClient, RabbitTemplate rabbitTemplate) {
        this.serieClient = serieClient;
        this.rabbitTemplate = rabbitTemplate;
    }

    /*Entiendo que aca serian los metodos de movie*/
    public ResponseEntity<List<SerieDTO>> findSerieByGenre(String genre) {
        LOG.info("Se va a incluir el llamado al serie-service..");
        return serieClient.getSerieByGenre(genre);
    }
/*
    @CircuitBreaker(name = "movies", fallbackMethod = "moviesFallbackMethod")
    public ResponseEntity<List<MovieDTO>> findMovieByGenre(String genre, Boolean throwError) {
        LOG.info("Se va a incluir el llamado al movie-service...");
        return movieClient.getMovieByGenreWithThrowError(genre, throwError);
    }

    private ResponseEntity<List<MovieDTO>> moviesFallbackMethod(CallNotPermittedException exception) {
        LOG.info("se activó el circuitbreaker");
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
    }
*/
    public void saveSerie(SerieDTO serieDTO) {
        //consumimos el cliente con sabeMovie? o que estamos haciendo?
        rabbitTemplate.convertAndSend(serieQueue, serieDTO);
    }

}
