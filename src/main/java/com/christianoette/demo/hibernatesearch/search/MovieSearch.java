package com.christianoette.demo.hibernatesearch.search;

import com.christianoette.demo.hibernatesearch.model.db.Movie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.TermContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MovieSearch {

    private final SearchUtils searchUtils;

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<Movie> search(String searchTerm) {
        FullTextEntityManager fullTextEntityManager = searchUtils.getFullTextEntityManager();
        QueryBuilder queryBuilder = searchUtils.getQueryBuilder(fullTextEntityManager, Movie.class);

        Optional<Query> storyLineQuery = createStoryLineQuery(searchTerm, queryBuilder);
        Optional<Query> titleQuery = createTitleQuery(searchTerm, queryBuilder);

        if (storyLineQuery.isEmpty() && titleQuery.isEmpty()) {
            return List.of();
        }

        var keywordQuery = queryBuilder.bool();
        storyLineQuery.ifPresent(keywordQuery::should);
        titleQuery.ifPresent(keywordQuery::should);

        var fullTextQuery = fullTextEntityManager.createFullTextQuery(keywordQuery.createQuery(), Movie.class);
        return fullTextQuery.getResultList();
    }

    protected Optional<Query> createStoryLineQuery(String searchTerm, QueryBuilder queryBuilder) {
        String fieldName = Movie.Fields.storyLine;
        boolean isSearchByStoryLinePossible = searchUtils.iSearchPossibleOrAlreadyFilteredByAnalyzer(Movie.class, fieldName, searchTerm);
        if (StringUtils.isEmpty(searchTerm) || !isSearchByStoryLinePossible) {
            return Optional.empty();
        }
        Query keywordQuery = getKeywordQuery(searchTerm, queryBuilder, fieldName, 1.0f, true);
        return Optional.of(keywordQuery);
    }

    protected Optional<Query> createTitleQuery(String searchTerm, QueryBuilder queryBuilder) {
        String fieldName = Movie.Fields.title;
        boolean isSearchByStoryTitlePossible = searchUtils.iSearchPossibleOrAlreadyFilteredByAnalyzer(Movie.class, fieldName, searchTerm);
        if (StringUtils.isEmpty(searchTerm) || !isSearchByStoryTitlePossible) {
            return Optional.empty();
        }
        Query keywordQuery = getKeywordQuery(searchTerm, queryBuilder, fieldName, 2.0f, false);
        return Optional.of(keywordQuery);
    }


    private Query getKeywordQuery(String searchTerm, QueryBuilder queryBuilder, String fieldName, float boost, boolean addFuzziness) {
        TermContext keyword = queryBuilder .keyword();
        if (addFuzziness) {
            keyword.fuzzy().withEditDistanceUpTo(1);
        }
        return keyword
                .onField(fieldName)
                .boostedTo(boost)
                .matching(searchTerm)
                .createQuery();
    }
}
