package com.christianoette.demo.hibernatesearch.search;

import com.christianoette.demo.hibernatesearch.model.db.Movie;

public record ExplanationDto(Movie movie, String explanation) {
}
