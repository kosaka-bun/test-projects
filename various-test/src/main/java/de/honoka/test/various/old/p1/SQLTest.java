package de.honoka.test.various.old.p1;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLTest {
	
	static Connection conn = null;
	static List<Watering> list = new ArrayList<>();

	public static synchronized boolean connect() {
		if(conn == null) {	//避免重复连接
			try {
				Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
				String url = "jdbc:mysql://xxx:3306/xxx?serverTimezone=GMT%2B8";
				String user = "root";
				String pwd = "xxx";
				conn = DriverManager.getConnection(url, user, pwd);
				return true;
			} catch(Exception e) {
				e.printStackTrace();
				return false;
			}
		} else return true;
	}
	
	public static void insertTest() {
		String sql = "INSERT INTO `watering` (`qq`,`level`,`nowExp`,`lastTimeWatering`) VALUES (?,?,?,?)";
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setLong(1, 10002L);
			ps.setInt(2, 1);
			ps.setInt(3, 88);
			java.util.Date nowTime = new java.util.Date();//util类型 
			Timestamp timeStamp = new Timestamp(nowTime.getTime());//Timestamp类型,timeDate.getTime()返回一个long型
			ps.setTimestamp(4, timeStamp);
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
