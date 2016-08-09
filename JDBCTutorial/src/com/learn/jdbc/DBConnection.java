package com.learn.jdbc;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

	public static Connection getConnection() {
		Properties props = new Properties();
		FileInputStream fis = null;
		Connection con = null;
		try {
			fis = new FileInputStream("D:/myProjects/Workspace/JDBCTutorial/db.properties");
			props.load(fis);

			// load the Driver Class
			Class.forName(props.getProperty("DB_DRIVER_CLASS"));

			// create the connection now
			// myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo", "student", "student");
			con = DriverManager.getConnection(props.getProperty("DB_URL"), props.getProperty("DB_USERNAME"), props.getProperty("DB_PASSWORD"));
		}
		catch(IOException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return con;
	}
}
