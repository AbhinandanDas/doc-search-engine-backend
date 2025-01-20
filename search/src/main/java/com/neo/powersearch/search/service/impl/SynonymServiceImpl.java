package com.neo.powersearch.search.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neo.powersearch.search.dto.SynonymResponse;
import com.neo.powersearch.search.service.SynonymService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Set;

@Service
public class SynonymServiceImpl implements SynonymService {

    private static final String API_URL = "https://api.datamuse.com/words?rel_syn=";
    @Override
    public Set<String> getSynonyms(String word) {
        Set<String> synonyms = new HashSet<>();
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(API_URL + word,String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            SynonymResponse[] synonymResponses = objectMapper.readValue(response,SynonymResponse[].class);
            for(SynonymResponse synonymResponse : synonymResponses) {
                synonyms.add(synonymResponse.getWord());
            }
        }
        catch(JsonProcessingException e) {
            System.err.println("Error parsing JSON response: " + e.getMessage());
        }
        return synonyms;
    }
}
