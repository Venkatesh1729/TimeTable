package com.timetable.resource;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.google.gson.Gson;
import com.timetable.dao.TimeTableDAO;
import com.timetable.to.ClassTo;
import com.timetable.to.PreferenceTo;

public class TimeTableResourceHelper
{
	static int rowIndex = 0;

	private static Workbook workbook;

	public JSONObject saveClassDetails(HttpServletRequest request, JSONObject jsonObject) throws Exception {
		try {

			JSONObject object = new JSONObject();
			TimeTableDAO timeTableDAO = new TimeTableDAO();

			String className = (String) jsonObject.get("className");
			int noOfDays = jsonObject.getInt("noOfDays");
			int noOfPeriods = jsonObject.getInt("noOfPeriods");

			int insertedPK = timeTableDAO.saveClassDetails(className, noOfPeriods, noOfDays);
			object.put("id", insertedPK);
			object.put("message", "Class saved successfully");
			return object;

		} catch (Exception e) {
			throw e;
		}
	}

	public JSONObject saveTeacherDetails(HttpServletRequest request, JSONObject jsonObject) throws Exception {
		try {

			JSONObject object = new JSONObject();
			TimeTableDAO timeTableDAO = new TimeTableDAO();

			String teacherName = (String) jsonObject.get("teacherName");
			String subjectName = (String) jsonObject.get("subjectName");

			int subjectId = timeTableDAO.getSubjectId(subjectName);

			// Save subject in subject_info table
			if (subjectId == 0) {
				timeTableDAO.saveSubjectDetails(subjectName);
			}

			// Now save teacher details
			int insertedPK = timeTableDAO.saveTeacherDetials(teacherName, subjectName, subjectId);
			object.put("id", insertedPK);
			object.put("message", "Class saved successfully");
			return object;

		} catch (Exception e) {
			throw e;
		}
	}

	public JSONObject saveClassTeacherDetails(HttpServletRequest request, JSONObject jsonObject) throws Exception {
		try {

			JSONObject object = new JSONObject();
			TimeTableDAO timeTableDAO = new TimeTableDAO();

			int classId = jsonObject.getInt("classId");
			JSONArray teacherArray = jsonObject.getJSONArray("teacherArray");

			for (int i = 0; i < teacherArray.length(); i++) {
				int teacherId = teacherArray.getInt(i);
				timeTableDAO.saveClassTeacherDetails(classId, teacherId);
			}

			object.put("message", "Class saved successfully");
			return object;

		} catch (Exception e) {
			throw e;
		}
	}

	public JSONObject savePreference(HttpServletRequest request, JSONObject jsonObject) throws Exception {
		try {
			
			JSONObject object = new JSONObject();
			TimeTableDAO timeTableDAO = new TimeTableDAO();
			
			int classId = jsonObject.getInt("classId");
			int teacherId = jsonObject.getInt("teacherId");
			int dayId = 0;
			int periodId = 0;
			int weeklyOnce = 0;
			int foreAfterNooon = 0;
			int preferenceId = 0;
			
			ClassTo classTo = timeTableDAO.getClassDetails(classId);
			
			if(jsonObject.has("day")){
				dayId = jsonObject.getInt("day");
			}
			if(jsonObject.has("period")){
				periodId = jsonObject.getInt("period");
			}
			if(jsonObject.has("week")){
				dayId = randomWithRange(1, classTo.getNumberOfDays());
				weeklyOnce = 1;
			}
			if(jsonObject.has("fnan")){
				
				int temp = classTo.getNumberOfPeriods()%2;
				if(temp == 0){
					temp = classTo.getNumberOfPeriods()/2;
				}
				
				if(jsonObject.getInt("fnan") == 1){
					periodId = randomWithRange(1,temp);
					foreAfterNooon = 1;
				} else if(jsonObject.getInt("fnan") == 2){
					periodId = randomWithRange(temp+1,classTo.getNumberOfPeriods());
					foreAfterNooon = 2;
				}
			}
			
			preferenceId = timeTableDAO.savePreferenceDetails(dayId, periodId, weeklyOnce, foreAfterNooon);
			timeTableDAO.updateClassTeacherDetails(classId, teacherId, preferenceId);
			

			object.put("message", "Class saved successfully");
			return object;

		} catch (Exception e) {
			throw e;
		}
	}

