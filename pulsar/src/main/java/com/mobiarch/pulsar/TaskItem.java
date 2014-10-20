package com.mobiarch.pulsar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimerTask;

public class TaskItem extends TimerTask {
	String type;
	String id;
	int frequency;
	Task task;
	Date startTime;

	public String getType() {
		return this.type;
	}

	public void setType(String type) throws Exception {
		if (type == null) {
			throw new Exception("type can not be absent.");
		}
		this.type = type.toLowerCase();
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) throws Exception {
		if (id == null) {
			throw new Exception("Task id can not be absent.");
		}
		this.id = id;
	}

	public int getFrequency() {
		return this.frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public void setFrequency(String frequency) throws Exception {
		if (frequency == null) {
			throw new Exception("frequency can not be absent.");
		}
		this.frequency = Integer.parseInt(frequency);
	}

	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date dt) {
		this.startTime = dt;
	}

	public void setStartTime(String startTime) throws Exception {
		if (startTime == null) {
			this.startTime = null;
		} else {
			try {
				SimpleDateFormat df = new SimpleDateFormat("HH:mm");

				df.setLenient(false);
				Date dt = df.parse(startTime); //Just get the hour and minute
				
				GregorianCalendar start = new GregorianCalendar();
				GregorianCalendar now = (GregorianCalendar) start.clone();
				GregorianCalendar tmpCal = new GregorianCalendar();
				
				tmpCal.setTime(dt); //Just has the hour and minute
				//Set the hour and minute
				start.set(Calendar.HOUR_OF_DAY, tmpCal.get(Calendar.HOUR_OF_DAY));
				start.set(Calendar.MINUTE, tmpCal.get(Calendar.MINUTE));
				start.set(Calendar.SECOND, 0);
				//Bring start time to future
				while (start.before(now)) {
					start.add(12, getFrequency());
				}
				
				setStartTime(start.getTime());
			} catch (ParseException pe) {
				throw new Exception("Start time must be in 24 hour time format: HH:mm. Example: 21:30");
			}
		}
	}

	public Task getTask() {
		return this.task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public void run() {
		try {
			this.task.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
