package com.timetable.properties;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TimeTableProperty {
	
	public String getPropertyValues() throws IOException{
		
		Properties properties = new Properties();
		String propertyFileName = "config.properties";
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propertyFileName);
		
		if(inputStream != null){
			properties.load(inputStream);
		}else {
			throw new FileNotFoundException("Property File :"+propertyFileName+" is missing");
		}
		
		return properties.getProperty("dbURL")+","+properties.getProperty("username")+","+properties.getProperty("password");
	
		
	}

}
