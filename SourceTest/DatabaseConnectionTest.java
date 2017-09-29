import Backend.Database.DataBaseServer;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnectionTest {
    private Connection databaseConnection;
    private final String DB_CONNECTION_STRING = "jdbc:sqlserver://localhost\\sqlexpress;user=sa;password=pwd4sa";

    public void DatabaseConnectionTest()
    {
        try {
            String dbURL = DB_CONNECTION_STRING;
            Connection conn = DriverManager.getConnection(dbURL);
            if (conn != null) {
                this.databaseConnection = conn;
                System.out.println("Connection established");
            }
            else
            {
                System.out.println("Connection failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        DatabaseConnectionTest connServer = new DatabaseConnectionTest();
    }
}
