package com.neo.powersearch.search.service.impl;

import com.neo.powersearch.search.index.impl.RankedInvertedIndex;
import com.neo.powersearch.search.service.PageRankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TermFrequencyPageRankingServiceImpl implements PageRankService {
    @Autowired
    private RankedInvertedIndex rankedInvertedIndex;
    @Override
    public Map<String, Double> rankDocuments(List<String> terms) {
        Map<String,Double> documentScores = new HashMap<>();
        int totalDocuments = rankedInvertedIndex.getTotalDocuments();

        for(String term : terms) {
            Map<String,Integer> termDocs = rankedInvertedIndex.searchTerm(term);
            int documentFrequency = rankedInvertedIndex.getDocumentFrequency(term);

            if(documentFrequency > 0) {
                double idf = Math.log((double)totalDocuments / documentFrequency);

                for(Map.Entry<String,Integer> entry : termDocs.entrySet()) {
                    String docId = entry.getKey();
                    int termFrequency = entry.getValue();
                    double tf = (double)termFrequency;

                    documentScores.put(docId, documentScores.getOrDefault(docId,0.0) + (tf * idf));
                }
            }

        }

        return documentScores;
    }
}
