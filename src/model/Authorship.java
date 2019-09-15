package model;

public class Authorship {
    private String pubkey;
    private String author;

    // getter
    public String getKey() { return pubkey; }

    public String getAuthor() { return author; }

    // setter
    public void setKey(String k) { this.pubkey = k; }

    public void setAuthor(String a) { this.author = a; }

    // print
    public String toString() {
        return "model.Authorship: Key="+this.pubkey+" Author=" + this.author;
    }

    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        if(obj instanceof Authorship)
        {
            Authorship temp = (Authorship) obj;
            if(this.pubkey.equals(temp.pubkey) && this.author.equals(temp.author))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub

        return (this.pubkey.hashCode() + this.author.hashCode());
    }
}
