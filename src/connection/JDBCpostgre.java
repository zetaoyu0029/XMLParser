package connection;

import model.Article;
import model.Authorship;
import model.Inproceedings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class JDBCpostgre {
    private final String url = "jdbc:postgresql://localhost:5432/dblp";
    private final String user = "dblpuser";
    private final String password = "19970617";

    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
//            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }

    /**
     * insert multiple actors
     */
    public void insertArticle(List<Article> list) {
        String SQL = "INSERT INTO \"Article\"(pubkey,title,journal,year) "
                + "VALUES(?,?,?,?)";
        try (
                Connection conn = connect();
                PreparedStatement statement = conn.prepareStatement(SQL);) {
            int count = 0;

            for (Article art : list) {
                statement.setString(1, art.getKey());
                statement.setString(2, art.getTitle());
                statement.setString(3, art.getJournal());
                statement.setInt(4, art.getYear());

                statement.addBatch();
                count++;
                // execute every 100 rows or less
                if (count % 100 == 0 || count == list.size()) {
                    statement.executeBatch();
                }
            }
//            System.out.println("\nSuccessfully storing articles\n");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public void insertInp(List<Inproceedings> list) {
        String SQL = "INSERT INTO \"Inproceedings\"(pubkey,title,booktitle,year) "
                + "VALUES(?,?,?,?)";
        try (
                Connection conn = connect();
                PreparedStatement statement = conn.prepareStatement(SQL);) {
            int count = 0;

            for (Inproceedings art : list) {
                statement.setString(1, art.getKey());
                statement.setString(2, art.getTitle());
                statement.setString(3, art.getBooktitle());
                statement.setInt(4, art.getYear());

                statement.addBatch();
                count++;
                // execute every 100 rows or less
                if (count % 100 == 0 || count == list.size()) {
                    statement.executeBatch();
                }
            }
//            System.out.println("\nSuccessfully storing improceedings\n");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public void insertAuth(List<Authorship> list) {
        String SQL = "INSERT INTO \"Authorship\"(pubkey,author) "
                + "VALUES(?,?)" + "ON CONFLICT (pubkey,author) DO NOTHING";
        try (
                Connection conn = connect();
                PreparedStatement statement = conn.prepareStatement(SQL);) {
            int count = 0;

            for (Authorship art : list) {
                statement.setString(1, art.getKey());
                statement.setString(2, art.getAuthor());

                statement.addBatch();
                count++;
                // execute every 100 rows or less
                if (count % 100 == 0 || count == list.size()) {
                    statement.executeBatch();
                }
            }
//            System.out.println("\nSuccessfully storing authorships\n");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
