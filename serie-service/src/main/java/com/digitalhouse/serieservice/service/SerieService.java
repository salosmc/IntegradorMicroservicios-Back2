package com.digitalhouse.serieservice.service;

import com.digitalhouse.serieservice.models.Serie;
import com.digitalhouse.serieservice.repository.SerieRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SerieService {

    /*inyectamos repository a service*/
    private final SerieRepository repository;
    @Autowired
    public SerieService(SerieRepository serieRepository){this.repository=serieRepository;}
    /*---------------------*/

    /*metodo para buscar por genero*/
    public List<Serie> findByGenre(String genre){
        //aca validamos
        //tambien podemos lanzar una exception RuntimeException()
        return repository.findByGenre(genre);
    }

    /*Metodo para guardar*/
    /*en este metodo es donde RabbitMQ esta escuchando y guardando la información*/
    @RabbitListener(queues = {"${queue.serie.name}"})
    public void save(Serie serie){
        //validaciones y demas idem al anterior
        repository.save(serie);
    }


}
