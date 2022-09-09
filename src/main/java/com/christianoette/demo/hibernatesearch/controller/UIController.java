package com.christianoette.demo.hibernatesearch.controller;

import com.christianoette.demo.hibernatesearch.model.db.Movie;
import com.christianoette.demo.hibernatesearch.search.MovieSearch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UIController {

    private final MovieSearch movieSearch;

    @GetMapping(value = "/")
    public ModelAndView showSearchPage(@RequestParam(required = false) String searchTerm) {
        ModelAndView modelAndView = new ModelAndView("index");
        List<Movie> searchResultList = Optional.ofNullable(searchTerm)
                .filter(StringUtils::isNotEmpty)
                .map(movieSearch::search)
                .orElse(new ArrayList<>());
        log.info("Search for {} finished with {} results", searchTerm, searchResultList.size());
        modelAndView.addObject("movies", searchResultList);
        return modelAndView;
    }
}
