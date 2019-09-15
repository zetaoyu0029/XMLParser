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

//            //print all information
//            for(Article a : artList)
//                System.out.println(a);
//            for(Authorship b : authList)
//                System.out.println(b);
//            for(Inproceedings c : inpList)
//                System.out.println(c);
//            int count = 0;
//            for (Authorship a: authList)
//            {
//                if(a.getAuthor().equals("Philip S. Yu"))
//                    count++;
//            }
//            System.out.println(count);
            // store all information to PostGre database
            JDBCpostgre post = new JDBCpostgre();
//            System.out.println("\nArticle count size is: "+artList.size());
//            System.out.println("\nInproceedings count size is: "+inpList.size());
//            System.out.println("\nAuthorship count size is: "+authList.size());
            if (artList.size() > 0) {post.insertArticle(artList);}
            if (inpList.size() > 0) {post.insertInp(inpList);}
            if (authList.size() > 0) {post.insertAuth(authList);}

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        long EndTime = System.currentTimeMillis();
        long timeElapsed = (EndTime - StartTime)/60000;
        System.out.println("Total run time is "+timeElapsed);
    }

    public static List<Authorship> checkRepitition(List<Authorship> list)
    {
//        Set<Authorship> s= new HashSet<Authorship>();
//        s.addAll(list);
//        list = new ArrayList<Authorship>();
//        list.addAll(s);
        LinkedHashSet<Authorship> hashSet = new LinkedHashSet<Authorship>(list);
        List<Authorship> listWithoutDuplicates = new ArrayList<Authorship>(hashSet);
        return listWithoutDuplicates;
    }
}
