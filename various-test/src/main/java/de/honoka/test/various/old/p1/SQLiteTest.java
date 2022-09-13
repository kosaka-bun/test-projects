package de.honoka.test.various.old.p1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class SQLiteTest {

    static Connection conn;

    public static void main(String[] args) throws Exception {
        connect();
        String sql;
        sql = "create table if not exists `test`\n" +
                "(`col_a` varchar(10) primary key,\n" +
                "`col_b` varchar(10)\n);";
        Statement st = conn.createStatement();
        st.execute(sql);
        for(int i = 1; i <= 10; i++) {
            st.execute("insert into `test` values (" + i + "," + (i+1) + ")");
        }
    }

    public static synchronized boolean connect() {
        if(conn == null) {	//避免重复连接
            try {
                Class.forName("org.sqlite.JDBC");
                String url = "xxxxxxxxxxxx";
                conn = DriverManager.getConnection(url);
                return true;
            } catch(Exception e) {
                e.printStackTrace();
                return false;
            }
        } else return true;
    }
}
