package com.neo.powersearch.search.service;

import java.util.List;

import com.neo.powersearch.search.index.InvertedIndex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SuggestionService {
    @Autowired
    private InvertedIndex invertedIndex;

    public SuggestionService() {
    }

    public List<String> getSuggestions(String term) {
        List<String> suggestions = this.invertedIndex.fetchSuggestions(term);
        return suggestions;
    }
}