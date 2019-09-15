package model;

public class Inproceedings {
    private String pubkey;
    private String title;
    private String booktitle;
    private int year;

    // getter
    public String getKey() { return pubkey; }

    public String getTitle() { return title; }

    public String getBooktitle() { return booktitle; }

    public int getYear() { return year; }

    // setter
    public void setKey(String k) { this.pubkey = k; }

    public void setTitle(String t) { this.title = t; }

    public void setBooktitle(String b) { this.booktitle = b; }

    public void setYear(int y) { this.year = y; }

    // print
    public String toString() {
        return "model.Inproceedings: Key="+this.pubkey+" Title=" + this.title + " Booktitle=" + this.booktitle + " year=" + this.year;
    }
}
