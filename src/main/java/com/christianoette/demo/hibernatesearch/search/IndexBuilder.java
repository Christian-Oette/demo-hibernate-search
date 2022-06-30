package com.christianoette.demo.hibernatesearch.search;

import com.christianoette.demo.hibernatesearch.model.db.Movie;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.CacheMode;
import org.hibernate.search.batchindexing.MassIndexerProgressMonitor;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Service
@Slf4j
public class IndexBuilder implements MassIndexerProgressMonitor{

    @PersistenceContext
    private EntityManager entityManager;

    @EventListener(ApplicationStartedEvent.class)
    @SneakyThrows
    @Transactional
    public void builIndexOnStartup() {
        log.info("Rebuild index");
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        fullTextEntityManager.createIndexer()
                .progressMonitor(this)
                .startAndWait();
        log.info("Rebuilding index finished");
    }

    @SneakyThrows
    @Transactional
    public void reindex(boolean purge) {
        log.info("Trigger reIndex");
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        fullTextEntityManager
                .createIndexer( Movie.class )
                .purgeAllOnStart(purge)
                .batchSizeToLoadObjects( 25 )
                .cacheMode( CacheMode.NORMAL )
                .threadsToLoadObjects( 1 )
                .idFetchSize( 150 )
                .transactionTimeout( 1800 )
                .progressMonitor(this)
                .optimizeAfterPurge(purge)
                .startAndWait();
    }

    @Override
    public void documentsBuilt(int count) {
    }

    @Override
    public void entitiesLoaded(int count) {
        log.info("{} Entities loaded", count);
    }

    @Override
    public void addToTotalCount(long count) {
        log.info("{} Added to total count",count);
    }

    @Override
    public void indexingCompleted() {
        log.info("Indexing completed");
    }

    @Override
    public void documentsAdded(long count) {
    }
}
