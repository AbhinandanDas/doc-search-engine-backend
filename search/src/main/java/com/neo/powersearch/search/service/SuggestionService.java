package com.neo.powersearch.search.service;

import java.util.List;

import com.neo.powersearch.search.index.InvertedIndexing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class SuggestionService {
    @Autowired
    @Qualifier("RankedInvertedIndex")
    private InvertedIndexing invertedIndex;

    public SuggestionService() {
    }

    public List<String> getSuggestions(String term) {
        return this.invertedIndex.fetchSuggestions(term);
    }
}