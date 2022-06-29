package com.christianoette.demo.hibernatesearch.controller;

import com.christianoette.demo.hibernatesearch.model.db.Movie;
import com.christianoette.demo.hibernatesearch.search.MovieSearch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

@SuppressWarnings("ClassCanBeRecord")
@RestController
@RequiredArgsConstructor
@Slf4j
@EnableSwagger2
public class ApiController {

    private final MovieSearch movieSearch;

    @GetMapping(value = "/")
    public RedirectView redirectToSwagger() {
        return new RedirectView("/swagger-ui/index.html");
    }

    @GetMapping(value = "/api/search")
    public List<Movie> search(@RequestParam String searchTerm) {
        return movieSearch.search(searchTerm);
    }

}
