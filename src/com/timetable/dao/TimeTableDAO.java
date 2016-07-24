package com.timetable.dao;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.timetable.connection.ConnectDB;
import com.timetable.to.ClassTo;
import com.timetable.to.PreferenceTo;
import com.timetable.to.SubjectTo;
import com.timetable.to.TeacherTo;

public class TimeTableDAO
{

	PreparedStatement preparedStatement;

	String sqlQuery;

	public Map<Integer, String> classMap = new HashMap<Integer, String>();

	public Map<Integer, Integer> classPeriodsMap = new HashMap<Integer, Integer>();

	public Map<Integer, Integer> classDaysMap = new HashMap<Integer, Integer>();

	public Map<Integer, String> subjectSubMap = new HashMap<Integer, String>();

	public Map<Integer, String> teacherSubMap = new HashMap<Integer, String>();

	public Map<Integer, Integer> subjectMap = new HashMap<Integer, Integer>();

	public int saveClassDetails(String className, int noOfPeriods, int noOfDays) throws Exception {
		try {
			int insertedPrimaryKey = 0;
			ConnectDB.getConnection().setAutoCommit(false);
			sqlQuery = "insert into class_info (class_name,no_of_periods,no_of_days)values (?,?,?)";
			preparedStatement = ConnectDB.getConnection().prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, className);
			preparedStatement.setInt(2, noOfPeriods);
			preparedStatement.setInt(3, noOfDays);
			preparedStatement.executeUpdate();
			ResultSet resultSet = preparedStatement.getGeneratedKeys();
			if (resultSet.next()) {
				insertedPrimaryKey = resultSet.getInt(1);
			}
			ConnectDB.getConnection().commit();
			return insertedPrimaryKey;
		} catch (Exception e) {
			System.err.println("Exception occured while saving class details");
			ConnectDB.getConnection().rollback();
			throw e;
		} finally {
			preparedStatement.close();
		}
	}

	public void saveSubjectDetails(String subjectName) throws ClassNotFoundException, SQLException, IOException {
		try {
			ConnectDB.getConnection().setAutoCommit(false);
			sqlQuery = "insert into subject_info (subject_name) values (?)";
			preparedStatement = ConnectDB.getConnection().prepareStatement(sqlQuery);
			preparedStatement.setString(1, subjectName);
			preparedStatement.executeUpdate();
			ConnectDB.getConnection().commit();
		} catch (Exception e) {
			System.err.println("Exception occured while saving subject details");
			ConnectDB.getConnection().rollback();
			throw e;
		} finally {
		}

	}

	public int saveTeacherDetials(String teacherName, String subjectName, int subjectId) throws ClassNotFoundException, SQLException, IOException {
		try {
			int insertedPrimaryKey = 0;
			ConnectDB.getConnection().setAutoCommit(false);
			sqlQuery = "insert into teacher_info (teacher_name,subject_id) values (?,?)";
			PreparedStatement statement = ConnectDB.getConnection().prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, teacherName);
			if (subjectId == 0) {
				statement.setInt(2, getSubjectId(subjectName));
			} else if (subjectId > 0) {
				statement.setInt(2, subjectId);
			}
			statement.executeUpdate();
			ResultSet resultSet = statement.getGeneratedKeys();
			if (resultSet.next()) {
				insertedPrimaryKey = resultSet.getInt(1);
			}
			ConnectDB.getConnection().commit();
			return insertedPrimaryKey;
		} catch (Exception e) {
			System.err.println("Exception occured while saving teacher details");
			ConnectDB.getConnection().rollback();
			throw e;
		} finally {
		}

	}

	public void saveClassTeacherDetails(int classId, int teacherId) throws ClassNotFoundException, SQLException, IOException {
		try {
			ConnectDB.getConnection().setAutoCommit(false);
			sqlQuery = "insert into class_teacher (class_id,teacher_id) values (?,?)";
			preparedStatement = ConnectDB.getConnection().prepareStatement(sqlQuery);
			preparedStatement.setInt(1, classId);
			preparedStatement.setInt(2, teacherId);
			preparedStatement.executeUpdate();
			ConnectDB.getConnection().commit();
		} catch (Exception e) {
			System.err.println("Exception occured while saving class and teacher details");
			ConnectDB.getConnection().rollback();
			throw e;
		} finally {
			preparedStatement.close();
		}

	}
	
	public int savePreferenceDetails(int dayId, int periodId, int weeklyOnce,int fnan) throws ClassNotFoundException, SQLException, IOException {
		try {
			int preferenceId = 0;
			ConnectDB.getConnection().setAutoCommit(false);
			sqlQuery = "insert into preference (day_id,period_id,weekly_once,fn_an) values (?,?,?,?)";
			PreparedStatement statement = ConnectDB.getConnection().prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, dayId);
			statement.setInt(2, periodId);
			statement.setInt(3, weeklyOnce);
			statement.setInt(4, fnan);
			statement.executeUpdate();
			ResultSet resultSet = statement.getGeneratedKeys();
			if (resultSet.next()) {
				preferenceId = resultSet.getInt(1);
			}
			ConnectDB.getConnection().commit();
			return preferenceId;
		} catch (Exception e) {
			System.err.println("Exception occured while saving preference details");
			ConnectDB.getConnection().rollback();
			throw e;
		} finally {
			preparedStatement.close();
		}

	}
	
	public void updateClassTeacherDetails(int classId, int teacherId,int preferenceId) throws ClassNotFoundException, SQLException, IOException {
		try {
			ConnectDB.getConnection().setAutoCommit(false);
			sqlQuery = "update class_teacher set preference_id = ? where class_id = ? and teacher_id = ?";
			preparedStatement = ConnectDB.getConnection().prepareStatement(sqlQuery);
			preparedStatement.setInt(1, preferenceId);
			preparedStatement.setInt(2, classId);
			preparedStatement.setInt(3, teacherId);
			preparedStatement.executeUpdate();
			ConnectDB.getConnection().commit();
		} catch (Exception e) {
			System.err.println("Exception occured while updating class and teacher details");
			ConnectDB.getConnection().rollback();
			throw e;
		} finally {
			preparedStatement.close();
		}

	}

	public int getSubjectId(String subjectName) throws ClassNotFoundException, SQLException, IOException {
		try {
			int subjectId = 0;
			sqlQuery = "select subject_id from subject_info where subject_name = ?";
			preparedStatement = ConnectDB.getConnection().prepareStatement(sqlQuery);
			preparedStatement.setString(1, subjectName);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				subjectId = resultSet.getInt("subject_id");
			}
			return subjectId;
		} catch (Exception e) {
			System.err.println("Exception occured while getting subject id");
			throw e;
		} finally {
		}
	}

	public List<Integer> getAssociatedTeacherIds(int classId) throws ClassNotFoundException, SQLException, IOException {
		try {
			List<Integer> teachersIdList = new ArrayList<Integer>();
			sqlQuery = "select teacher_id from class_teacher where class_id = ?";
			preparedStatement = ConnectDB.getConnection().prepareStatement(sqlQuery);
			preparedStatement.setInt(1, classId);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				teachersIdList.add(resultSet.getInt("subjectId"));
			}
			return teachersIdList;
		} catch (Exception e) {
			System.err.println("Exception occured while getting associated teacher's id");
			throw e;
		} finally {
			preparedStatement.close();
		}
	}

	public List<ClassTo> getClassDetails() throws ClassNotFoundException, SQLException, IOException {
		try {
			List<ClassTo> classTos = new ArrayList<ClassTo>();
			sqlQuery = "select class_id,class_name,no_of_periods,no_of_days from class_info";
			preparedStatement = ConnectDB.getConnection().prepareStatement(sqlQuery);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				ClassTo classTo = new ClassTo();
				classTo.setClassId(resultSet.getInt("class_id"));
				classTo.setClassName(resultSet.getString("class_name"));
				classTo.setNumberOfDays(resultSet.getInt("no_of_days"));
				classTo.setNumberOfPeriods(resultSet.getInt("no_of_periods"));
				classTos.add(classTo);
				classMap.put(resultSet.getInt("class_id"), resultSet.getString("class_name"));
				classPeriodsMap.put(resultSet.getInt("class_id"), resultSet.getInt("no_of_periods"));
				classDaysMap.put(resultSet.getInt("class_id"), resultSet.getInt("no_of_days"));
			}
			return classTos;
		} catch (Exception e) {
			System.err.println("Exception occured while getting class details");
			throw e;
		} finally {
		}
	}

	public List<SubjectTo> getSubjectDetails() throws ClassNotFoundException, SQLException, IOException {
		try {
			Map<Integer, String> subjectList = new HashMap<Integer, String>();
			List<SubjectTo> subjectTos = new ArrayList<SubjectTo>();
			sqlQuery = "select subject_id,subject_name from subject_info";
			preparedStatement = ConnectDB.getConnection().prepareStatement(sqlQuery);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				SubjectTo subjectTo = new SubjectTo();
				subjectTo.setSubjectId(resultSet.getInt("subject_id"));
				subjectTo.setSubjectName(resultSet.getString("subject_name"));
				subjectTos.add(subjectTo);
				subjectList.put(resultSet.getInt("subject_id"), resultSet.getString("subject_name"));
			}
			subjectSubMap = subjectList;
			return subjectTos;
		} catch (Exception e) {
			System.err.println("Exception occured while getting subject details");
			throw e;
		} finally {
		}
	}

	public List<TeacherTo> getTeacherDetails() throws ClassNotFoundException, SQLException, IOException {
		try {
			List<TeacherTo> teacherTos = new ArrayList<TeacherTo>();
			Map<Integer, String> teacherList = new HashMap<Integer, String>();
			sqlQuery = "select teacher_id,teacher_name,subject_id from teacher_info";
			preparedStatement = ConnectDB.getConnection().prepareStatement(sqlQuery);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				TeacherTo teacherTo = new TeacherTo();
				teacherTo.setTeacherId(resultSet.getInt("teacher_id"));
				teacherTo.setTeacherName(resultSet.getString("teacher_name"));
				teacherTo.setSubjectId(resultSet.getInt("subject_id"));
				teacherTos.add(teacherTo);
				teacherList.put(resultSet.getInt("teacher_id"), resultSet.getString("teacher_name"));
				subjectMap.put(resultSet.getInt("teacher_id"), resultSet.getInt("subject_id"));
			}
			teacherSubMap = teacherList;
			return teacherTos;
		} catch (Exception e) {
			System.err.println("Exception occured while getting teacher details");
			throw e;
		} finally {
		}
	}

	public Map<Integer, String> getTeacherSubjectDetails() throws ClassNotFoundException, SQLException, IOException {
		try {
			Map<Integer, String> teacherList = new HashMap<Integer, String>();
			sqlQuery = "select ti.teacher_id,ti.teacher_name,si.subject_name from teacher_info ti, subject_info si where ti.subject_id = si.subject_id";
			preparedStatement = ConnectDB.getConnection().prepareStatement(sqlQuery);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				teacherList.put(resultSet.getInt("teacher_id"), resultSet.getString("teacher_name") + "(" + resultSet.getString("subject_name") + ")");
			}
			return teacherList;
		} catch (Exception e) {
			System.err.println("Exception occured while getting teacher details");
			throw e;
		} finally {
			preparedStatement.close();
		}
	}

	public Map<Integer, Map<Integer, Integer>> getClassTeacherDetails() throws ClassNotFoundException, SQLException, IOException {
		try {

			Map<Integer, Map<Integer, Integer>> classTeacherpreferenceMap = new HashMap<Integer, Map<Integer, Integer>>();

			// Set Class,Teacher and Subject Maps
			List<ClassTo> classTos = getClassDetails();
			getSubjectDetails();
			getTeacherDetails();

			// Iterate each class to get associated teachers
			for (ClassTo classTo : classTos) {

				Map<Integer, Integer> innerMapForPreference = new HashMap<Integer, Integer>();

				sqlQuery = "select teacher_id,preference_id from class_teacher where class_id = ?";
				preparedStatement = ConnectDB.getConnection().prepareStatement(sqlQuery);
				preparedStatement.setInt(1, classTo.getClassId());
				ResultSet resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					innerMapForPreference.put(resultSet.getInt("teacher_id"), resultSet.getInt("preference_id"));
				}

				classTeacherpreferenceMap.put(classTo.getClassId(), innerMapForPreference);
			}

			return classTeacherpreferenceMap;

		} catch (Exception e) {
			System.err.println("Exception occured while getting teacher details" + e);
			throw e;
		} finally {
			preparedStatement.close();
		}

	}
	
	public Map<Integer, List<Integer>> getClassTeacherIds() throws ClassNotFoundException, SQLException, IOException {
		try {

			Map<Integer, List<Integer>> classTeacherpreferenceMap = new HashMap<Integer, List<Integer>>();

			// Set Class,Teacher and Subject Maps
			List<ClassTo> classTos = getClassDetails();
			getSubjectDetails();
			getTeacherDetails();

			// Iterate each class to get associated teachers
			for (ClassTo classTo : classTos) {

				List<Integer> listOfTeacherIds = new ArrayList<Integer>();

				sqlQuery = "select teacher_id from class_teacher where class_id = ?";
				preparedStatement = ConnectDB.getConnection().prepareStatement(sqlQuery);
				preparedStatement.setInt(1, classTo.getClassId());
				ResultSet resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					listOfTeacherIds.add(resultSet.getInt("teacher_id"));
				}

				classTeacherpreferenceMap.put(classTo.getClassId(), listOfTeacherIds);
			}

			return classTeacherpreferenceMap;

		} catch (Exception e) {
			System.err.println("Exception occured while getting teacher details" + e);
			throw e;
		} finally {
			preparedStatement.close();
		}

	}

	public List<PreferenceTo> getPreferenceDetail(int preferenceId) throws ClassNotFoundException, SQLException, IOException {
		try {
			List<PreferenceTo> preferenceTos = new ArrayList<PreferenceTo>();
			sqlQuery = "select day_id,period_id,weekly_once,fn_an from preference where preference_id = ?";
			preparedStatement = ConnectDB.getConnection().prepareStatement(sqlQuery);
			preparedStatement.setInt(1, preferenceId);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				PreferenceTo preferenceTo = new PreferenceTo();

				preferenceTo.setDay(resultSet.getInt("day_id"));
				preferenceTo.setPeriod(resultSet.getInt("period_id"));
				if (resultSet.getInt("weekly_once") == 1) {
					preferenceTo.setWeeklyOnce(true);
				} else {
					preferenceTo.setWeeklyOnce(false);
				}
				if (resultSet.getInt("fn_an") == 1) {
					preferenceTo.setAfterNoon(true);
				} else {
					preferenceTo.setAfterNoon(false);
				}
				preferenceTos.add(preferenceTo);
			}
			return preferenceTos;
		} catch (Exception e) {
			System.err.println("Exception occured while getting preference detail");
			throw e;
		} finally {
			preparedStatement.close();
		}
	}

	public void clearClassDetails() throws ClassNotFoundException, SQLException, IOException {
		try {

			sqlQuery = "select class_id from class_teacher";
			preparedStatement = ConnectDB.getConnection().prepareStatement(sqlQuery);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				sqlQuery = "delete from class_teacher";
				preparedStatement = ConnectDB.getConnection().prepareStatement(sqlQuery);
				preparedStatement.executeUpdate();

			}

			sqlQuery = "delete from class_info";
			preparedStatement = ConnectDB.getConnection().prepareStatement(sqlQuery);
			preparedStatement.executeUpdate();

		} catch (Exception e) {
			System.err.println("Exception occured while deleting class details");
			throw e;
		} finally {
			preparedStatement.close();
		}
	}

	public void clearTeacherDetails() throws ClassNotFoundException, SQLException, IOException {
		try {

		} catch (Exception e) {
			System.err.println("Exception occured while deleting class details");
			throw e;
		} finally {
			preparedStatement.close();
		}
	}

	public void clearClassTeacherDetails() throws ClassNotFoundException, SQLException, IOException {
		try {

			sqlQuery = "delete from class_info";
			preparedStatement = ConnectDB.getConnection().prepareStatement(sqlQuery);
			preparedStatement.executeUpdate();

		} catch (Exception e) {
			System.err.println("Exception occured while deleting class details");
			throw e;
		} finally {
			preparedStatement.close();
		}
	}

	public void clearPreference() throws ClassNotFoundException, SQLException, IOException {
		try {

			sqlQuery = "delete from class_info";
			preparedStatement = ConnectDB.getConnection().prepareStatement(sqlQuery);
			preparedStatement.executeUpdate();

		} catch (Exception e) {
			System.err.println("Exception occured while deleting class details");
			throw e;
		} finally {
			preparedStatement.close();
		}
	}

	public void clearsubjectDetails() throws ClassNotFoundException, SQLException, IOException {
		try {

			sqlQuery = "delete from class_info";
			preparedStatement = ConnectDB.getConnection().prepareStatement(sqlQuery);
			preparedStatement.executeUpdate();

		} catch (Exception e) {
			System.err.println("Exception occured while deleting class details");
			throw e;
		} finally {
			preparedStatement.close();
		}
	}

	public boolean getClassTeacherDetails(int classId, int teacherId) throws Exception {
		try {
			sqlQuery = "select class_id from class_teacher where class_id = ? and teacher_id = ?";
			preparedStatement = ConnectDB.getConnection().prepareStatement(sqlQuery);
			preparedStatement.setInt(1, classId);
			preparedStatement.setInt(2, teacherId);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				return true;
			}
			return false;
		} catch (Exception e) {
			System.err.println("Exception occured while getting subject id");
			throw e;
		} finally {
		}
	}
	
	public ClassTo getClassDetails(int classId) throws ClassNotFoundException, SQLException, IOException {
		try {
			ClassTo classTo = new ClassTo();
			sqlQuery = "select no_of_periods,no_of_days from class_info where class_id = ?";
			preparedStatement = ConnectDB.getConnection().prepareStatement(sqlQuery);
			preparedStatement.setInt(1,classId);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				classTo.setNumberOfPeriods(resultSet.getInt("no_of_periods"));
				classTo.setNumberOfDays(resultSet.getInt("no_of_days"));;
			}
			return classTo;
		} catch (Exception e) {
			System.err.println("Exception occured while getting subject details");
			throw e;
		} finally {
		}
	}

}
