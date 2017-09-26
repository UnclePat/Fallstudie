package Backend.Database;

import java.sql.*;

public class DataBaseServer
{
    private Connection databaseConnection;
    private final String DB_CONNECTION_STRING = "jdbc:sqlserver://localhost\\sqlexpress;user=sa;password=pwd4sa";

    public void dbConnect(String db_connect_string)
    {
        try {
            String dbURL = db_connect_string;
            Connection conn = DriverManager.getConnection(dbURL);
            if (conn != null) {
                System.out.println("Connected");
            }
            else
            {
                this.databaseConnection = conn;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void DataBaseServer()
    {
        this.dbConnect(DB_CONNECTION_STRING);
    }

    public static void main(String[] args)
    {
        DataBaseServer connServer = new DataBaseServer();
    }

    public ResultSet select(String query) throws SQLException {
        Statement statement = this.databaseConnection.createStatement();

        return statement.executeQuery(query);
    }

    public int update(String query) throws SQLException {
        Statement statement = this.databaseConnection.createStatement();

        return statement.executeUpdate(query);
    }

    public int insert(String query) throws SQLException {
        Statement statement = this.databaseConnection.createStatement();

        return statement.executeUpdate(query);
    }

    public int markAsDeleted(String table, int key) throws SQLException {
        String sql = " UPDATE " + table +
        " SET deletionFlag = true" +
        " WHERE intKey = " + key;

        Statement statement = this.databaseConnection.createStatement();
        return statement.executeUpdate(sql);
    }

    public int markAsNotDeleted(String table, int key) throws SQLException{
        String sql = " UPDATE " + table +
                " SET deletionFlag = false" +
                " WHERE intKey = " + key;

        Statement statement = this.databaseConnection.createStatement();
        return statement.executeUpdate(sql);
    }
}