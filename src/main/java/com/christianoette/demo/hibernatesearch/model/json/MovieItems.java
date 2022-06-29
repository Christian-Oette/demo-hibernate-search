package com.christianoette.demo.hibernatesearch.model.json;

import lombok.Data;

import java.util.List;

@Data
public class MovieItems {
    List<MovieItem> movies;
}
