package Backend.Database;

import Backend.Base.Application;

import javax.swing.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Enthält alle nötigen Methoden zur direkten Kommunikation mit der Datenbank und casht die Verbindung.
 */
public class DataBaseServer
{
    private static Connection databaseConnection = null;
    private static final String DB_CONNECTION_STRING = "jdbc:sqlserver://localhost\\sqlexpress;user=sa;password=pwd4sa;databaseName=Haushaltsbuch;";

    /**
     * Öffnet bei erstmaliger Ausführung eine Verbindung zur Datenbank. Die Verbindung wird gecasht. Bei weiteren
     * Ausführungen wird die gecashte Verbindung verwendet, statt eine neue zu erzeugen.
     */
    public static void dbConnect(){
        try {
            if (databaseConnection != null){
                return;
            }

            String dbURL = DB_CONNECTION_STRING;
            Connection conn = DriverManager.getConnection(dbURL);
            if (conn != null) {
                System.out.println("Connected");
                DataBaseServer.databaseConnection = conn;
            }
            else
            {
                System.out.println("Connection attempt failed");
            }
        } catch (Exception e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            JOptionPane.showMessageDialog(null,errors.toString() , "InfoBox: Error", JOptionPane.INFORMATION_MESSAGE);

            e.printStackTrace();
        }
    }

    public DataBaseServer(){
        DataBaseServer.dbConnect();
    }

    /**
     * Setzt ein Select Statement auf der Datenbank ab. Aus der angegebenen Query und der Value Liste wird hierbei ein
     * preparedStatement generiert und ausgeführt.
     * @param query
     * Die Abfrage, die ausgeführt werden soll.
     * @param values
     * Liste der an die Abfrage anzuhängenden Werte um Parameter zu befüllen.
     * @return
     * Das Ergebnis der Abfrage.
     * @throws SQLException
     */
    public ResultSet select(String query, List<String> values) throws SQLException {
        PreparedStatement statement = DataBaseServer.databaseConnection.prepareStatement(query);

        int maxI = values.size();
        for (int i = 0; i < maxI; i++){
            if (values.get(i) == null)
                statement.setNull(i + 1, 1);

            statement.setString(i + 1, values.get(i));
        }

        return statement.executeQuery();
    }

    /**
     * Setzt ein Update Statement auf der Datenbank ab. Aus der angegebenen Query und der Value Liste wird hierbei ein
     * preparedStatement generiert und ausgeführt.
     * @param query
     * Die Abfrage, die ausgeführt werden soll.
     * @param values
     * Liste der an die Abfrage anzuhängenden Werte um Parameter zu befüllen.
     * @return
     * Das Ergebnis der Abfrage.
     * @throws SQLException
     */
    public int update(String query, List<String> values) throws SQLException {
        PreparedStatement statement = DataBaseServer.databaseConnection.prepareStatement(query);

        int maxI = values.size();
        for (int i = 0; i < maxI; i++){
            if (values.get(i) == null)
                statement.setNull(i + 1, 1);

            statement.setString(i + 1, values.get(i));
        }

        return statement.executeUpdate();
    }

    /**
     * Setzt ein Insert Statement auf der Datenbank ab. Aus der angegebenen Query und der Value Liste wird hierbei ein
     * preparedStatement generiert und ausgeführt. Das ergebnis der Abfrage ist der generierte Schlüßel der neuen Zeile.
     * @param query
     * Die Abfrage, die ausgeführt werden soll.
     * @param values
     * Liste der an die Abfrage anzuhängenden Werte um Parameter zu befüllen.
     * @return
     * Der erzeugte Schlüßel.
     * @throws SQLException
     */
    public int insert(String query, List<String> values) throws SQLException {
        PreparedStatement statement = DataBaseServer.databaseConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

        int maxI = values.size();
        for (int i = 0; i < maxI; i++){
            if (values.get(i) == null)
                statement.setNull(i + 1, 1);

            statement.setString(i + 1, values.get(i));
        }

        statement.execute();

        ResultSet rs = statement.getGeneratedKeys();
        rs.next();

        return rs.getInt(1);
    }

    /**
     * Markiert ein Element einer Tabelle als gelöscht.
     * @param table
     * Name der Tabelle von welcher ein Element als gelöscht markiert werden soll.
     * @param key
     * Schlüßel des als gelöscht zu markierenden Elements.
     * @return
     * Das Ergebnis der Abfrage.
     * @throws SQLException
     */
    public int markAsDeleted(String table, int key) throws SQLException {
        String sql = " UPDATE " + table +
                " SET [boolDeletionFlag] = 1," +
                "     [intFkeyUserDeletedBy] = ?," +
                "     [dateDeleted] = ?" +
                " WHERE intKey = ?";

        PreparedStatement statement = DataBaseServer.databaseConnection.prepareStatement(sql);
        statement.setString(1, Application.getCurrentUser().getKey().toString());
        statement.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
        statement.setInt(3, key);

        return statement.executeUpdate();
    }

    /**
     * Hebt die Löschmarkierung eines Elementes einer Tabelle auf.
     * @param table
     * Name der Tabelle von welcher ein Element als nicht gelöscht markiert werden soll.
     * @param key
     * Schlüßel des als nicht gelöscht zu markierenden Elements.
     * @return
     * Das Ergebnis der Abfrage.
     * @throws SQLException
     */
    public int markAsNotDeleted(String table, int key) throws SQLException{
        String sql = " UPDATE " + table +
                " SET [boolDeletionFlag] = 0," +
                "     [intFkeyUserDeletedBy] = ?," +
                "     [dateDeleted] = ?" +
                " WHERE intKey = ?";

        PreparedStatement statement = DataBaseServer.databaseConnection.prepareStatement(sql);
        statement.setNull(1, 1);
        statement.setNull(2, 1);
        statement.setInt(3, key);

        return statement.executeUpdate();
    }

    /**
     * Erstellt ein Backup der Datenbank in den übergebenen Pfad.
     * @param path
     * Pfad in den das Backup erstellt werden soll.
     * @throws SQLException
     */
    public void executeBackup(String path) throws SQLException {
        String backupStatement = "BACKUP DATABASE [Haushaltsbuch] TO DISK = '" + path + "' with INIT, NAME = N'SQL Voll'";
        Statement statement = DataBaseServer.databaseConnection.createStatement();
        statement.execute(backupStatement);
    }
}