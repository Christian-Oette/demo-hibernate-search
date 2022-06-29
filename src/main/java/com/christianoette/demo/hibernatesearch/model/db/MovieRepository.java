package com.christianoette.demo.hibernatesearch.model.db;

import org.springframework.data.repository.CrudRepository;

public interface MovieRepository extends CrudRepository<Movie, Long> {
}
