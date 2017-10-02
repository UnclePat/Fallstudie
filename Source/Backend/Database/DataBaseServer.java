package Backend.Database;

import java.sql.*;
import java.util.List;

public class DataBaseServer
{
    private Connection databaseConnection;
    private final String DB_CONNECTION_STRING = "jdbc:sqlserver://localhost\\sqlexpress;user=sa;password=pwd4sa";

    public void dbConnect(){
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

    public DataBaseServer(){
        this.dbConnect();
    }

    public ResultSet select(String query) throws SQLException {
        Statement statement = this.databaseConnection.createStatement();

        return statement.executeQuery(query);
    }

    public int update(String query, List<String> values) throws SQLException {
        PreparedStatement statement = this.databaseConnection.prepareStatement(query);

        int maxI = values.size();
        for (int i = 0; i < maxI; i++){
            statement.setString(i + 1, values.get(i));
        }

        return statement.executeUpdate();
    }

    public int insert(String query, List<String> values) throws SQLException {
        PreparedStatement statement = this.databaseConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

        int maxI = values.size();
        for (int i = 0; i < maxI; i++){
            statement.setString(i + 1, values.get(i));
        }

        statement.execute(query);

        ResultSet rs = statement.getGeneratedKeys();
        rs.next();

        return rs.getInt(1);
    }

    public int markAsDeleted(String table, int key) throws SQLException {
        String sql = " UPDATE " + table +
                " SET boolDeletionFlag = true" +
                " WHERE intKey = " + key;

        Statement statement = this.databaseConnection.createStatement();
        return statement.executeUpdate(sql);
    }

    public int markAsNotDeleted(String table, int key) throws SQLException{
        String sql = " UPDATE " + table +
                " SET boolDeletionFlag = false" +
                " WHERE intKey = " + key;

        Statement statement = this.databaseConnection.createStatement();
        return statement.executeUpdate(sql);
    }
}