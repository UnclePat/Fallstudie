package Backend.Database;

import java.sql.Connection;
import java.sql.DriverManager;

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
}