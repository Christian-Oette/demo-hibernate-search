package com.christianoette.demo.hibernatesearch.search;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SearchUtils {

    private final EntityManager entityManager;

    public QueryBuilder getQueryBuilder(FullTextEntityManager fullTextEntityManager, Class<?> targetClass) {
        return fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder()
                .forEntity(targetClass)
                .get();
    }

    public FullTextEntityManager getFullTextEntityManager() {
        return Search.getFullTextEntityManager(entityManager);
    }

    /**
     * Hibernate search throws an exception if the search is not possible.
     *
     * Example:
     * Search for "A quite world" <- Should work ("quite" and "world" are usually tokens)
     * Search for "A" <- Should not work (A is filtered and never indexed)
     */
    public boolean iSearchPossibleOrAlreadyFilteredByAnalyzer(Class<?> entity, String fieldName, String searchTerm) {
        Analyzer customAnalyzer = getFullTextEntityManager().getSearchFactory().getAnalyzer(entity);
        return anyTokensLeftAfterAnalyze(customAnalyzer, fieldName, searchTerm );
    }

    /**
     * Validate input against the tokenizer and return a list of terms.
     * Idea from <a href="https://stackoverflow.com/questions/13765698/getting-error-on-a-specific-query">...</a>
     */
    @SneakyThrows(IOException.class)
    private boolean anyTokensLeftAfterAnalyze(Analyzer analyzer, String fieldName, String searchTerm) {
        try(TokenStream stream = analyzer.tokenStream(fieldName, searchTerm)) {
            stream.reset();
            return stream.incrementToken();
        }
    }
}