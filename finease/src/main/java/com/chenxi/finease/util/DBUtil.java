// package com.chenxi.finease.util;

// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.SQLException;

// public class DBUtil {
//     private static Connection connection = null;

//     public static Connection getConnection() throws SQLException {
//         if (connection != null) {
//             return connection;
//         } else {
//             String driver = "com.mysql.cj.jdbc.Driver";
//             String url = "jdbc:mysql://localhost:3306/fineasedb";
//             String user = "root";
//             String password = "root";
//             try {
//                 Class.forName(driver);
//                 connection = DriverManager.getConnection(url, user, password);
//             } catch (ClassNotFoundException e) {
//                 throw new RuntimeException("Database driver not found", e);
//             }
//         }
//         return connection;
//     }
// }

package com.chenxi.finease.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DBUtil {
    private static DataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/fineasedb");
        config.setUsername("root");
        config.setPassword("root");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");

        // Configure other HikariCP properties as needed

        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
