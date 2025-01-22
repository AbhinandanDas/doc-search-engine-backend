package com.neo.powersearch.search.index.impl;

import com.neo.powersearch.search.index.InvertedIndexing;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Qualifier("RankedInvertedIndex")
public class RankedInvertedIndex implements InvertedIndexing {
    private final Map<String, Map<String,Integer>> index = new HashMap<>(); // term -> docid -> tf(term frequency).
    private int totalDocuments = 0; // to store the total number of documents.


    @Override
    public void addWord(String content,String docId) {
        totalDocuments++;
        String[] words = content.split("\\W+");
        int totalTerms = words.length;

        Map<String,Integer> termFrequencies = new HashMap<>();

        // Calculate frequency
        for(String word : words) {
            word = word.toLowerCase();
            termFrequencies.put(word, termFrequencies.getOrDefault(word,0)+1);
        }


        // Normalise the term frequency before adding to the hashmap
        for(Map.Entry<String,Integer> entry : termFrequencies.entrySet()) {
            String term = entry.getKey();
            Integer frequency = entry.getValue();

            double tf = (double) frequency / totalTerms;

            index.putIfAbsent(term,new HashMap<>());
            index.get(term).put(docId, (int) tf);
        }
    }

    @Override
    public List<String> fetchSuggestions(String prefix) {
        List<String> suggestions = new ArrayList<>();

        for (String word : this.index.keySet()) {
            if (word.startsWith(prefix)) {
                suggestions.add(word);
            }
        }

        return suggestions;
    }
    @Override
    public Map<String,Integer> searchTerm(String term) {
        return index.getOrDefault(term,new HashMap<>());
    }

    public int getTotalDocuments() {
        return totalDocuments;
    }

    public int getDocumentFrequency(String term) {
        return index.containsKey(term) ? index.get(term).size() : 0;
    }

}
