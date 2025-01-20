package com.neo.powersearch.search.service.impl;

import java.io.IOException;
import java.io.StringReader;

import com.neo.powersearch.search.service.StemmingService;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.springframework.stereotype.Service;

@Service
public class StemmingServiceImpl implements StemmingService {

    @Override
    public String stem(String word) {
        try(Analyzer analyzer = new WhitespaceAnalyzer()) {
            // Tokenize the input text(word)
            var tokenStream = analyzer.tokenStream(null,new StringReader(word));
            tokenStream = new PorterStemFilter(tokenStream);
            tokenStream.reset();

            //Extract the stemmed term
            if(tokenStream.incrementToken()) {
                CharTermAttribute attr = tokenStream.getAttribute(CharTermAttribute.class);
                return attr.toString();
            }

            tokenStream.end();
            tokenStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return word;
    }
}
