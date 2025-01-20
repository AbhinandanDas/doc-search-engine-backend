package com.neo.powersearch.search.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import com.neo.powersearch.search.model.Document;
import com.neo.powersearch.search.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentService {
    @Autowired
    private DocumentRepository documentRepository;

    public void saveDocument(String docId, String content, String fileName, String fileType) {
        Document document = new Document();
        document.setDocId(docId);
        document.setContent(content);
        document.setFileName(fileName);
        document.setFileType(fileType);
        document.setUploadDate(new Timestamp(System.currentTimeMillis()));
        documentRepository.save(document);
    }

    public Optional<Document> getDocumentById(String docId) {
        return documentRepository.findByDocId(docId);
    }

    public Optional<List<Document>> findAllDocuments() {
        return Optional.of(documentRepository.findAll());
    }
}
