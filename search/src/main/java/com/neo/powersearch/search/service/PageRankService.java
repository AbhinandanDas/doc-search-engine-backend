package com.neo.powersearch.search.service;

import java.util.List;
import java.util.Map;

public interface PageRankService {
    Map<String,Double> rankDocuments(List<String> terms);
}
