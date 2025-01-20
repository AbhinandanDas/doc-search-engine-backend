package com.neo.powersearch.search.index;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class InvertedIndex {
    private Map<String, Set<String>> index = new HashMap();

    public void add(String word, String docId) {
        word = word.toLowerCase();
        ((Set)this.index.computeIfAbsent(word, (k) -> {
            return new HashSet();
        })).add(docId);
    }

    public Set search(String word) {
        word = word.toLowerCase();
        return (Set)(!this.index.containsKey(word) ? new HashSet() : (Set)this.index.get(word));
    }

    public Set<String> searchByMultipleWords(List<String> words) {
        if (words != null && !words.isEmpty()) {
            Set<String> result = null;
            Iterator var3 = words.iterator();

            while(var3.hasNext()) {
                String word = (String)var3.next();
                Set<String> docsWithWord = this.search(word);
                if (result == null) {
                    result = new HashSet(docsWithWord);
                } else {
                    result.retainAll(docsWithWord);
                }
            }

            if (result.isEmpty()) {
                return Collections.emptySet();
            } else {
                return result;
            }
        } else {
            return Collections.emptySet();
        }
    }

    public List<String> fetchSuggestions(String prefix) {
        List<String> suggestions = new ArrayList<>();

        for (String word : this.index.keySet()) {
            if (word.startsWith(prefix)) {
                suggestions.add(word);
            }
        }

        return suggestions;
    }

    public void printIndex() {

        for (Map.Entry<String, Set<String>> stringSetEntry : this.index.entrySet()) {
            Map.Entry entry = stringSetEntry;
            PrintStream var10000 = System.out;
            String var10001 = (String) entry.getKey();
            var10000.println("The word: " + var10001 + "is present in documents: " + String.valueOf(entry.getValue()));
        }

    }
}