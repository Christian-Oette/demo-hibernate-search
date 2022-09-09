package com.christianoette.demo.hibernatesearch.model.json;

import com.christianoette.demo.hibernatesearch.model.db.Movie;
import com.christianoette.demo.hibernatesearch.model.db.MovieRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;


@Service
@RequiredArgsConstructor
@Slf4j
public class DemoDataCreator {

    public static final String DEMO_DATA_FILE = "top-rated-movies.json";

    private final MovieRepository movieRepository;
    private ObjectMapper mapper;

    @EventListener(ApplicationReadyEvent.class)
    @SneakyThrows
    public void initDemoData() {
        initMapper();
        String movieListJsonData = readFileToString(DEMO_DATA_FILE);
        MovieItems movieItems = mapper.readValue(movieListJsonData, MovieItems.class);
        movieItems.getMovies()
                .stream()
                .filter(m -> StringUtils.isNotEmpty(m.getOriginalTitle()))
                .forEach(this::storeMovieInDatabase);
    }

    private void storeMovieInDatabase(MovieItem movieItem) {
        Movie movie = new Movie();
        movie.setTitle(movieItem.getOriginalTitle());
        movie.setStoryLine(movieItem.getStoryline());
        Movie savedMovie = movieRepository.save(movie);
        log.info("Save demo data {}", savedMovie);
    }

    private void initMapper() {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @SneakyThrows
    private String readFileToString(String fileName) {
        URL resource = Objects.requireNonNull(getClass().getResource(fileName), "Resource " + fileName + " not found");
        Path path = Paths.get(resource.toURI());
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
