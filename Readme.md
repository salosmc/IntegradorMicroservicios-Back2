# Especialización en Back End I

## Examen final

Esta evaluación será distinta a la anterior, ya que la construiremos durante las clases
siguientes. Esto no quiere decir que lo resolvamos durante los encuentros en vivo, pero con
los temas que iremos abordando podrán completar los requisitos. ¡Éxitos!

### Contextualización

El proyecto consiste de tres microservicios: Movie, Series y Catalog. Catalog es un
microservicio que lee información de Movie y Series con el objetivo de enviar un catálogo
al cliente. Catalog recibe un mensaje cada vez que una película o una serie es dada de alta
y las persiste en una base de datos no relacional de MongoDB. Cuando le llega una petición
del cliente, busca en la base de datos y responde.

Veamos un diagrama base de los microservicios:
	

![Esquema de Microservicios](/img/esquemaMicroServicios.png)


A continuación, veremos la información detallada de los microservicios.

## movie-service

El microservicio gestiona las operaciones sobre las películas. Cada película tiene como
atributo:
- id
- name
- genre
- urlStream

## serie-service

El microservicio gestiona las operaciones sobre las series. Cada serie tiene como atributos:
- id
- name
- genre
- seasons
    - id
    - seasonNumber
    - chapters
        - id
        - name
        - number
        - urlStream

## catalog-service

El microservicio tiene como objetivo invocar a los microservicios Movies y Series. Estos
microservicios deben ser invocados cada vez que se carga una nueva película o serie y se
debe persistir la información que proporcionan ambos microservicios en una base de datos
no relacional de MongoDB con la siguiente estructura:
- genre
    - movies
        - id
        - name
        - genre
        - urlStream
    - series
        - id
        - name
        - genre
        - seasons
            - id
            - seasonNumber
            - chapters
                - id
                - name
                - number
                - urlStream

## Consigna

## serie-service
* Crear microservicio serie. ✅

    [ver directorio](/serie-service/)

* Configurar Eureka para el nuevo servicio y utilizar el nombre: serie-service. ✅

    > ### pom.xml
    > Microsiervicio : serie-service
    > Previsualización de dependencias:
    >
    >     <dependency>
    >       <groupId>org.springframework.cloud</groupId>
    >       <artifactId> spring-cloud-starter-netflix-eureka-client</artifactId>
    >     </dependency>

    [ir a archivo .pom](/serie-service/pom.xml)

    > ### bootstrap.yml
    > Microservicio: serie-service 
    >
    > Configuraciónes iniciales:
    >
    >     spring:
    >        application:
    >           name: serie-service
    >     eureka:
    >        client:
    >            service-url:
    >               defaultZone: http://${eureka-service:localhost}:8761/eureka

    [ir a archivo bootstrap.yml](/serie-service/src/main/resources/bootstrap.yml)

