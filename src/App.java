import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import connection.JDBCpostgre;
import model.Article;
import model.Authorship;
import model.Inproceedings;
import org.xml.sax.SAXException;
import parse.MySaxParser;


public class App {
    public static void main(String[] args) {
        long StartTime = System.currentTimeMillis();
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            MySaxParser handler = new MySaxParser();
            saxParser.parse(new File("/Users/stevenyu0029/Desktop/dblp-2019-09-05.xml"), handler); //dblp-2019-09-05
            //Get model.Article list
            List<Article> artList = handler.getArtList();
            //Get model.Authorship list
            List<Authorship> authList = handler.getAuthList();
            //Get model.Inproceedings list
            List<Inproceedings> inpList = handler.getInpList();

//            int count = 0;
//            for (Authorship a: authList)
//            {
//                if(a.getAuthor().equals("Wei Wang"))
//                    count++;
//            }
//            System.out.println(count);

            // store the rest of the list to PostGre database
            JDBCpostgre post = new JDBCpostgre();
            if (artList.size() > 0) {post.insertArticle(artList);}
            if (inpList.size() > 0) {post.insertInp(inpList);}
            if (authList.size() > 0) {post.insertAuth(authList);}

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        long EndTime = System.currentTimeMillis();
        long timeElapsed = (EndTime - StartTime)/60000;
        // print out total execution time for the program
        System.out.println("Total run time is "+timeElapsed);
    }

}
