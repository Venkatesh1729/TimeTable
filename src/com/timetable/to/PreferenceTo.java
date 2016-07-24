package com.timetable.to;

public class PreferenceTo
{

	private int preferenceId;

	private int day;

	private int period;

	private boolean weeklyOnce;

	private boolean isAfterNoon;

	public int getPreferenceId() {
		return preferenceId;
	}

	public void setPreferenceId(int preferenceId) {
		this.preferenceId = preferenceId;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public boolean isWeeklyOnce() {
		return weeklyOnce;
	}

	public void setWeeklyOnce(boolean weeklyOnce) {
		this.weeklyOnce = weeklyOnce;
	}

	public boolean isAfterNoon() {
		return isAfterNoon;
	}

	public void setAfterNoon(boolean isAfterNoon) {
		this.isAfterNoon = isAfterNoon;
	}

}