* Configurar el ruteo en el gateway para el nuevo servicio y agregar seguridad con OAuth. ☑️

    > ### api-gateway-dev.yml
    > Microservicio: api-gateway
    >
    > Configuraciónes de rutas:
    >
    >     routes:
    >       - id: serieRoute
    >       uri: lb://serie-service
    >       predicates:
    >           - Path=/series/**
   
    [ir a archivo api-gateway-dev.yml](/api-gateway-dev.yml)

* Configurar Server config para obtener la configuración desde un repositorio Git. ✅

    > ### bootstrap.yml
    > Microservicio: serie-service 
    >
    > Configuraciónes iniciales:
    >
    >     cloud:
    >       config:
    >           discovery:
    >               enabled: true
    >               service-id: ${config-service:localhost}

    [ir a archivo bootstrap.yml](/serie-service/src/main/resources/bootstrap.yml)

* Crear API que nos permita:
    * Obtener un listado de series por género. Endpoint: /series/{genre} [GET] ✅

        > ### SerieController.java
        > Microservicio: serie-service 
        >
        > Previsualización del codigo:
        >
        >     @GetMapping("/{genre}")
        >     ResponseEntity<List<Serie>> getSerieByGenre(@PathVariable String genre){
        >       return ResponseEntity.ok().body(service.findByGenre(genre));
        >     }

    * Agregar una nueva serie. Endpoint: /series [POST]✅

        > ### SerieController.java
        > Microservicio: serie-service 
        >
        > Previsualización del codigo:
        >
        >     @PostMapping
        >     public ResponseEntity<?> saveSeries(@RequestBody Serie serie) {
        >       service.saveSerie(serie);
        >       return ResponseEntity.ok("La serie fue guardada correctamente");
        >     }

    [ir a archivo SerieController.java](/serie-service/src/main//java/com/digitalhouse/serieservice/controller/SerieController.java)


* Persistencia: agregar la dependencia e implementar MongoRepository para persistir las series. ✅

    > ### pom.xml
    > Microservicio: serie-service 
    >
    > Previsualización de dependencias:
    >
    >     <dependency>
	>	    <groupId>org.springframework.boot</groupId>
	>		<artifactId>spring-boot-starter-data-mongodb</artifactId>
	>	  </dependency>

    [ir a archivo .pom](/serie-service/pom.xml)

    > ### serie-service-dev.yml
    > Microservicio: serie-service 
    >
    > Previsualización de configuraciones:
    >
    >     spring:
    >       data:
    >       mongodb:
    >           authentication-database: admin
    >           username: rootuser
    >           password: rootpass
    >           database: series
    >           port: 27017
    >           host: mongodb

    [ir a archivo serie-service-dev.yml](/serie-service-dev.yml)


    > ### Serie.java
    > Microservicio: serie-service 
    >
    > Previsualización de codigo:
    >
    >     @Document
    >     public class Serie {
    >       @Id
    >       private Long id;
    >       private String name;
    >       private String genre;
    >       private List<Season> seasons;

    [ir a archivo Serie.java](/serie-service/src/main/java/com/digitalhouse/serieservice/models/Serie.java)

    > ### SerieRepository.java
    > Microservicio: serie-service 
    >
    > Previsualización de codigo:
    >
    >     @Repository
    >     public interface SerieRepository extends MongoRepository<Serie, String> {
    >       List<Serie> findByGenre(String genre);
    >     }

    [ir a archivo SerieRepository.java](/serie-service/src/main/java/com/digitalhouse/serieservice/repository/SerieRepository.java)


* Agregar RabbitMQ y enviar un mensaje en el momento que se agregue una nueva serie. ✅

    > ### pom.xml
    > Microservicio: serie-service 
    >
    > Previsualización de codigo:
    >
    >     <dependency>
    >        <groupId>org.springframework.boot</groupId>
    >        <artifactId>spring-boot-starter-amqp</artifactId>
    >     </dependency>
    >     <dependency>
    >        <groupId>org.springframework.amqp</groupId>
    >        <artifactId>spring-rabbit-test</artifactId>
    >        <scope>test</scope>
    >     </dependency>

    [ir a archivo pom.xml](/serie-service/pom.xml)

    > ### bootstrap.yml
    > Microservicio: serie-service
    >
    > Previsualización de configuración:
    >
    >     spring:
    >       rabbitmq:
    >           username: guest
    >           password: guest
    >           host: ${rabbitmq:localhost}
    >           port: 5672
    >       profiles:
    >           active: dev
    >     
    >     queue:
    >       serie:
    >           name: Serie
  

    [ir a archivo bootstrap.yml](/serie-service/src/main/resources/bootstrap.yml)

    //Observación : estas configuracion podrían ir en serie-service-dev.yml así ya se estaría tomando del config-server.

    ### /config

    > ### RabbitMQSenderConfig.java
    > Microservicio: serie-service
    >
    > Previsualización de configuración:
    >
    >     @Configuration
    >     public class RabbitMQSenderConfig {
    >       @Value("${queue.serie.name}")
    >       private String serieQueue;
    >       @Bean
    >       public Queue queue() {
    >           return new Queue(this.serieQueue, true);
    >       }
    >     }

    [ir a archivo RabbitMQSenderConfig.java](/serie-service/src/main/java/com/digitalhouse/serieservice/config/RabbitMQSenderConfig.java)

    > ### RabbitTemplateConfig.java
    > Microservicio: serie-service
    >
    > Previsualización de configuración:
    >
    >     @Configuration
    >     public class RabbitTemplateConfig {
    >       @Bean
    >       public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
    >           return new Jackson2JsonMessageConverter();
    >       }
    >       @Bean
    >       public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
    >           RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
    >           rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
    >           return rabbitTemplate;
    >       }
    >     }

    [ir a archivo RabbitTemplateConfig.java](/serie-service/src/main/java/com/digitalhouse/serieservice/config/RabbitTemplateConfig.java)

    > ### SerieService.java
    > Microservicio: serie-service
    >
    > Previsualización de codigo:
    >
    >       public void saveSerie(Serie serie) {
    >        repository.save(serie);
    >        rabbitTemplate.convertAndSend(serieQueue, serie);
    >       }

    [ir a archivo SerieService.java](/serie-service/src/main/java/com/digitalhouse/serieservice/service/SerieService.java)

## movie-service

* Agregar persistencia: utilizar MySQL para persistir las películas. ✅

    > ### pom.xml
    > Microservicio: movie-service
    >
    > Previsualización de dependencia:
    >
    >     <dependency>
    >       <groupId>mysql</groupId>
    >       <artifactId>mysql-connector-java</artifactId>
    >     </dependency>

    [ir a archivo pom.xml](/movie-service/pom.xml)


    > ### movie-service-dev.yml
    >
    > Microservicio: movie-service
    >
    > Previsualización de configuraciones:
    >
    >     spring:
    >       datasource:
    >           url: jdbc:mysql://mysql:3306/moviedb
    >           username: user
    >           password: pass
    >           driver-class-name: com.mysql.cj.jdbc.Driver

    [ir a archivo movie-service-dev.yml](/movie-service-dev.yml)


    > ### Movie.java
    > Microservicio: movie-service 
    >
    > Previsualización de codigo:
    >
    >     @Entity
    >     public class Movie {
    >       @Id
    >       @GeneratedValue(strategy = GenerationType.IDENTITY)
    >       private Long id;
    >       private String name;
    >       private String genre;
    >       private String urlStream;

    [ir a archivo Movie.java](/movie-service/src/main/java/com/digitalhouse/movieservice/models/Movie.java)

    > ### MovieRepository.java
    > Microservicio: movie-service 
    >
    > Previsualización de codigo:
    >
    >     @Repository
    >     public interface MovieRepository extends JpaRepository<Movie, Long>{
    >       List<Movie> findByGenre(String genre);
    >     }

    [ir a archivo MovieRepository.java](/movie-service/src/main/java/com/digitalhouse/movieservice/repository/MovieRepository.java)

* Configurar el ruteo en el gateway para agregar seguridad con OAuth.  ☑️

    > ### api-gateway-dev.yml
    > Microservicio: api-gateway
    >
    > Configuraciónes de rutas:
    >
    >     routes:
    >       - id: movieRoute
    >       uri: lb://movie-service
    >       predicates:
    >           - Path=/movies/**
   
    [ir a archivo api-gateway-dev.yml](/api-gateway-dev.yml)

* Agregar RabbitMQ y enviar un mensaje en el momento que se agregue una nueva película.

    > ### RabbitMQSenderConfig.java
    > Microservicio: movie-service
    >
    > Previsualización de codigo:
    >
    >     @Configuration
    >     public class RabbitMQSenderConfig {
    >       @Value("${queue.movie.name}")
    >       private String movieQueue;
    >       @Bean
    >       public Queue queueMovie() {
    >           return new Queue(this.movieQueue, true);
    >       }
    >     }
   
    [ir a archivo RabbitMQSenderConfig.java](/movie-service/src/main/java/com/digitalhouse/movieservice/config/RabbitMQSenderConfig.java)

    > ### MovieService.java
    > Microservicio: movie-service
    >
    > Previsualización de codigo:
    >
    >     public void saveMovie(Movie movie) {
    >       repository.save(movie);
    >       rabbitTemplate.convertAndSend(movieQueue, movie);
    >     }
   
    [ir a archivo MovieService.java](/movie-service/src/main/java/com/digitalhouse/movieservice/service/MovieService.java)

## catalog-service

* Actualizar el catálogo utilizando Feign de manera de agregar a este servicio la búsqueda de las series por género (serie-service) y agregarlas a la respuesta del endpoint /catalog/{genre}.

    > ### SerieClient.java
    > Microservicio: catalog-service
    >
    > Previsualización del codigo:
    >
    >     @FeignClient(name = "serie-service")
    >     public interface SerieClient {
    >       @GetMapping("/series/{genre}")
    >       ResponseEntity<List<Serie>> getSerieByGenre(@PathVariable String genre);
   
    [ir a archivo SerieClient.java](/catalog-service/src/main/java/com/digitalhouse/catalogservice/client/SerieClient.java)

    > ### SerieService.java
    > Microservicio: catalog-service
    >
    > Previsualización del codigo:
    >
    >     public ResponseEntity<List<Serie>> findSerieByGenre(String genre) {
    >       LOG.info("Se va a incluir el llamado al serie-service..");
    >       return serieClient.getSerieByGenre(genre);
    >      }

    [ir a archivo SerieService.java](/catalog-service/src/main/java/com/digitalhouse/catalogservice/service/SerieService.java)

    > ### CatalogController.java
    > Microservicio: catalog-service
    >
    > Previsualización del codigo:
    >
    >     @GetMapping("/{genre}")
    >     ResponseEntity<Catalog> getCatalog(@PathVariable String genre){
    >       Catalog catalogo = new Catalog();
    >       catalogo.setGenre(genre);
    >       catalogo.setSeries(serieService.findSerieByGenre(genre));
    >       catalogo.setMovies(movieService.findMovieByGenre(genre));
    >       return ResponseEntity.ok().body(catalogo);
    >      }

    [ir a archivo SerieService.java](/catalog-service/src/main/java/com/digitalhouse/catalogservice/service/SerieService.java)

* Agregar persistencia: luego de obtener las películas y las series según género, persistir las mismas en MongoDB.

    > ### SerieService.java
    > Microservicio: catalog-service
    >
    > Previsualización del codigo:
    >
    >     public List<Serie> findSerieByGenre(String genre) {
    >       List<Serie> series = serieClient.getSerieByGenre(genre).getBody();
    >       Catalog catalog = repository.findByGenre(genre);
    >       catalog.setSeries(series);
    >       repository.save(catalog);
    >       return series;
    >     }

    [ir a archivo SerieService.java](/catalog-service/src/main/java/com/digitalhouse/catalogservice/service/SerieService.java)

    > ### MovieService.java
    > Microservicio: catalog-service
    >
    > Previsualización del codigo:
    >
    >     public List<Movie> findMovieByGenre(String genre) {
    >       List<Movie> movies = movieClient.getMovieByGenre(genre).getBody();
    >       Catalog catalogo = repository.findByGenre(genre);
    >       catalogo.setMovies(movies);
    >       repository.save(catalogo);
    >       return movies;
    >     }

    [ir a archivo MovieService.java](/catalog-service/src/main/java/com/digitalhouse/catalogservice/service/MovieService.java)

* Agregar RabbitMQ y escuchar los mensajes que envían movie-service y serie-service. En caso de recibir un mensaje de algún servicio, actualizar el listado correspondiente, ya sea las películas o las series.

    > ### RabbitMQSenderConfig.java
    > Microservicio: catalog-service
    >
    > Previsualización de codigo:
    >
    >     @Configuration
    >     public class RabbitMQSenderConfig {
    >       @Value("${queue.movie.name}")
    >       private String movieQueue;
    >       @Bean
    >       public Queue queueMovie() {
    >           return new Queue(this.movieQueue, true);
    >       }
    >       @Value("${queue.serie.name}")
    >       private String serieQueue;
    >       @Bean
    >       public Queue queueSerie() {
    >           return new Queue(this.serieQueue, true);
    >       }
    >     }

    [ir a archivo RabbitMQSenderConfig.java](/catalog-service/src/main/java/com/digitalhouse/catalogservice/config/RabbitMQSenderConfig.java)

    > ### SerieService.java
    > Microservicio: catalog-service
    >
    > Previsualización del codigo:
    >
    >     @RabbitListener(queues = {"${queue.serie.name}"})
    >       public void saveSerie(Serie serie){
    >       Catalog catalog = repository.findByGenre(serie.getGenre());
    >       List<Serie> series = catalog.getSeries();
    >       series.add(serie);
    >       catalog.setSeries(series);
    >       repository.save(catalog);
    >     }

    [ir a archivo SerieService.java](/catalog-service/src/main/java/com/digitalhouse/catalogservice/service/SerieService.java)


    > ### MovieService.java
    > Microservicio: catalog-service
    >
    > Previsualización del codigo:
    >
    >     @RabbitListener(queues = {"${queue.movie.name}"})
    >     public void saveMovie(Movie movie){
    >       Catalog catalogo = repository.findByGenre(movie.getGenre());
    >       List<Movie> movies = catalogo.getMovies();
    >       movies.add(movie);
    >       catalogo.setMovies(movies);
    >       repository.save(catalogo);
    >     }

    [ir a archivo MovieService.java](/catalog-service/src/main/java/com/digitalhouse/catalogservice/service/MovieService.java)


## Spring Cloud: traceo utilizando Zipkin

* Crear proyecto y configurar Zipkin server para recibir los mensajes de los microservicios. Agregar Zipkin UI para visualizar las trazas.



* Configurar Zipkin en cada microservicio.

    > ### pom.xml
    > Microservicio:
    >   - catalog-service
    >   - movie-service
    >   - serie-service
    >
    > Previsualizar dependencias:
    >
    >     <dependency>
    >       <groupId>org.springframework.cloud</groupId>
    >       <artifactId>spring-cloud-starter-sleuth</artifactId>
    >     </dependency>
    >     <dependency>
    >        <groupId>org.springframework.cloud</groupId>
    >        <artifactId>spring-cloud-sleuth-zipkin</artifactId>
    >     </dependency>


    > ## application.yml
    >
    > Microservicio:
    >   - catalog-service
    >   - movie-service
    >   - serie-service  
    > 
    > Previsualización de configuración:
    >
    >     spring:
    >       zipkin:
    >           enabled: true
    >           baseUrl: http://${zipkin:localhost}:9411


* Visualizar las comunicaciones entre los microservicios desde la interfaz que nos da
Zipkin UI.

* Deployment: todos los microservicios deberán deployarse en dockers.

    [ir a archivo docker-compose](/docker-compose.yml)

## Resiliencia - Resilence4J

* Del proyecto anterior, se debe seleccionar uno de los servicios (preferentemente el que
consideres que será más utilizado) y adaptarlo para que el mismo sea tolerante a fallos.

* Para lo anterior deberás:
    * Definir esquema de resiliencia. Por ejemplo: doble redundancia, retry and
fallback, balanceo de carga, tiempos de warm up, reglas de circuito.
    * Modificar el código de tu proyecto —aplicando alguna de las tres tecnologías
mencionadas— para que dentro del servicio seleccionado se aplique el
esquema definido.

* Como mínimo, el servicio deberá contar con:
    * Doble redundancia.
    * Reglas del circuito (se puede crear un servicio que devuelva activo/inactivo en
función de la memoria disponible, uso del procesador, exceptions).
    * Descripción de la solución de redundancia, justificación (un comentario en el
código).

