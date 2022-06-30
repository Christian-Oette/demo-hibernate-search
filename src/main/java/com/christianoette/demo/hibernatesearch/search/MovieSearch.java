package com.christianoette.demo.hibernatesearch.search;

import com.christianoette.demo.hibernatesearch.model.db.Movie;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class MovieSearch {

    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<Movie> search(String searchTerm) {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        QueryBuilder queryBuilder = getQueryBuilder(fullTextEntityManager, Movie.class);

        Query keywordQuery = queryBuilder
                .keyword()
                .fuzzy()
                .withEditDistanceUpTo(1)
                .onField(Movie.Fields.storyLine)
                .matching(searchTerm)
                .createQuery();

        return wrapQuery(fullTextEntityManager, keywordQuery).getResultList();
    }

    private FullTextQuery wrapQuery(FullTextEntityManager fullTextEntityManager, Query keywordQuery) {
        return fullTextEntityManager.createFullTextQuery(keywordQuery, Movie.class);
    }

    private QueryBuilder getQueryBuilder(FullTextEntityManager fullTextEntityManager, Class<?> targetClass) {
        return fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder()
                .forEntity(targetClass)
                .get();
    }
}
