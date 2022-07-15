package com.christianoette.demo.hibernatesearch.search;

import com.christianoette.demo.hibernatesearch.model.db.Movie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.Query;
import org.hibernate.search.exception.EmptyQueryException;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MovieSearch {

    private final SearchUtils searchUtils;

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<Movie> search(String searchTerm) {
        boolean isSearchByStoryLinePossible = searchUtils.iSearchPossibleOrAlreadyFilteredByAnalyzer(Movie.class, Movie.Fields.storyLine, searchTerm);
        boolean isSearchByStoryTitlePossible = searchUtils.iSearchPossibleOrAlreadyFilteredByAnalyzer(Movie.class, Movie.Fields.title, searchTerm);

        // Check avoids EmptyQueryException
        if (isSearchByStoryLinePossible || isSearchByStoryTitlePossible) {
            FullTextQuery fullTextQuery = createQuery(searchTerm);
            return fullTextQuery.getResultList();
        } else {
            return List.of();
        }
    }

    public String explain(String searchTerm) {
        StringBuilder explanations = new StringBuilder();
        FullTextEntityManager fullTextEntityManager = searchUtils.getFullTextEntityManager();
        QueryBuilder queryBuilder = searchUtils.getQueryBuilder(fullTextEntityManager, Movie.class);

        FullTextQuery fullTextQuery = createQuery(searchTerm).setProjection(
                        FullTextQuery.EXPLANATION,
                        FullTextQuery.DOCUMENT_ID,
                        FullTextQuery.THIS );

        List<Object[]> results = fullTextQuery.getResultList();
        for (Object[] result : results) {
            String title = ((Movie) result[2]).getTitle();
            String explanation = ((Explanation) result[0]).toString();
            explanations.append( title).append("\n").append(explanation).append("\n\n");
        }
        return explanations.toString();
    }

    private FullTextQuery createQuery(String searchTerm) {
        FullTextEntityManager fullTextEntityManager = searchUtils.getFullTextEntityManager();
        QueryBuilder queryBuilder = searchUtils.getQueryBuilder(fullTextEntityManager, Movie.class);
        Query keywordQuery = queryBuilder
                .keyword()
                .fuzzy()
                .withEditDistanceUpTo(1)
                .onField(Movie.Fields.storyLine)
                .andField(Movie.Fields.title)
                .matching(searchTerm)
                .createQuery();
        return fullTextEntityManager.createFullTextQuery(keywordQuery, Movie.class);
    }
}
