package com.timetable.connection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.timetable.properties.TimeTableProperty;

public class ConnectDB {
	
	private static Connection connection;
	
	public static synchronized Connection getConnection() throws SQLException, ClassNotFoundException, IOException{
		if(connection == null){
			TimeTableProperty timeTableProperty = new TimeTableProperty();
			String[] connectionArray = new String[3];
			connectionArray = timeTableProperty.getPropertyValues().split(",");
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			connection = DriverManager.getConnection(connectionArray[0],connectionArray[1],connectionArray[2]);
			return connection;
		}
		return connection;
	}

}
