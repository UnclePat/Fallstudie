package Backend.Database;

import java.sql.*;

public class DataBaseServer
{
    Connection databaseConnection;

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
        this.dbConnect("jdbc:sqlserver://localhost\\sqlexpress;user=sa;password=pwd4sa");
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

    public int markAsDeleted(){
        //ToDo String Tabelle, int Key -> Set DeletionFlag = true
        return 0;
    }

    public int markAsNotDeleted(){
        //ToDo String Tabelle, int Key -> Set DeletionFlag = false
        return 0;
    }
}