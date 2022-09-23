package com.digitalhouse.serieservice.controller;

import com.digitalhouse.serieservice.models.Serie;
import com.digitalhouse.serieservice.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/series")
public class SerieController {
    private final SerieService service;
    @Autowired
    public SerieController(SerieService serieService){
        this.service=serieService;
    }

    @GetMapping("/{genre}")
    ResponseEntity<List<Serie>> getSerieByGenre(@PathVariable String genre){
        return ResponseEntity.ok().body(service.findByGenre(genre));
    }

}
