import com.ibatis.common.jdbc.ScriptRunner;

import java.io.*;
import java.sql.*;

/**
 * Created by Covaciu on 20/01/2016.
 */

public class DatabaseWords {
    Connection con;
    Statement stmt;
    ResultSet rs;


    public void createConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/entries", "root", "");
            con.setAutoCommit(true);
            stmt = null;
            stmt = con.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createTable() {
        String aSQLScriptFilePath = "G:\\Informatica\\java workspace\\Comparing PDFS\\src\\resources\\dictionarysql.sql";
        try {
            // Create MySql Connection
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/entries", "root", "");
            stmt = null;
            // Initialize object for ScripRunner
            ScriptRunner sr = new ScriptRunner(con, false, false);

            // Give the input file to Reader
            Reader reader = new BufferedReader(
                    new FileReader(aSQLScriptFilePath));

            // Exctute script
            sr.runScript(reader);
            con.close();
        } catch (Exception e) {
            System.err.println("Failed to Execute" + aSQLScriptFilePath
                    + " The error is " + e.getMessage());
        }
    }

    public void displayData() {
        try {
            rs = stmt.executeQuery("SELECT word FROM entries WHERE word='zymoscope'");
            while (rs.next()) {
                System.out.println(rs.getString("word"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean searchFor(String word) {
        try {
            String singular = null;
            rs = stmt.executeQuery("SELECT word, definition FROM entries WHERE substr(word, 1, 1)='" + word.charAt(0) + "';");
            while (rs.next()) {
                if (rs.getString("definition").contains(word))
                    return true;
                if (word.endsWith("s") && word.length() > 2) {
                    singular = word.substring(0, word.length() - 1);
                    if (singular.toLowerCase().equals(rs.getString("word").toLowerCase()))
                        return true;
                }
                if (word.toLowerCase().equals(rs.getString("word").toLowerCase()))
                    return true;
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return false;
    }

}
