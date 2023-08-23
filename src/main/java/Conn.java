import java.sql.Connection;
import java.sql.DriverManager;

public class Conn {
    private static final String url = "jdbc:mysql://localhost:3306/testeatletas";
    private static final String user = "root";
    private static final String password = "";
    private static Connection conn;

    public static Connection getConn() {
        try {
            if ( conn == null ) {
                conn = DriverManager.getConnection( url, user, password );
                return conn;
            } else {
                return conn;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}