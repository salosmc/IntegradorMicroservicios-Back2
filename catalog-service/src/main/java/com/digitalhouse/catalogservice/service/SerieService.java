package com.digitalhouse.catalogservice.service;


import com.digitalhouse.catalogservice.client.SerieClient;
import com.digitalhouse.catalogservice.models.Catalog;
import com.digitalhouse.catalogservice.models.Serie;
import com.digitalhouse.catalogservice.repository.CatalogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SerieService {
    private final Logger LOG = LoggerFactory.getLogger(SerieService.class);
    private final SerieClient serieClient;
    private final CatalogRepository repository;

    private String genre;
    public SerieService(SerieClient serieClient, CatalogRepository repository) {
        this.serieClient = serieClient;
        this.repository = repository;
    }
    public List<Serie> findSerieByGenre(String genre) {
        this.genre=genre;
        LOG.warn("Buscamos series a [serie-service] por genero : "+genre);
        List<Serie> series = serieClient.getSerieByGenre(genre).getBody();
        if(series.isEmpty()||series == null){
            LOG.error("No se encontraron series en [serie-service]");
            return new ArrayList<Serie>();
        }
        LOG.warn("Buscamos catalogo por genero : "+genre);
        Catalog catalogo = repository.findByGenre(genre);
        if(catalogo == null){
            LOG.warn("No se encontro catalogo en BD");
            catalogo = new Catalog();
            catalogo.setGenre(genre);
            catalogo.setSeries(new ArrayList<Serie>());
        }
        LOG.info("Se encontro/creo catalogo por el genero : "+catalogo.getGenre());
        catalogo.setSeries(series);
        LOG.warn("Persistiendo en BD");
        repository.save(catalogo);
        return series;
    }
    /*Metodo para guardar*/
    @RabbitListener(queues = {"${queue.serie.name}"})
    public void saveSerie(Serie serie) {
        try {
            LOG.warn("Buscando catalogo por genero : " + serie.getGenre());
            Catalog catalogo = repository.findByGenre(serie.getGenre());
            if (catalogo == null) {
                LOG.warn("No se encontro ningun catalogo");
                catalogo = new Catalog();
                catalogo.setGenre(serie.getGenre());
            }
            if(catalogo.getSeries() == null){
                catalogo.setSeries(new ArrayList<Serie>());
            }
            List<Serie> series = catalogo.getSeries();
            LOG.warn("Se agrega a catalogo la serie ");
            series.add(serie);
            catalogo.setSeries(series);
            LOG.info("Se persisite catalogo en BD");
            repository.save(catalogo);

        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }
}
