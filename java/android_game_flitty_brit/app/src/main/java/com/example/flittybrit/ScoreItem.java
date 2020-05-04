package com.example.flittybrit;

public class ScoreItem {
    private String displayName;
    private long score;

    public ScoreItem() {
    }

    public ScoreItem(String displayName, long score) {
        this.displayName = displayName;
        this.score = score;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public long getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
