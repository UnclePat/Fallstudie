package Backend.Database;

import java.sql.*;

public class DataBaseServer
{
    private Connection databaseConnection;
    private final String DB_CONNECTION_STRING = "jdbc:sqlserver://localhost\\sqlexpress;user=sa;password=pwd4sa";

    public void dbConnect()
    {
        try {
            String dbURL = DB_CONNECTION_STRING;
            Connection conn = DriverManager.getConnection(dbURL);
            if (conn != null) {
                System.out.println("Connected");
                this.databaseConnection = conn;
            }
            else
            {
                System.out.println("Connection attempt failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DataBaseServer()
    {
        this.dbConnect();
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
        Statement statement = this.databaseConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.execute(query);

        ResultSet rs = statement.getGeneratedKeys();
        rs.next();

        return rs.getInt(1);
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