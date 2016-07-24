package com.timetable.properties;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.gson.Gson;

public class Sample1
{
	public static void main(String[] args) throws JSONException {
		
		Map<Integer, String> classMap = new HashMap<Integer, String>();
		classMap.put(1, "san");
		classMap.put(2, "sarn");
		
		Gson gson = new Gson();
		System.out.println(new JSONObject(gson.toJson(classMap)));
		
	}

}
