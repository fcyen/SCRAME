package SCRAME.Model;

import java.io.Serializable;

public class StudentCourse implements Serializable {
	private String courseCode;
	
	/**
	 * This StudentCourse's tutorial group.
	 * "NA" to indicate that tutorial is not conducted for the course.
	 */
	private String tutGroup; // "NA" to indicate class type not conducted for the course
	
	/**
	 * This StudentCourse's lab group.
	 * "NA" to indicate that lab is not conducted for the course.
	 */
	private String labGroup;
	private String studentID;
	
	/**
	 * This StudentCourse's exam results. Initialized as -1.
	 */
	private int examResult; 
	
	/**
	 * This StudentCourse's coursework results stored in the same order as 
	 * courseworkComponent in the Course class.
	 * The 1st element is initialized as -1.
	 */
	private int[] courseworkResult;
	
	/**
	 * Constructor of StudentCourse. Initializes exam result and the 1st element of coursework
	 * result as -1. Creates an array of integers of size parameter size.
	 * @param sID This StudentCourse's student ID.
	 * @param cC This StudnetCourse's course code.
	 * @param tG This StudentCourse's tutorial group. "NA" if not applicable.
	 * @param lG This StudentCourse's lab group. "NA" if not applicable.
	 * @param size The number of coursework components in this StudentCourse.
	 */
	public StudentCourse(String sID, String cC, String tG, String lG, int size) 
	{
		studentID = sID;
		courseCode = cC;
		tutGroup = tG;
		labGroup = lG;
		examResult = -1;
		courseworkResult = new int[size];
		courseworkResult[0] = -1;
	}
	
	public String getCourseCode() 
	{
		return courseCode;
	}
	
	public String getStudentID() 
	{
		return studentID;
	} 
	
	public String getTutGroup() 
	{
		return tutGroup;
	}
	
	public String getLabGroup() 
	{
		return labGroup;
	}

	public int[] getCourseworkResult() 
	{
		return courseworkResult;
	}
	
	public int getExamResult() 
	{
		return examResult;
	}
	
	public void setExamResult(int mark) 
	{
		examResult = mark;
	}
	
	public void setCourseworkResult(int[] marks) 
	{
		courseworkResult = marks;
	}
	
	/**
	 * Overrides the equals method.
	 * Returns true if the course code and student ID matches.
	 */
	public boolean equals(Object o) {
		if (o instanceof StudentCourse) {
			StudentCourse sc = (StudentCourse)o;
			return (this.courseCode.equals(sc.getCourseCode()) && studentID.equals(sc.getStudentID()));
		}
		return false;
	}
}
