package controllers;

import org.json.JSONException;
import org.json.JSONObject;
import structure.Branch;
import structure.Company;
import structure.Note;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlController {
    private static String connectionString = "jdbc:sqlserver://localhost\\DESKTOP-DP477LA/tema:55150;database=IQueue;integratedSecurity=true";

    private void closeConnection(Connection con, Statement stmt, ResultSet executeQuery){
        // Закрываем соединение
        try { con.close(); } catch(SQLException | NullPointerException se) { /*can't do anything */ }
        try { stmt.close(); } catch(SQLException | NullPointerException se) { /*can't do anything */ }
        try { executeQuery.close(); } catch(SQLException | NullPointerException se) { /*can't do anything */ }
    }


    public boolean Registration(String login, String password, String name, Boolean sex, String city, Integer age)
    {
        Connection con = null;
        Statement stmt = null;
        ResultSet executeQuery = null;
        try {
            // Подключение к базе данных
            con = DriverManager.getConnection(connectionString);
            // Отправка запроса на выборку и получение результатов
            stmt = con.createStatement();
            String query = String.format("SELECT * FROM [Login] WHERE login = '%s' AND password = '%s'", login, password);
            executeQuery = stmt.executeQuery(query);
            // Обход результатов выборки
            while (executeQuery.next())
                return false;
            query = String.format("INSERT INTO Login(login, password, name, sex, city, age)\n" +
                    "VALUES ('%s', '%s', '%s', '%s', '%s', '%s');", login, password, name, sex, city, age);
            stmt.executeUpdate(query);
            return true;
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
        finally {
            closeConnection(con, stmt, executeQuery);
        }
        return false;
    }

    public boolean AdminRegistration(String login, String password, String name, String logo)
    {
        Connection con = null;
        Statement stmt = null;
        ResultSet executeQuery = null;
        try {
            // Подключение к базе данных
            con = DriverManager.getConnection(connectionString);
            // Отправка запроса на выборку и получение результатов
            stmt = con.createStatement();
            String query = String.format("SELECT * FROM [Company] WHERE login = '%s' AND password = '%s'", login, password);
            executeQuery = stmt.executeQuery(query);
            // Обход результатов выборки
            while (executeQuery.next())
                return false;
            query = String.format("INSERT INTO Company(login, password, name, logo)\n" +
                    "VALUES ('%s', '%s', '%s', '%s');", login, password, name, logo);
            stmt.executeUpdate(query);
            return true;
        } catch (SQLException ex) {
            // Обработка исключений
            System.out.println(ex.getMessage());
            System.out.println(ex.toString());
        }
        finally {
            closeConnection(con, stmt, executeQuery);
        }
        return false;
    }

    public boolean Login(String login, String password)
    {
        Connection con = null;
        Statement stmt = null;
        ResultSet executeQuery = null;
        try {
            // Подключение к базе данных
            con = DriverManager.getConnection(connectionString);
            // Отправка запроса на выборку и получение результатов
            stmt = con.createStatement();
            String query = String.format("SELECT * FROM [Login] WHERE login = '%s' AND password = '%s'", login, password);
            executeQuery = stmt.executeQuery(query);
            // Обход результатов выборки
            while (executeQuery.next())
                return true;
        } catch (SQLException ex) {
            // Обработка исключений
            System.out.println(ex.getMessage());
            System.out.println(ex.toString());
        }
        finally {
            closeConnection(con, stmt, executeQuery);
        }
        return false;
    }

    public JSONObject Profile(String login, String password)
    {
        Connection con = null;
        Statement stmt = null;
        ResultSet executeQuery = null;
        try {
            // Подключение к базе данных
            con = DriverManager.getConnection(connectionString);
            // Отправка запроса на выборку и получение результатов
            stmt = con.createStatement();
            String query = String.format("SELECT * FROM [Login] WHERE login = '%s' AND password = '%s'", login, password);
            executeQuery = stmt.executeQuery(query);
            // Обход результатов выборки
            while (executeQuery.next()) {
                JSONObject profile = new JSONObject();
                try {
                    profile.put("name", executeQuery.getString("name"));
                    profile.put("city", executeQuery.getString("city"));
                    profile.put("age", ""+executeQuery.getInt("age"));
                    profile.put("sex", executeQuery.getBoolean("sex") ? "Male" : "Female");
                    profile.put("login", executeQuery.getString("login"));
                } catch (JSONException e) { e.printStackTrace(); }
                return profile;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println(ex.toString());
        }
        finally {
            closeConnection(con, stmt, executeQuery);
        }
        return new JSONObject();
    }

    public String AdminLogin(String login, String password)
    {
        Connection con = null;
        Statement stmt = null;
        ResultSet executeQuery = null;
        try {
            // Подключение к базе данных
            con = DriverManager.getConnection(connectionString);
            // Отправка запроса на выборку и получение результатов
            stmt = con.createStatement();
            String query = String.format("SELECT * FROM [Company] WHERE login = '%s' AND password = '%s'", login, password);
            executeQuery = stmt.executeQuery(query);
            // Обход результатов выборки
            while (executeQuery.next())
                return executeQuery.getString("name");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        finally {
            closeConnection(con, stmt, executeQuery);
        }
        return "false";
    }

    public List<Note> GetNotes(String login){
        Connection con = null;
        Statement stmt = null;
        ResultSet executeQuery = null;
        List<Note> notes = new ArrayList<>();
        try {
            // Подключение к базе данных
            con = DriverManager.getConnection(connectionString);
            // Отправка запроса на выборку и получение результатов
            stmt = con.createStatement();
            String query = String.format("SELECT * FROM [Notes] WHERE owner='%s' AND done=0 AND deleted=0", login);
            executeQuery = stmt.executeQuery(query);
            // Обход результатов выборки
            while (executeQuery.next()) {
                notes.add(new Note(executeQuery.getString("company"),
                        executeQuery.getString("address"),
                        executeQuery.getString("logo"),
                        executeQuery.getTimestamp("time"),
                        executeQuery.getLong("id")));
            }

        } catch (SQLException ex) {
            // Обработка исключений
            System.out.println(ex.getMessage());
            System.out.println(ex.toString());
        }
        finally {
            closeConnection(con, stmt, executeQuery);
        }

        return notes;
    }

    public boolean AddNotes(String login, String company, String logo, String address, Date time){
        Connection con = null;
        Statement stmt = null;
        ResultSet executeQuery = null;
        try {
            // Подключение к базе данных
            con = DriverManager.getConnection(connectionString);
            // Отправка запроса на выборку и получение результатов
            stmt = con.createStatement();
            try { logo = URLDecoder.decode(logo, "UTF-8"); } catch (UnsupportedEncodingException ignored) { }
            String query = String.format("INSERT INTO Notes(company, logo, address, owner, time)\n" +
                    "VALUES ('%s', '%s', '%s', '%s', '%5$tY-%5$tm-%5$tdT%5$tH:%5$tM:%5$tS')", company, logo, address, login, time);
            stmt.executeUpdate(query);
            return true;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println(ex.toString());
        }
        finally {
            closeConnection(con, stmt, executeQuery);
        }

        return false;
    }

    public void deleteNote(long id){
        Connection con = null;
        Statement stmt = null;
        ResultSet executeQuery = null;
        try {
            // Подключение к базе данных
            con = DriverManager.getConnection(connectionString);
            // Отправка запроса на выборку и получение результатов
            stmt = con.createStatement();
            String query = String.format("UPDATE Notes SET deleted=1 WHERE id=%s", id);
            stmt.executeUpdate(query);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println(ex.toString());
        }
        finally {
            closeConnection(con, stmt, executeQuery);
        }
    }

    public List<Company> GetCompanies(){
        Connection con = null;
        Statement stmt = null;
        ResultSet executeQuery = null;
        List<Company> companies = new ArrayList<>();
        try {
            // Подключение к базе данных
            con = DriverManager.getConnection(connectionString);
            // Отправка запроса на выборку и получение результатов
            stmt = con.createStatement();
            String query = String.format("SELECT * FROM [Company]");
            executeQuery = stmt.executeQuery(query);
            // Обход результатов выборки
            while (executeQuery.next())
                companies.add(new Company(executeQuery.getString("name"),
                        executeQuery.getString("logo")));

        } catch (SQLException ex) {
            // Обработка исключений
            System.out.println(ex.getMessage());
            System.out.println(ex.toString());
        }
        finally {
            closeConnection(con, stmt, executeQuery);
        }

        return companies;
    }

    public List<Branch> GetBranches(String company){
        Connection con = null;
        Statement stmt = null;
        ResultSet executeQuery = null;
        List<Branch> branches = new ArrayList<>();
        try {
            // Подключение к базе данных
            con = DriverManager.getConnection(connectionString);
            // Отправка запроса на выборку и получение результатов
            stmt = con.createStatement();
            String query = String.format("SELECT * FROM [Branches] WHERE company = '%s'", company);
            executeQuery = stmt.executeQuery(query);
            // Обход результатов выборки
            while (executeQuery.next())
                branches.add(new Branch(executeQuery.getString("company"),
                        executeQuery.getString("address"),
                        executeQuery.getDouble("latitude"),
                        executeQuery.getDouble("longitude"),
                        executeQuery.getDouble("average")));

        } catch (SQLException ex) {
            // Обработка исключений
            System.out.println(ex.getMessage());
            System.out.println(ex.toString());
        }
        finally {
            closeConnection(con, stmt, executeQuery);
        }

        return branches;
    }

    public boolean AddBranch(String company, String name, double longitude, double latitude){
        Connection con = null;
        Statement stmt = null;
        ResultSet executeQuery = null;
        try {
            con = DriverManager.getConnection(connectionString);
            stmt = con.createStatement();
            String query = String.format("INSERT INTO Branches(company, address, longitude, latitude, average)" +
                        " VALUES ('%s', '%s', %s, %s, 0);", company, name,
                        (""+longitude).replace(',','.'), (""+latitude).replace(',','.'));
            stmt.executeUpdate(query);
            return true;
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
        finally {
            closeConnection(con, stmt, executeQuery);
        }

        return false;
    }
}
