package SCRAME.Model;

import java.io.Serializable;
import java.util.*;

public class Course implements Serializable
{
	/**
	 * courseCode is a string with 2 characters of abbreviation of course major in front, 
	 * followed by 4 numbers indicating their level and ID.
	 */
	private String courseCode;

	/**
	 * Name of the course to easily identify the course.
	 */
	private String courseName;

	/**
	 * Name of course coordinator. It is the attribute name in the Professor object.
	 */
	private String coordinator;

	/**
	 * Indicates whether the course has lecture, tutorial and lab.
	 * Number of groups are stored in the following order: [lecture, tutorial, lab]
	 * 0 indicates that it is not part of the course delivery structure of the course.
	 * Other positive integer indicates the number of groups.
	 */
	private int[] numOfGroup = new int[3]; 
	
	/**
	 * All the tutorial groups for this course in the format <index, vacancy>
	 * null if this course does not have tutorials.
	 */
	private Map <String, Integer> tutGroups = new Hashtable<String, Integer>();
	
	/** 
	 * All the lab groups for this course in the format <index, vacancy>
	 * null if this course does not have labs.
	 */
	private Map <String, Integer> labGroups = new Hashtable<String, Integer>();
	
	/**
	 * Number of available slots left for this course.
	 */
	private int overallVacancy;
	
	/**
	 * Capacity of this course.
	 */
	private int initialVacancy;
	
	/**
	 * The exam weightage of this course out of 100.
	 */
	private int examWeightage;
	
	/**
	 * The coursework components of this course and its respective weightage in the format <sub-component, weightage>
	 * If this course only has one main coursework, courseworkComponent only contains for key-value pair <"Coursework", 100>
	 */
	private Map <String, Integer> courseworkComponent = new Hashtable<String, Integer>();  // <type, weightage>


	public Course(String courseCode,String courseName,String coordinator,int[] numOfGroup,Map <String, Integer> tutGroups,Map <String, Integer> labGroups,int overallVacancy,int examWeightage, Map <String, Integer> courseworkComponent)
	{
		this.courseCode = courseCode;
		this.courseName = courseName;
		this.coordinator = coordinator;
		this.numOfGroup = numOfGroup;
		this.tutGroups = tutGroups;
		this.labGroups = labGroups;
		this.overallVacancy = overallVacancy;
		this.initialVacancy = overallVacancy;
		this.examWeightage = examWeightage;
		this.courseworkComponent = courseworkComponent;
	}

	/**
	 * get the course code of this course
	 * @return this course's code in String type
	 */
	public String getCourseCode() {
		return courseCode;
	}
	
	/**
	 *get the course name of this course
	 *@return this course's name in String type
	 */
	public String getCourseName() {
		return courseName;
	}

	/**
	 * get the overall available vacancy of this course
	 * @return this course's overall vacancy in int type
	 */
	public int getOverallVacancy() {
		return overallVacancy;
	}
	
	/**
	 * get the initial vacancy of this course
	 * @return this course's initial vacancy in int type
	 */
	public int getInitialVacancy() {
		return initialVacancy;
	}

	/**
	 * get the coordinator of this course
	 * @return this course's coordinator name in String type
	 */
	public String getCoordinator() {
		return coordinator;
	}

	/**
	 * get all the tutorial group available of this course
	 * @return this course's list of tutorial groups in Map structure
	 */
	public Map getTutGroup() {
		return tutGroups;
	}

	/**
	 * get all the lab group available of this course
	 * @return this course's list of lab groups in Map structure
	 */
	public Map getLabGroup() {
		return labGroups;
	}

	/**
	 * get the number of both tutorial and lab groups available of this course
	 * @return this course's number of group for both lab and tutorial in array of int type
	 */
	public int[] getNumOfGroup() {
		return numOfGroup;
	}

	/**
	 * get the coursework component of this course
	 * @return this course's coursework component in Map structure
	 */
	public Map<String, Integer> getCourseworkComponent() {
		return courseworkComponent;
	}

	/**
	 * get the exam weightage of this course
	 * @return this course's exam weightage in int type
	 */
	public int getExamWeightage() {
		return examWeightage;
	}
	
	/**
	 * set the initial overall vacancy for this course
	 * @param v this course's vacancy in int type
	 */
	public void setVacancy(int v) {
		overallVacancy = v;
	}
	
	/**
	 * set the tutorial's vacancy of this course
	 * @param t the name of the tutorial group
	 * @param v the vacancy of the tutorial group
	 */
	public void setTutVacancy(String t, int v) {
		tutGroups.put(t, v);
	}
	
	/**
	 * set the lab vacancy of this course
	 * @param l the name of the lab group
	 * @param v the vacancy of the lab group
	 */
	public void setLabVacancy(String l, int v) {
		labGroups.put(l, v);
	}

	/**
	 * @return the course instance in the format of " courseCode : courseName;"
	 */
	@Override
	public String toString(){
		return courseCode+" : "+courseName;
	}
}