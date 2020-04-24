package controllers;

import org.apache.commons.math3.stat.regression.SimpleRegression;
import structure.AdminNote;

import java.sql.*;
//import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class SqlControllerStats {
    private static String connectionString = "jdbc:sqlserver://localhost\\DESKTOP-DP477LA/tema:55150;database=IQueue;integratedSecurity=true";

    private void closeConnection(Connection c, Statement s, ResultSet r){
        // Закрываем соединение
        try { c.close(); } catch(SQLException | NullPointerException se) { /*can't do anything */ }
        try { s.close(); } catch(SQLException | NullPointerException se) { /*can't do anything */ }
        try { r.close(); } catch(SQLException | NullPointerException se) { /*can't do anything */ }
    }

    public String getCompanyName(String login)
    {
        Connection con = null;
        Statement stmt = null;
        ResultSet executeQuery = null;
        try {
            con = DriverManager.getConnection(connectionString);
            stmt = con.createStatement();
            String loginQuery = String.format("SELECT [name] FROM [Company] WHERE [login]='%s'", login);
            executeQuery = stmt.executeQuery(loginQuery);
            executeQuery.next();
            return executeQuery.getString(1);
        } catch (SQLException ex) { System.out.println(ex.getMessage()); }
        finally { closeConnection(con, stmt, executeQuery); }
        return "Unknown";
    }

    public float getAverageAge(String company) {
        Connection con = null;
        Statement stmt = null;
        ResultSet executeQuery = null;
        try {
            con = DriverManager.getConnection(connectionString);
            stmt = con.createStatement();
            String query = String.format("SELECT AVG(Login.age) FROM Notes \n" +
                    "JOIN Login ON Login.login = Notes.owner\n" +
                    "WHERE Notes.company='%s' AND Notes.deleted=0", company);

            executeQuery = stmt.executeQuery(query);
            executeQuery.next();
            return executeQuery.getInt(1);
        } catch (SQLException ex) { System.out.println(ex.getMessage()); }
        finally { closeConnection(con, stmt, executeQuery); }
        return 0;
    }


    public float[] getSexPiechart(String company)
    {
        Connection con = null;
        Statement stmt = null;
        ResultSet executeQuery = null;
        try {
            con = DriverManager.getConnection(connectionString);
            stmt = con.createStatement();
            String maleCountQuery = String.format("SELECT \n" +
                    "COUNT(Login.sex) FROM Notes\n" +
                    "JOIN Login ON Login.login = Notes.owner \n" +
                    "WHERE Notes.company='%s' AND Login.sex=%s AND Notes.deleted=0", company, 1);
            String femaleCountQuery = String.format("SELECT \n" +
                    "COUNT(Login.sex) FROM Notes\n" +
                    "JOIN Login ON Login.login = Notes.owner \n" +
                    "WHERE Notes.company='%s' AND Login.sex=%s AND Notes.deleted=0", company, 0);

            float[] data = new float[] {0, 0};
            executeQuery = stmt.executeQuery(maleCountQuery);
            executeQuery.next();
            data[0] = executeQuery.getInt(1);
            executeQuery = stmt.executeQuery(femaleCountQuery);
            executeQuery.next();
            data[1] = executeQuery.getInt(1);
            return data;
        } catch (SQLException ex) { System.out.println(ex.getMessage()); }
        finally { closeConnection(con, stmt, executeQuery); }
        return new float[] {50, 50};
    }

    public int[][] getOrdersW(String company) {
        Connection con = null;
        Statement stmt = null;
        ResultSet executeQuery = null;
        int[][] data = new int[][] {new int[7], new int[7], new int[7]};
        try {
            con = DriverManager.getConnection(connectionString);
            stmt = con.createStatement();

            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 3; j++) {
                    String ordersCount = String.format("SELECT COUNT(*) FROM [Notes] " +
                            "WHERE YEAR(time)=YEAR(CURRENT_TIMESTAMP)-%s AND company='%s' " +
                            "AND done=%s AND deleted=%s" +
                            "AND DATEPART(week, time)=DATEPART(week,CURRENT_TIMESTAMP);",
                            i, company, j==1?1:0, j==2?1:0);

                    executeQuery = stmt.executeQuery(ordersCount);
                    executeQuery.next();
                    data[2-j][6-i] = executeQuery.getInt(1);
                }
            }
            return data;
        } catch (SQLException ex) { System.out.println(ex.getMessage()); }
        finally { closeConnection(con, stmt, executeQuery); }
        return data;
    }

    public int[][] getOrdersM(String company) {
        Connection con = null;
        Statement stmt = null;
        ResultSet executeQuery = null;
        int[][] data = new int[][] {new int[12], new int[12], new int[12]};
        try {
            con = DriverManager.getConnection(connectionString);
            stmt = con.createStatement();

            for (int i = 0; i < 12; i++) {
                for (int j = 0; j < 3; j++) {
                    String ordersCount = String.format("SELECT COUNT(*) FROM [Notes] " +
                                    "WHERE YEAR(time)=YEAR(CURRENT_TIMESTAMP)-%s AND company='%s' " +
                                    "AND done=%s AND deleted=%s" +
                                    "AND DATEPART(month, time)=DATEPART(month, CURRENT_TIMESTAMP);",
                            i, company, j==1?1:0, j==2?1:0);

                    executeQuery = stmt.executeQuery(ordersCount);
                    executeQuery.next();
                    data[2-j][11-i] = executeQuery.getInt(1);
                }
            }
            return data;
        } catch (SQLException ex) { System.out.println(ex.getMessage()); }
        finally { closeConnection(con, stmt, executeQuery); }
        return data;
    }

    public int[][] getOrdersY(String company) {
        Connection con = null;
        Statement stmt = null;
        ResultSet executeQuery = null;
        int[][] data = new int[][] {new int[7], new int[7], new int[7]};
        try {
            con = DriverManager.getConnection(connectionString);
            stmt = con.createStatement();

            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 3; j++) {
                    String ordersCount = String.format("SELECT COUNT(*) FROM [Notes] " +
                            "WHERE YEAR(time)=YEAR(CURRENT_TIMESTAMP)-%s AND company='%s' " +
                            "AND done=%s AND deleted=%s;", i, company, j==1?1:0, j==2?1:0);

                    executeQuery = stmt.executeQuery(ordersCount);
                    executeQuery.next();
                    data[2-j][6-i] = executeQuery.getInt(1);
                }
            }
            return data;
        } catch (SQLException ex) { System.out.println(ex.getMessage()); }
        finally { closeConnection(con, stmt, executeQuery); }
        return data;
    }

    public int[] getUniqueOrdersM(String company) {
        Connection con = null;
        Statement stmt = null;
        ResultSet executeQuery = null;
        int[] data = new int[12];
        try {
            con = DriverManager.getConnection(connectionString);
            stmt = con.createStatement();

            for (int i = 0; i < 12; i++) {
                String ordersCount = String.format("SELECT COUNT(DISTINCT owner) FROM [Notes] " +
                                "WHERE YEAR(time)=YEAR(CURRENT_TIMESTAMP)-%s AND company='%s' " +
                                " AND deleted=0" +
                                "AND DATEPART(month, time)=DATEPART(month, CURRENT_TIMESTAMP);",
                        i, company);

                executeQuery = stmt.executeQuery(ordersCount);
                executeQuery.next();
                data[11-i] = executeQuery.getInt(1);
            }
            return data;
        } catch (SQLException ex) { System.out.println(ex.getMessage()); }
        finally { closeConnection(con, stmt, executeQuery); }
        return data;
    }

    public int getAllOrdersCount(String company) {
        Connection con = null;
        Statement stmt = null;
        ResultSet executeQuery = null;
        int data = 0;
        try {
            con = DriverManager.getConnection(connectionString);
            stmt = con.createStatement();

            String ordersCount = String.format("SELECT COUNT(*) FROM [Notes] " +
                    "WHERE company='%s' AND deleted=0;", company);

            executeQuery = stmt.executeQuery(ordersCount);
            executeQuery.next();
            data = executeQuery.getInt(1);

            return data;
        } catch (SQLException ex) { System.out.println(ex.getMessage()); }
        finally { closeConnection(con, stmt, executeQuery); }
        return data;
    }

    public int getTodayOrdersCount(String company) {
        Connection con = null;
        Statement stmt = null;
        ResultSet executeQuery = null;
        int data = 0;
        try {
            con = DriverManager.getConnection(connectionString);
            stmt = con.createStatement();

            String ordersCount = String.format("SELECT COUNT(*) FROM [Notes] " +
                    "WHERE company='%s' AND DATEPART(dayofyear, time)=DATEPART(dayofyear, CURRENT_TIMESTAMP)" +
                    "AND YEAR(time)=YEAR(CURRENT_TIMESTAMP) AND deleted=0;", company);

            executeQuery = stmt.executeQuery(ordersCount);
            executeQuery.next();
            data = executeQuery.getInt(1);

            return data;
        } catch (SQLException ex) { System.out.println(ex.getMessage()); }
        finally { closeConnection(con, stmt, executeQuery); }
        return data;
    }

    public int getLastWeekOrdersCount(String company) {
        Connection con = null;
        Statement stmt = null;
        ResultSet executeQuery = null;
        int data = 0;
        try {
            con = DriverManager.getConnection(connectionString);
            stmt = con.createStatement();

            String ordersCount = String.format("SELECT COUNT(*) FROM [Notes] " +
                    "WHERE company='%s' AND DATEPART(week, time)=DATEPART(week, CURRENT_TIMESTAMP)" +
                    "AND YEAR(time)=YEAR(CURRENT_TIMESTAMP) AND deleted=0;", company);

            executeQuery = stmt.executeQuery(ordersCount);
            executeQuery.next();
            data = executeQuery.getInt(1);
            return data;
        } catch (SQLException ex) { System.out.println(ex.getMessage()); }
        finally { closeConnection(con, stmt, executeQuery); }
        return data;
    }

    public int getLastMonthOrdersCount(String company) {
        Connection con = null;
        Statement stmt = null;
        ResultSet executeQuery = null;
        int data = 0;
        try {
            con = DriverManager.getConnection(connectionString);
            stmt = con.createStatement();

            String ordersCount = String.format("SELECT COUNT(*) FROM [Notes] " +
                    "WHERE company='%s' AND DATEPART(month, time)=DATEPART(month, CURRENT_TIMESTAMP)" +
                    "AND YEAR(time)=YEAR(CURRENT_TIMESTAMP) AND deleted=0;", company);

            executeQuery = stmt.executeQuery(ordersCount);
            executeQuery.next();
            data = executeQuery.getInt(1);

            return data;
        } catch (SQLException ex) { System.out.println(ex.getMessage()); }
        finally { closeConnection(con, stmt, executeQuery); }
        return data;
    }

    public List<AdminNote> GetNotesAdmin(String company, String branch) {
        Connection con = null;
        Statement stmt = null;
        ResultSet executeQuery = null;
        List<AdminNote> notes = new ArrayList<>();
        try {
            con = DriverManager.getConnection(connectionString);
            stmt = con.createStatement();

            String ordersCount = String.format("SELECT Notes.time, Login.name FROM Notes Join Login\n" +
                            "  ON Notes.owner=Login.login WHERE Notes.time<GETDATE() AND Notes.deleted=0 AND\n" +
                            "  Notes.done=0 AND Notes.company='%s' AND Notes.address='%s'", company, branch);

            executeQuery = stmt.executeQuery(ordersCount);
            while (executeQuery.next())
                notes.add(new AdminNote(
                        executeQuery.getString("name"),
                        executeQuery.getTimestamp("time")));

            notes.sort((o1, o2) -> -Long.compare(o2.getTime().getTime(), o1.getTime().getTime()));
            return notes;
        } catch (SQLException ex) { System.out.println(ex.getMessage()); }
        finally { closeConnection(con, stmt, executeQuery); }
        return notes;
    }

    public boolean doneNoteAdmin(String company, String branch, String owner, String time) {
        Connection con = null;
        Statement stmt = null;
        ResultSet executeQuery = null;
        try {
            con = DriverManager.getConnection(connectionString);
            stmt = con.createStatement();

            time = time.replace("T", " ").replace("+", " ");
            String doneNote = String.format("UPDATE Notes SET done=1, finish=GETDATE() " +
                    "WHERE company='%s' AND address='%s'" +
                    " AND DATEDIFF(SECOND, '%s', time)=0 \n", company, branch, time);
            stmt.execute(doneNote);

            retrain(company, branch);
            return true;
        } catch (SQLException ex) { System.out.println(ex.getMessage()); }
        finally { closeConnection(con, stmt, executeQuery); }
        return false;
    }

    public void retrain(String company, String branch) {
        SimpleRegression regression = new SimpleRegression();

        Connection con = null;
        Statement stmt = null;
        ResultSet executeQuery = null;
        try {
            con = DriverManager.getConnection(connectionString);
            stmt = con.createStatement();

            String ordersCount = String.format("SELECT Notes.time, Notes.finish " +
                    "FROM [IQueue].[dbo].[Notes] " +
                    "WHERE Notes.company='%s' AND Notes.address='%s'",
                    company, branch);

            executeQuery = stmt.executeQuery(ordersCount);
            boolean empty = true;
            while (executeQuery.next())
                if (executeQuery.getTimestamp("finish") != null) {
                    regression.addData(executeQuery.getTimestamp("time").getTime(),
                            executeQuery.getTimestamp("finish").getTime());
                    empty = false;
                }

            if (empty) return;

            double average = (regression.predict(new Date().getTime())  - new Date().getTime()) / 60000;
            String averageStr = String.format(Locale.ENGLISH, "%(.2f", average);
            String updateAverage = String.format("UPDATE Branches SET average=%s " +
                    "WHERE company='%s' AND address='%s'", averageStr, company, branch);
            stmt.execute(updateAverage);
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        finally {
            closeConnection(con, stmt, executeQuery);
        }
    }

    public void Generate(String company, String address) {
        Connection con = null;
        Statement stmt = null;
        ResultSet executeQuery = null;
        Random rnd = new Random();
        try {
            con = DriverManager.getConnection(connectionString);
            stmt = con.createStatement();

            for (int i = 0; i < 50; i++) {
                String login = "" + (rnd.nextInt(10) + 1) + "@";
                int done = 0, deleted = i % 10 == 0 ? 1 : 0;
                Date time = new Date();
                time.setYear(2013 + rnd.nextInt(7) - 1900);
                time.setMonth(rnd.nextInt(12));
                time.setDate(rnd.nextInt(28));
                if (time.getTime() > new Date().getTime())
                    time = new Date();
                else
                    done = 1;
                Date finish = new Date(time.getTime() + rnd.nextInt(45) * 60000);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSS");
                String timeStr = sdf.format(new java.sql.Date(time.getTime()));
                String query;
                String finishStr = sdf.format(new java.sql.Date(finish.getTime()));
                if (finish.getTime() > new Date().getTime())
                    query = String.format("INSERT INTO Notes(company, logo, address, owner, deleted, done, time) " +
                        "VALUES ('%s', '%s', '%s', '%s', %s, %s, '%s')",
                        company, "test", address, login, deleted, done, timeStr);
                else
                    query = String.format("INSERT INTO Notes(company, logo, address, owner, deleted, done, time, finish) " +
                                    "VALUES ('%s', '%s', '%s', '%s', %s, %s, '%s', '%s')",
                            company, "test", address, login, deleted, done, timeStr, finishStr);

                stmt.execute(query);
            }
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        finally {
            closeConnection(con, stmt, executeQuery);
        }

        retrain(company, address);
    }
}
