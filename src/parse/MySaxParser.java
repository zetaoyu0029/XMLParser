package parse;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import connection.JDBCpostgre;
import model.Article;
import model.Authorship;
import model.Inproceedings;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MySaxParser extends DefaultHandler{
    // List to hold Article, Authorship and Inproceedings object
    private List<Article> artList = null;
    private Article article = null;
    private List<Authorship> authList = null;
    private Authorship authorship = null;
    private List<Inproceedings> inpList = null;
    private Inproceedings inproceedings = null;

    private StringBuilder data = null;
    private List<String> authors = null;

    // getter method for three lists
    public List<Article> getArtList() {
        return artList;
    }
    public List<Authorship> getAuthList() { return authList; }
    public List<Inproceedings> getInpList() {
        return inpList;
    }

    boolean btitle = false;
    boolean bjournal = false;
    boolean byear = false;
    boolean bbooktitle = false;
    boolean bauth = false;

    boolean isArt = false;
    boolean isInp = false;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
        if (!isArt && !isInp) {
            // first meet art or inp attr
            if (qName.equalsIgnoreCase("Article")){
                // create an instance of Article
                String key = attributes.getValue("key");
                // initialize Article object and set key attribute
                article = new Article();
                article.setKey(key);
                // initialize Authorship object and set key attribute
                authorship = new Authorship();
                authorship.setKey(key);
                // set flag to be true
                isArt = true;
            } else if (qName.equalsIgnoreCase("inproceedings")){
                // create an instance of Inproceedings
                String key = attributes.getValue("key");
                // initialize Inp object and set key attribute
                inproceedings = new Inproceedings();
                inproceedings.setKey(key);
                // initialize Authorship object and set key attribute
                authorship = new Authorship();
                authorship.setKey(key);
                // set flag to be true
                isInp = true;
            }
            // initialize list
            if (inpList == null)
                inpList = new ArrayList<>();
            if (artList == null)
                artList = new ArrayList<>();
            if (authList == null)
                authList = new ArrayList<>();
            if (authors == null)
                authors = new ArrayList<>();
        } else if (isArt) {
            // already enter into an Article
            if (qName.equalsIgnoreCase("title")) {
                btitle = true;
            } else if (qName.equalsIgnoreCase("journal")) {
                bjournal = true;
            } else if (qName.equalsIgnoreCase("year")){
                byear = true;
            } else if (qName.equalsIgnoreCase("author")){
                bauth = true;
            }
        } else if (isInp) {
            // already enter into an Inp
            if (qName.equalsIgnoreCase("title")) {
                btitle = true;
            } else if (qName.equalsIgnoreCase("booktitle")) {
                bbooktitle = true;
            } else if (qName.equalsIgnoreCase("year")){
                byear = true;
            } else if (qName.equalsIgnoreCase("author")){
                bauth = true;
            }
        }
        data = new StringBuilder();

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException
    {
        if (isArt) {
            if (btitle) {
                // age element, set Employee age
                article.setTitle(data.toString());
                btitle = false;
            } else if (bjournal) {
                article.setJournal(data.toString());
                bjournal = false;
            } else if (byear) {
                article.setYear(Integer.parseInt(data.toString()));
                byear = false;
            } else if (bauth) {
                authors.add(data.toString());
//                authorship.setAuthor(data.toString());
                bauth = false;
            }

        } else if (isInp) {
            if (btitle) {
                // age element, set Employee age
                inproceedings.setTitle(data.toString());
                btitle = false;
            } else if (bbooktitle) {
                inproceedings.setBooktitle(data.toString());
                bbooktitle = false;
            } else if (byear) {
                inproceedings.setYear(Integer.parseInt(data.toString()));
                byear = false;
            } else if (bauth) {
                authors.add(data.toString());
//                authorship.setAuthor(data.toString());
                bauth = false;
            }
        }

        if (qName.equalsIgnoreCase("Article")) {
            // add authorship to list
            storeAuthor();
            // add article to list and set the flag to be false
            artList.add(article);
            if (artList.size() > 5000 || artList.size() == 5000) {
                // connect database and send data whenever over 5000
                send(artList);
            }
            isArt = false;
        } else if (qName.equalsIgnoreCase("inproceedings")) {
            // add authorship to list
            storeAuthor();
            // add the object to list and set the flag to be false
            inpList.add(inproceedings);
            if (inpList.size() > 5000 || inpList.size() == 5000) {
                // connect database and send data whenever over 5000
                send(inpList);
            }
            isInp = false;
        }
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        data.append(new String(ch, start, length));
    }

    public void storeAuthor()
    {
        String currentKey = authorship.getKey();
        for (String author: authors){
            authorship = new Authorship();
            authorship.setKey(currentKey);
            authorship.setAuthor(author);
            authList.add(authorship);
        }

        if (authList.size() > 5000 || authList.size() == 5000) {
            // connect database and send data whenever over 5000
            send(authList);
        }
        // clear temp authors
        authors.clear();
        authorship = new Authorship();
    }

    // send data to database
    public void send(List list)
    {
        JDBCpostgre post = new JDBCpostgre();
        if (list.get(0) instanceof Article) {
            post.insertArticle(artList);
            artList.clear();
        }else if (list.get(0) instanceof Inproceedings){
            post.insertInp(inpList);
            inpList.clear();
        }else if (list.get(0) instanceof Authorship){
            post.insertAuth(authList);
            authList.clear();
        }
    }
}
