package com.neo.powersearch.search.service;

import com.neo.powersearch.search.index.InvertedIndex;
import com.neo.powersearch.search.model.Document;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class NewInvertedIndexService {
    @Autowired
    private DocumentService documentService;
    @Autowired
    private StemmingService stemmingService;
    @Autowired
    public InvertedIndex invertedIndex;

    @PostConstruct
    public void buildIndex() {
        Optional<List<Document>> documents = this.documentService.findAllDocuments();
        documents.ifPresent((docs) -> {

            for (Document doc : docs) {
                String content = doc.getContent();
                String docId = doc.getDocId();
                String[] words = content.toLowerCase().split("\\W+");
                String[] var7 = words;
                int var8 = words.length;

                for (int var9 = 0; var9 < var8; ++var9) {
                    String word = var7[var9];
                    String stemmedWord = this.stemmingService.stem(word);
                    System.out.println("Building index: " + word + " = " + stemmedWord);
                    this.invertedIndex.add(stemmedWord, docId);
                }
            }

        });
    }

    public void indexDocument(String docId) {
        Optional<Document> fetchDocument = this.documentService.getDocumentById(docId);
        if (fetchDocument.isPresent()) {
            Document document = (Document)fetchDocument.get();
            String content = document.getContent();
            String[] words = content.toLowerCase().split("\\W+");
            String[] var6 = words;
            int var7 = words.length;

            for(int var8 = 0; var8 < var7; ++var8) {
                String word = var6[var8];
                String stemmedWord = this.stemmingService.stem(word);
                this.invertedIndex.add(stemmedWord, docId);
            }
        }

    }
}
