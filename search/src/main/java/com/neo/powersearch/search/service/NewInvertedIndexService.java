package com.neo.powersearch.search.service;

import com.neo.powersearch.search.index.InvertedIndexing;
import com.neo.powersearch.search.index.impl.InvertedIndex;
import com.neo.powersearch.search.model.Document;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class NewInvertedIndexService {
    @Autowired
    private DocumentService documentService;
    @Autowired
    private StemmingService stemmingService;
    @Autowired
    @Qualifier("RankedInvertedIndex")
    private InvertedIndexing invertedIndex;

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
                    this.invertedIndex.addWord(stemmedWord, docId);
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
            int n = words.length;

            for(int i = 0; i < n; ++i) {
                String word = words[i];
                String stemmedWord = this.stemmingService.stem(word);
                this.invertedIndex.addWord(stemmedWord, docId);
            }
        }

    }
}
