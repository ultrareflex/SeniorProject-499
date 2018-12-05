package seniorprojec;


import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import javax.sql.DataSource;
public class DBconnct {


    public static DataSource getMysql() {

        MysqlDataSource dataSource = new MysqlDataSource();
        //dataSource or drivemanager for mySQL
        dataSource.setDatabaseName("SeniorProject"); 
        dataSource.setUser("root"); 
        dataSource.setPassword("y0r1ck12"); 
        dataSource.setServerName("localhost"); 

        return dataSource;
    
        
    }



}