package com.christianoette.demo.hibernatesearch.search;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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
public class IndexBuilder {

    @PersistenceContext
    private EntityManager entityManager;

    @EventListener(ApplicationStartedEvent.class)
    @SneakyThrows
    @Transactional
    public void rebuildIndex() {
        log.info("Rebuild index");
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        fullTextEntityManager.createIndexer().startAndWait();
        log.info("Rebuilding index finished");
    }
}
