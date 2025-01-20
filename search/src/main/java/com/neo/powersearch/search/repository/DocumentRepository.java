package com.neo.powersearch.search.repository;

import java.util.Optional;

import com.neo.powersearch.search.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    Optional<Document> findByDocId(String docId);
}