	public JSONObject generateTimeTable(HttpServletRequest request, JSONObject jsonObject) throws Exception {
		try {

			JSONObject object = new JSONObject();

			TimeTableDAO timeTableDAO = new TimeTableDAO();

			Map<Integer, Map<Integer, Integer>> classTeacherpreferenceMap = timeTableDAO.getClassTeacherDetails();

			Random random = new Random();
			Map<Integer, Integer[][]> timeTableMap = new HashMap<Integer, Integer[][]>();

			// Get list of class from the class_teacher table and shuffle them
			List<Integer> classKeys = new ArrayList<Integer>(classTeacherpreferenceMap.keySet());
			Collections.shuffle(classKeys);

			// Iterate each class
			for (int classkey : classKeys) {

				// Get the number of days and periods for each class
				int row = timeTableDAO.classDaysMap.get(classkey);
				int column = timeTableDAO.classPeriodsMap.get(classkey);
				int i = 0;
				int j = 0;
				boolean reset = false;
				Integer[][] stringArray = new Integer[row][column];
				Integer[][] stringArrayDummy = null;

				// Get list of teacher from class_teacher table and shuffle them
				List<Integer> subjectKeys = new ArrayList<Integer>(classTeacherpreferenceMap.get(classkey).keySet());
				Collections.shuffle(subjectKeys);

				// Preference table for each class
				List<Integer> preferenceKeys = new ArrayList<Integer>(classTeacherpreferenceMap.get(classkey).values());
				if (preferenceKeys.size() > 0) {
					for (int preferenceKey : preferenceKeys) {
						if (preferenceKey > 0) {
							List<PreferenceTo> preferenceTos = timeTableDAO.getPreferenceDetail(preferenceKey);
							for (PreferenceTo preferenceTo : preferenceTos) {
								stringArray[preferenceTo.getDay() - 1][preferenceTo.getPeriod() - 1] = 1;
							}
						}
						timeTableMap.put(classkey, stringArray);
					}
				}

				for (int iterator = 0; iterator < column; iterator++) {

					stringArrayDummy = timeTableMap.get(classkey);

					if (stringArrayDummy[i][j] == null || stringArrayDummy[i][j] == 0) {

						if (iterator >= subjectKeys.size()) {
							int randomInteger = random.nextInt(subjectKeys.size());
							stringArray[i][j] = subjectKeys.get(randomInteger);
						} else {
							stringArray[i][j] = subjectKeys.get(iterator);
						}

						// Check whether same period is allocated for same teacher in some other class, if it so reset the for loop
						for (int classkeyCheck : classKeys) {
							if (timeTableMap.get(classkeyCheck) != null && classkeyCheck != classkey) {
								Integer[][] temp01 = timeTableMap.get(classkeyCheck);
								if (i < temp01.length && j < temp01[i].length && temp01[i][j] != null && temp01[i][j].equals(stringArray[i][j])) {
									j = 0;
									Collections.shuffle(subjectKeys);
									iterator = -1;
									reset = true;
								}
							}
						}
						if (reset) {
							reset = false;
							continue;
						}
					} else {
						System.out.println();
					}

					if (j < column && j != column - 1) {
						j++;
					} else if (j == column - 1 && i != row - 1) {
						i++;
						j = 0;
						Collections.shuffle(subjectKeys);
						iterator = -1;
					} else if (j == column - 1 && i == row - 1) {
						break;
					}
				}

				timeTableMap.put(classkey, stringArray);
			}
			
			if(jsonObject.has("fileName")){
				exportExcelFile(timeTableMap, timeTableDAO.classMap, timeTableDAO.subjectMap, timeTableDAO.teacherSubMap, timeTableDAO.subjectSubMap,jsonObject.getString("fileName"));
			}
			object.put("message", "Class saved successfully");
			return object;

		} catch (Exception e) {
			throw e;
		}
	}

	public JSONObject getAllClassTeacherDetails(HttpServletRequest request, JSONObject jsonObject) throws Exception {
		try {

			JSONObject object = new JSONObject();
			Gson gson = new Gson();

			TimeTableDAO timeTableDAO = new TimeTableDAO();
			timeTableDAO.getClassDetails();

			object.put("CLASS_ARRAY", new JSONObject(gson.toJson(timeTableDAO.classMap)));
			object.put("TEACHER_ARRAY", new JSONObject(gson.toJson(timeTableDAO.getTeacherSubjectDetails())));
			object.put("CLASS_TEACHER_ARRAY", new JSONObject(gson.toJson(timeTableDAO.getClassTeacherIds())));

			return object;

		} catch (Exception e) {
			throw e;
		}
	}

	public static void exportExcelFile(Map<Integer, Integer[][]> timeTableMap, Map<Integer, String> classMapMirror, Map<Integer, Integer> subjectMapMirror, Map<Integer, String> teacherSubMapMirror, Map<Integer, String> subjectSubMapMirror,String fileName) {

		workbook = new XSSFWorkbook();
		Sheet timeTableSheet = workbook.createSheet("Time Table");

		for (Map.Entry<Integer, Integer[][]> entry : timeTableMap.entrySet()) {

			createTableName(timeTableSheet, classMapMirror.get(entry.getKey()));
			createEmptyRow(timeTableSheet);

			Integer[][] renderTimeTable = entry.getValue();

			createFirstRow(timeTableSheet, renderTimeTable[0].length);

			for (int i = 0; i < renderTimeTable.length; i++) {

				int cellIndex = 0;

				Row row = timeTableSheet.createRow(rowIndex++);

				row.createCell(cellIndex++).setCellValue("Day " + (i + 1));

				for (int j = 0; j < renderTimeTable[i].length; j++) {

					row.createCell(cellIndex++).setCellValue(teacherSubMapMirror.get(renderTimeTable[i][j]) + "(" + subjectSubMapMirror.get(subjectMapMirror.get(renderTimeTable[i][j])) + ")");

				}
			}

			createEmptyRow(timeTableSheet);

		}

		FileOutputStream fos;
		try {
			fos = new FileOutputStream(fileName);
			workbook.write(fos);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void createTableName(Sheet timeTableSheet, String className) {

		Row row = timeTableSheet.createRow(rowIndex++);

		int cellIndex = 0;

		row.createCell(cellIndex++).setCellValue(className);

	}

	public static void createFirstRow(Sheet timeTableSheet, int size) {

		Row row = timeTableSheet.createRow(rowIndex++);

		int cellIndex = 0;

		row.createCell(cellIndex++).setCellValue("Day / Period");
		for (int i = 1; i <= size; i++) {
			row.createCell(cellIndex++).setCellValue("Period " + i);
		}

	}

	public static void createEmptyRow(Sheet timeTableSheet) {

		Row row = timeTableSheet.createRow(rowIndex++);

		int cellIndex = 0;

		row.createCell(cellIndex++);

	}
	
	int randomWithRange(int min, int max)
	{
	   int range = (max - min) + 1;     
	   return (int)(Math.random() * range) + min;
	}

}
