package com.digitalhouse.movieservice.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQSenderConfig {

    /*temple de cola de movie*/
    @Value("${queue.movie.name}")
    private String movieQueue;

    @Bean
    public Queue queue() {
        return new Queue(this.movieQueue, true);
    }


    /*voy a intentar configurar una cola de catalog*/
    @Value("${queue.catalog.name}")
    private String catalogQueue;

    @Bean
    public Queue queueCatalog() {
        return new Queue(this.catalogQueue, true);
    }

}
