package serve;

import java.sql.*;
import java.util.ArrayList;

import serve.Setting;

public class SeekSql {
	Connection conn = null;
	Statement stmt = null;

	public SeekSql() {
		try {
			Class.forName(Setting.JDBC_DRIVER);
			conn = DriverManager.getConnection(Setting.DB_URL, Setting.USER, Setting.PASS);
			stmt = conn.createStatement();
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<String> GetList(Setting.Arears arear) {
		ArrayList<String> list = new ArrayList<String>();
		String sql = "SELECT name FROM Songslist WHERE arear = '" + arear.toString() + "';";
		try {
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				list.add(rs.getString("name"));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (list.size() > 0)
			return list;
		else
			return null;
	}

	public String GetSongPath(String name) {
		String sql = "SELECT song FROM Songslist WHERE name = '" + sqlStrCheck(name) + "';";
		try {
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			String p = rs.getString(1);
			rs.close();
			return p;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String GetIamgePath(String name)
	{
		String sql = "SELECT image FROM Songslist WHERE name = '" + sqlStrCheck(name) + "';";
		try {
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			String p = rs.getString(1);
			rs.close();
			return p;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<String> GetMessage(String name) {
		ArrayList<String> list = new ArrayList<String>();
		String sql = "SELECT album,artist FROM Songslist WHERE name = '" + sqlStrCheck(name) + "';";
		try {
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			list.add(rs.getString("album"));
			list.add(rs.getString("artist"));
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (list.size() > 0)
			return list;
		else
			return null;
	}
	
	public void close() {
		try {
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private String sqlStrCheck(String sql) {
		if (sql.contains("'")) {
			String[] t = sql.split("'");
			sql = t[0];
			for (int i = 1; i < t.length; i++) {
				sql += sql + "\\'" + t[i];
			}
		}
		return sql;
	}
}
