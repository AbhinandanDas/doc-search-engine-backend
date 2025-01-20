package com.neo.powersearch.search.service;

import java.util.Set;

public interface SynonymService {
    Set<String> getSynonyms(String word);
}
