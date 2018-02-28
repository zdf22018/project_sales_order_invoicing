
package ipd12.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Jian Zhao
 */
public class Database {
    private final String dbURL = "//azuredf.database.windows.net";
    private final String databaseName = "project";
    private final String username = "zdf2018";
    private final String password = "zoe20178@";
    
    private Connection conn;
    
    public Database() throws SQLException {
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        }catch(ClassNotFoundException ex){
            //exception chaining
            throw new SQLException("Driver not found.", ex);
        }
        
        String connectionString = String.format("jdbc:sqlserver:%s:1433;database=%s;user=%s;password=%s;", dbURL, databaseName, username, password);
        conn = DriverManager.getConnection(connectionString);
    }
}
