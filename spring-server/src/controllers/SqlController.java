package controllers;

import structure.Branch;
import structure.Company;
import structure.Note;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlController {
    private static String connectionString = "jdbc:sqlserver://localhost\\DESKTOP-DP477LA/tema:55150;database=IQueue;integratedSecurity=true";
    private Connection con;
    private Statement stmt;
    private ResultSet executeQuery;

    private void closeConnection(){
        // Закрываем соединение
        try { con.close(); } catch(SQLException | NullPointerException se) { /*can't do anything */ }
        try { stmt.close(); } catch(SQLException | NullPointerException se) { /*can't do anything */ }
        try { executeQuery.close(); } catch(SQLException | NullPointerException se) { /*can't do anything */ }
    }


    public boolean Registration(String login, String password, String name, String surname)
    {
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
            query = String.format("INSERT INTO Login(login, password, name, surname)\n" +
                    "VALUES ('%s', '%s', '%s', '%s');", login, password, name, surname);
            stmt.executeUpdate(query);
            return true;
        } catch (SQLException ex) {
            // Обработка исключений
            System.out.println(ex.getMessage());
            System.out.println(ex.toString());
        }
        finally {
            closeConnection();
        }
        return false;
    }

    public boolean Login(String login, String password)
    {
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
            closeConnection();
        }
        return false;
    }

    public List<Note> GetNotes(String login){
        List<Note> notes = new ArrayList<>();
        try {
            // Подключение к базе данных
            con = DriverManager.getConnection(connectionString);
            // Отправка запроса на выборку и получение результатов
            stmt = con.createStatement();
            String query = String.format("SELECT * FROM [Notes] WHERE owner = '%s'", login);
            executeQuery = stmt.executeQuery(query);
            // Обход результатов выборки
            while (executeQuery.next())
                notes.add(new Note(executeQuery.getString("company"),
                        executeQuery.getString("address"),
                        executeQuery.getString("logo"),
                        executeQuery.getDate("time")));

        } catch (SQLException ex) {
            // Обработка исключений
            System.out.println(ex.getMessage());
            System.out.println(ex.toString());
        }
        finally {
            closeConnection();
        }

        return notes;
    }

    public boolean AddNotes(String login, String company, String logo, String address, Date time){
        try {
            // Подключение к базе данных
            con = DriverManager.getConnection(connectionString);
            // Отправка запроса на выборку и получение результатов
            stmt = con.createStatement();
            String query = String.format("INSERT INTO Notes(company, logo, address, owner, time)\n" +
                    "VALUES ('%s', '%s', '%s', '%s', '%5$tY-%5$tm-%5$tdT%5$tH:%5$tM:%5$tS')", company, logo, address, login, time);
            stmt.executeUpdate(query);
            return true;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println(ex.toString());
        }
        finally {
            closeConnection();
        }

        return false;
    }

    public List<Company> GetCompanies(){
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
            closeConnection();
        }

        return companies;
    }

    public List<Branch> GetBranches(String company){
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
                        executeQuery.getString("address")));

        } catch (SQLException ex) {
            // Обработка исключений
            System.out.println(ex.getMessage());
            System.out.println(ex.toString());
        }
        finally {
            closeConnection();
        }

        return branches;
    }
}
