package com.neo.powersearch.search.index;

import java.util.List;
import java.util.Map;

public interface InvertedIndexing {
    public void addWord(String content,String docId);
    public Map<String,Integer> searchTerm(String term);
    public List<String> fetchSuggestions(String prefix);
}
