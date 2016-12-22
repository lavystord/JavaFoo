package com.urent.server.utility;

/**
 * Created by Dell on 2015/10/12.
 */


import java.sql.DriverManager;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import com.mysql.jdbc.Connection;



public class DBTestSQL {
    private static Connection conn;
    public static void init() {

        connect2DB();
        querySQL();

    }

    public static void restore() {
        connect2DB();
        removeTestData();
    }

    private static void connect2DB() {
        try {
            Properties props = Resources.getResourceAsProperties("jdbc_test.properties");
            String url = props.getProperty("url");
            String driver = props.getProperty("driver");
            String username = props.getProperty("username");
            String password = props.getProperty("password");
//            System.out.println("url: "+url);
//            if(url.equals("jdbc:mysql://localhost:3306/serverdb?useUnicode=true&characterEncoding=utf8")) {
            Class.forName(driver).newInstance();
            conn = (Connection) DriverManager.getConnection(url, username, password);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void querySQL() {
        try {
            ScriptRunner runner = new ScriptRunner(conn);
            runner.setErrorLogWriter(null);
            runner.setLogWriter(null);
            runner.runScript(Resources.getResourceAsReader("SQL/ordinary_data.sql"));
            runner.runScript(Resources.getResourceAsReader("SQL/locktypeandversion.sql"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void  removeTestData() {
        try {
            ScriptRunner runner = new ScriptRunner(conn);
            runner.setErrorLogWriter(null);
            runner.setLogWriter(null);
            runner.runScript(Resources.getResourceAsReader("SQL/removeTestData.sql"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
