package com.neo.powersearch.search.dto;

public class SynonymResponse {
    private String word;
    private double score;
    public String getWord() {
        return word;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
