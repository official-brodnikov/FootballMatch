import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectToDataBase {
    private static final String Url = "jdbc:mysql://localhost:3306/footballmatch"+
            "?verifyServerCertificate=false"+
            "&useSSL=false"+
            "&requireSSL=false"+
            "&useLegacyDatetimeCode=false"+
            "&amp"+
            "&serverTimezone=UTC";
    private static final String Password = "root";
    private static final String User = "root";

    public static void main(String[] args) throws SQLException {
        Connection connection;

        connection = DriverManager.getConnection(Url, User, Password);
        if (!connection.isClosed())
            System.out.println("Success");
        else
            System.out.println("NotSuccess");
    }
}
