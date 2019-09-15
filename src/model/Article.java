package model;

public class Article {
    private String pubkey;
    private String title;
    private String journal;
    private int year;

    // getter
    public String getKey() { return pubkey; }

    public String getTitle() { return title; }

    public String getJournal() { return journal; }

    public int getYear() { return year; }

    // setter
    public void setKey(String k) { this.pubkey = k; }

    public void setTitle(String t) { this.title = t; }

    public void setJournal(String j) { this.journal = j; }

    public void setYear(int y) { this.year = y; }

    // print
    public String toString() {
        return "model.Article: Key="+this.pubkey+" Title=" + this.title + " Journal=" + this.journal + " year=" + this.year;
    }
}
