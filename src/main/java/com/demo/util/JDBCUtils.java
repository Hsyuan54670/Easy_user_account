package main.java.com.demo.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCUtils {
    private static Properties prop = new Properties();
    static{
        try {
            prop.load(new FileInputStream("useraccount/src/main/resources/db.properties"));
        } catch (IOException e) {
            System.out.println("加载配置文件错误！");
            throw new RuntimeException(e);
        }
    }
    static DataSource ds;

    static {
        try {
            ds = DruidDataSourceFactory.createDataSource(prop);
        } catch (Exception e) {
            System.out.println("数据库连接池配置失败！");
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() throws Exception {
        return ds.getConnection();
    }
    public static void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        if (rs != null) {
            try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        if (pstmt != null) {
            try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        if (conn != null) {
            try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}
