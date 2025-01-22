package com.neo.powersearch.search.controller;


import com.neo.powersearch.search.index.InvertedIndexing;
import com.neo.powersearch.search.model.Document;
import com.neo.powersearch.search.repository.DocumentRepository;
import com.neo.powersearch.search.service.PageRankService;
import com.neo.powersearch.search.service.StemmingService;
import com.neo.powersearch.search.service.SuggestionService;
import com.neo.powersearch.search.service.SynonymService;
import jakarta.transaction.Transactional;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/search"})
@CrossOrigin(
        origins = {"http://localhost:3000"}
)
@Transactional
public class SearchController {
    @Autowired
    @Qualifier("RankedInvertedIndex")
    private InvertedIndexing invertedIndex;
    @Autowired
    private StemmingService stemmingService;
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private SuggestionService suggestionService;

    @Autowired
    private SynonymService synonymService;

    @Autowired
    private PageRankService pageRankService;

    public SearchController() {
    }

    @GetMapping({"/term"})
    public ResponseEntity<List<String>> searchTerms(@RequestParam String term) {
        List<String> words = List.of(term.split(" "));
        List<String> searchList = new ArrayList<>();
        Iterator var4 = words.iterator();

        while(var4.hasNext()) {
            String word = (String)var4.next();
            String stemmedWord = this.stemmingService.stem(word);
            searchList.add(stemmedWord);
        }

        // append the synonyms to the stemmed words list
        Set<String> synonyms = new HashSet<>(synonymService.getSynonyms(term));

        // Searching the words(stemmed + synonmys)
        Map<String,Double> docsWithRank = this.pageRankService.rankDocuments(searchList);
        for(String synonym : synonyms) {
            docsWithRank.putAll(this.pageRankService.rankDocuments(Collections.singletonList(synonym)));
        }

        // Sort the document by score in descending order
        Map<String,Double> sortedDocs = new TreeMap<>((a,b) -> docsWithRank.get(b).compareTo(docsWithRank.get(a)));

        sortedDocs.putAll(docsWithRank);

//        Set<String> documentIds = this.invertedIndex.searchByMultipleWords(searchList);
//        for(String synonym : synonyms) {
//            documentIds.addAll(this.invertedIndex.searchByMultipleWords(Collections.singletonList(synonym)));
//        }
        List<String> documentNames = new ArrayList<>();
        if (sortedDocs.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            for(String docId : sortedDocs.keySet()) {
                Optional<Document> document = this.documentRepository.findByDocId(docId);
                System.out.println("Current Doc ID: " + docId);
                if(document.isPresent()) {
                    documentNames.add(document.get().getFileName());
                }
            }
            return ResponseEntity.ok(documentNames);
        }
    }
    @GetMapping({"/suggestions"})
    public ResponseEntity<List<String>> getSuggestions(@RequestParam String term) {
        List<String> suggestions = this.suggestionService.getSuggestions(term);
        return ResponseEntity.ok(suggestions);
    }
}
