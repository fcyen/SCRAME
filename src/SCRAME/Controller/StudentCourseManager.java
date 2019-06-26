package SCRAME.Controller;

import java.util.Map;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import SCRAME.Model.*;
import SCRAME.View.IOE;
import SCRAME.Controller.*;


public class StudentCourseManager {
	/**
	 * ArrayList containing all StudentCourse objects stored in the dat file.
	 */
	private ArrayList<StudentCourse> list = new ArrayList<StudentCourse>();
	
	/**
	 * TreeMap that maps the course code to a list of indices pointing to the related StudentCourse records.
	 * Format: <courseCode, <Integer>>
	 */
	private TreeMap<String, LinkedList<Integer>> byCourse = new TreeMap<String, LinkedList<Integer>>();
	
	/**
	 * TreeMap that maps the student ID to a list of indices pointing to the related StudentCourse records.
	 * Format: <studnetID <Integer>>
	 */
	//
	private TreeMap<String, LinkedList<Integer>> byStudent = new TreeMap<String, LinkedList<Integer>>();
	private String filename = "../SCRAME/src/SCRAME/Database/StudentCourse.dat";
	private Scanner read = new Scanner(System.in);
	private static StudentCourseManager theinstance = null;
	private static CourseManager coursemanager = CourseManager.initiate();
	private static StudentManager studentmanager = StudentManager.initiate();

	/**
	 * Creates a new StudentCourseManager.
	 * Populates ArrayList list with all StudentCourse objects stored in the dat file.
	 * Populates TreeMap byCourse.
	 * Populates TreeMap byStudent.
	 */
	private StudentCourseManager()
	{ 
		System.out.println("Loading grades data... Please wait...");
			try	{
				list = (ArrayList) IOE.readSerializedObject(filename);
				if(list == null) list = new ArrayList<StudentCourse>();
		System.out.println("25%..");
				
				// from list create two TreeMap: one with courseID as key and another with studentID as key
				// add all courses to byCourse
				for (Course entry: coursemanager.list) {
					byCourse.put(entry.getCourseCode(), new LinkedList<Integer>());
				}
		System.out.println("50%..");
				
				// add all students to byStudent
				for (Student entry: studentmanager.list) {
					byStudent.put(entry.getID(), new LinkedList<Integer>());
				}
		System.out.println("75%..");
				
				// update indices of records
				for (int i = 0; i < list.size(); i++) {
					byCourse.get(list.get(i).getCourseCode()).add(i);
					byStudent.get(list.get(i).getStudentID()).add(i);
				}
		System.out.println("100%..");

		}  catch ( Exception e ) {
		 	System.out.println( "Exception >> " + e.getMessage() );
		}
		System.out.println("Load grades data, done.\n");
	}

	/**
	 * Returns StudentCourseManager instance.
	 * @return If StudentCourseManager has been instantiated, returns the same instance of StudentCourseManager.
	 * 		   Else, creates a new StudentCourseManager and returns it. Saves this instance to theinstance so 
	 * 		   that the same instance will be return each time initiate() is called.
	 */
	public static StudentCourseManager initiate() {
		if(theinstance == null)
			theinstance = new StudentCourseManager();
		return theinstance;
	}

	/**
	 * Register for a course.
	 * @param profile This StudentCourse's student ID. If null is passed into profile, the user will be asked to
	 * 				  input a valid student ID.
	 */
	public void registerCourse(String profile) {
		String student = profile;
		if(student == null)
			{
				studentmanager.printStudents();
				System.out.println("Enter student ID: ");
				student = read.next().toUpperCase(); read.nextLine();
			}
		coursemanager.printCourses();
		System.out.println("Enter course code: ");
		String course = read.next().toUpperCase(); read.nextLine();
		
		// check if student and course exists
		if (byCourse.containsKey(course) && byStudent.containsKey(student))
		{ 
			
			// check if student is already registered for the course
			LinkedList indexList = byCourse.get(course);
			for (int i = 0; i < indexList.size(); i++)
			{
				// if student is found in the course, cancel registration
				if (list.get((int) indexList.get(i)).getStudentID().equals(student))
				{ ;
					System.out.println("Student is already registered for this course.");
					return;
				}			
			}
			
			if (coursemanager.getVacancy(course) <= 0)
			{
				System.out.println("Course has no more vacancy.");
				return;
			}
			
			// student not registered yet, proceed with registration ---
			String tutg = "NA";
			String labg = "NA";
			boolean okflag = false;
			
			// check for available tutorial groups
			Map<String, Integer> tutGroup = coursemanager.checkAvailableTutGroup(course);
			if (tutGroup != null) {
				do 
				{
					System.out.println("Enter tutorial group: ");
					tutg = read.next().toUpperCase(); read.nextLine();
					if (!tutGroup.containsKey(tutg))
					{
						System.out.println("Invalid tutorial group. Please try again.");
						continue;
					}
					if (tutGroup.get(tutg) <= 0) 
					{
						System.out.println("This tutorial group is full. Please select another one.");
						continue;
					}
					okflag = true;
				}
				while(!okflag);
			}
			
			// check for available lab groups
			okflag = false;
			Map<String, Integer> labGroup = coursemanager.checkAvailableLabGroup(course);
			if (labGroup != null) {				
				do 
				{
					System.out.println("Enter lab group: ");
					labg = read.next().toUpperCase(); read.nextLine();
					if (!labGroup.containsKey(labg))
					{
						System.out.println("Invalid lab group. Please try again.");
						continue;
					}
					if (labGroup.get(labg) <= 0) 
					{
						System.out.println("This lab group is full. Please select another one.");
						continue;
					}
					okflag = true;
				}
				while(!okflag);
			}
			
			int n = coursemanager.getNumOfComponent(course);
			
			StudentCourse newSC = new StudentCourse(student, course, tutg, labg, n);
			// update master record
			list.add(newSC); 
			// update both TreeMaps
			byCourse.get(course).add(list.size()-1);
			byStudent.get(student).add(list.size()-1);
			// update dat file
			IOE.writeSerializedObject(filename, list);
			
			// update course vacancy
			coursemanager.updateVacancy(course, tutg, labg);
			
			System.out.println("Student successfully registered.");	
		}
		
		// student or course doesn't exist, cancel registration
		else
		{ 
			if (!byCourse.containsKey(course))
				System.out.println("Invalid course.");
			if (!byStudent.containsKey(student))
				System.out.println("Invalid student.");
		}
	}
	
	/**
	 * Prints out the list of students registered for a course.
	 * Asks user to input course code.
	 * User can choose to print all the students in the course or students in a particular tutorial or lab group.
	 */
	public void printStudentList() {
		System.out.println("Enter course code: ");
		String course = read.next().toUpperCase(); read.nextLine();
		String id;
		
		if (byCourse.containsKey(course)) {
			String group = "NA";
			LinkedList indexList = byCourse.get(course);
			int size = indexList.size();
			
			System.out.println("Select type of list");
			System.out.println("1: All students in the course");
			if (coursemanager.getTutGroup(course) != null)
				System.out.println("2: By tutorial group");
			if (coursemanager.getLabGroup(course) != null)
				System.out.println("3: By lab group");
			
			System.out.println();
			int choice = IOE.scint();
			String selected;
			switch(choice) {
			// print all
			case 1:
				System.out.println("List of students in " + course);
				for (int i = 0; i < size; i++) {
					id = list.get((int) indexList.get(i)).getStudentID();
					System.out.println(studentmanager.getStudent(id));
				}
				return;
			
			// by tutorial group
			case 2:
				// get a list of tutorial groups
				if (coursemanager.getTutGroup(course) != null) {
					Set<String> tutGroup = coursemanager.getTutGroup(course);
					System.out.println("Select tutorial group: ");
					// prints out all tutorial groups
					for (String key : tutGroup) {
						System.out.println(key);
					}
					read.nextLine();
					selected = read.nextLine().toUpperCase();
					while (!tutGroup.contains(selected))
					{
						System.out.println("Invalid tutorial group. Please try again.");
						System.out.println("Select tutorial group: ");
						selected = read.next().toUpperCase(); read.nextLine();
					}
				}
				else {
					System.out.println("Invalid selection.");
					return;
				}
				// print out names
				System.out.println("List of students in " + course + " tutorial group " + selected);
				for (int i = 0; i < size; i++) {
					if (list.get((int) indexList.get(i)).getTutGroup().equals(selected)) {
						id = list.get((int) indexList.get(i)).getStudentID();
						System.out.println(studentmanager.getStudent(id));
					}
				}
				return;
				
			// by lab group
			case 3:
				// get a list of lab groups
				if (coursemanager.getLabGroup(course) != null) {
					Set<String> labGroup = coursemanager.getLabGroup(course);
					System.out.println("Select lab group");
					for (String key : labGroup) {
						System.out.println(key);
					}
					read.nextLine();
					selected = read.nextLine().toUpperCase();
					while (!labGroup.contains(selected))
					{
						System.out.println("Invalid lab group. Please try again.");
						System.out.println("Select lab group: ");
						selected = read.next().toUpperCase(); read.nextLine();
					}
				}
				else {
					System.out.println("Invalid selection.");
					return;
				}
				// print out names
				System.out.println("List of students in " + course + " lab group " + selected);
				for (int i = 0; i < size; i++) {
					if (list.get((int) indexList.get(i)).getLabGroup().equals(selected)) {
						id = list.get((int) indexList.get(i)).getStudentID();
						System.out.println(studentmanager.getStudent(id));
					}
				}
				return;
				
			default: break;
			}
		}
		else {
			System.out.println("Invalid course ID.");
			return;
		}
	}
	
	/**
	 * Allows user to input a student's exam mark of a course.
	 * Asks user to input student ID and course code, then lets user input the exam mark.
	 * Updates the dat file after successful course registration.
	 */
	public void inputExamMark () {
		System.out.println("Enter student ID: ");
		String name = read.nextLine().toUpperCase();
		
		if (byStudent.containsKey(name)) {
			LinkedList indexList = byStudent.get(name);
			System.out.println("Enter course code: ");
			name = read.nextLine().toUpperCase();
			for (int i = 0; i < indexList.size(); i++) {
				if (list.get((int) indexList.get(i)).getCourseCode().equals(name)) {
					System.out.println("Enter exam mark: ");
					int mark = IOE.scint();
					while (mark < 0 || mark > 100) {
				    	System.out.println("Invalid mark. Please re-enter.");
				    	System.out.println("Enter exam mark: ");
					    mark = IOE.scint();
				    }
					list.get((int) indexList.get(i)).setExamResult(mark);
					IOE.writeSerializedObject(filename, list);
					System.out.println("Results saved.");
					return;
				}
			}	
			// if course not found
			System.out.println("Invalid course.");
		}
		else 
			// if student doesn't exist
			System.out.println("Invalid student.");
	}
	
	/**
	 * Allows user to input a student's coursework marks of a course.
	 * Asks user to input student ID and course code, then lets user input the coursework marks for each sub-component.
	 */
	public void inputCourseworkMark () {
		System.out.println("Enter student ID: ");
		String student = read.nextLine().toUpperCase();
		
		if (byStudent.containsKey(student)) {
			LinkedList indexList = byStudent.get(student);
			System.out.println("Enter course code: ");
			String course = read.next().toUpperCase(); read.nextLine();
			
			for (int i = 0; i < indexList.size(); i++) {
				// find course
				if (list.get((int) indexList.get(i)).getCourseCode().equals(course)) {
					// check if coursework component has been entered
					if (coursemanager.getCourseworkComponent(course) != null) {
						Map<String, Integer> componentWeightage = coursemanager.getCourseworkComponent(course);
						int[] marks = new int[componentWeightage.size()];
						int j = 0;
						int mark;
						
						// iterate through every component and ask for marks
						for (Map.Entry<String, Integer> entry : componentWeightage.entrySet()) {
						    System.out.println("Enter mark for " + entry.getKey() + ":  ");
						    mark = IOE.scint();
						    while (mark < 0 || mark > 100) {
						    	System.out.println("Invalid mark. Please re-enter.");
						    	System.out.println("Enter mark for " + entry.getKey() + ":");
							    mark = IOE.scint();
						    }
						    marks[j++] = mark;
						}
						list.get((int) indexList.get(i)).setCourseworkResult(marks);
						IOE.writeSerializedObject(filename, list);
						System.out.println("Results saved.");
						return;
					}
					System.out.println("No information about coursework component.");
					return;
				}
			}
			System.out.println("Invalid course.");
			return;
		}
		else {
			System.out.println("Invalid student.");
			return;
		}
	}
	
	/**
	 * Displays the percentage of student getting each grade.
	 * Asks user to input course code.
	 * Prints error message if exam or coursework marks are not entered yet and returns.
	 */
	public void printCourseStatistics() {
		System.out.println("Enter course code: ");
		String course = read.next().toUpperCase(); read.nextLine();
		int[] count = {0,0,0,0,0}; //[A,B,C,D,F]
		int mark, grade;
		
		if (byCourse.containsKey(course)) {
			LinkedList indexList = byCourse.get(course);
			System.out.println("Select type of statistics");
			System.out.println("1: Overall");
			System.out.println("2: Exams only");
			System.out.println("3: Coursework only");
			int choice = IOE.scint();
			
			switch(choice) {
			case 1: 
				for (int i = 0; i < indexList.size(); i++) 
				{
					mark = calTotalMarks(list.get((int) indexList.get(i)));
					if (mark == -1) {
						System.out.println("Mark for student " + list.get((int) indexList.get(i)).getStudentID() + " have not been entered yet.");
						return;
					}
					grade = convertToGrade(mark);
					if (grade > 0) {
						count[grade-1]++;
					}
				}
				break;
				
			case 2: 
				for (int i = 0; i < indexList.size(); i++) 
				{
					mark = list.get((int) indexList.get(i)).getExamResult();
					if (mark == -1) {
						System.out.println("Exam marks not entered yet.");
						return;
					}
					grade = convertToGrade(mark);
					if (grade > 0) {
						count[grade-1]++;
					}
				}
				break;
				
			case 3: 
				int sum = 0;
				for (int i = 0; i < indexList.size(); i++) {
					int[] marks = list.get((int) indexList.get(i)).getCourseworkResult();
					if (marks.length == 0) {
						System.out.println("Coursework marks not entered yet.");
						return;
					}
					for (int j = 0; j < marks.length; j++) {
						sum = sum + marks[j];
					}
					grade = convertToGrade(sum);
					if (grade > 0) {
						count[grade-1]++;
					}
				}
				break;
			}
			
			int numOfGrades = count[0] + count[1] + count[2] + count[3] + count[4];
			System.out.println("Statistics for " + course);
			System.out.println("A: " + (count[0]*100/numOfGrades) + "%");
			System.out.println("B: " + (count[1]*100/numOfGrades) + "%");
			System.out.println("C: " + (count[2]*100/numOfGrades) + "%");
			System.out.println("D: " + (count[3]*100/numOfGrades) + "%");
			System.out.println("F: " + (count[4]*100/numOfGrades) + "%");
		}
		
		else {
			System.out.println("Invalid course.");
			return;
		}
		
		
	}
	
	/**
	 * Displays student transcript containing student name, CPGA, grades and component marks for each course registered.
	 * Prints results not available message if marks have not yet been entered.
	 * @param profile The student's ID.
	 */
	public void printStudentTranscript(String profile) {
		String student = profile;
		if(student == null)
		{
			System.out.println("Enter student ID: ");
			student = read.next().toUpperCase(); read.nextLine();
		}

		int mark;
		String course;
		String[] grade = {"A", "B", "C", "D", "F"};
		StudentCourse sC;
		
		if (byStudent.containsKey(student)) {
			LinkedList indexList = byStudent.get(student);
			System.out.println("----------------------------------------------");
			System.out.println("Student Name: " + studentmanager.getStudentName(student));
			System.out.println("CGPA: " + calCGPA(student));
			
			// find all the courses the student registered for
			for (int i = 0; i < indexList.size(); i++) {
				sC = list.get((int) indexList.get(i));
				System.out.println();
				course = sC.getCourseCode();
				System.out.println("|" + course + ": " + coursemanager.getCourseName(course) + "|");
				mark = calTotalMarks(sC);
				int w = coursemanager.getExamWeightage(course);
				
				if (mark >= 0) {
					System.out.println("Overall Mark: " + mark);
					System.out.println("Grade: " + grade[convertToGrade(mark)-1]);	
					System.out.println();
					System.out.format("%-15s %-10s %-15s%n", "Component", "Mark", "Weightage");
					System.out.format("%-15s %-10s %-15s%n", "Exam", sC.getExamResult(), w);
					
					// for courseworks with sub-components
					if (sC.getCourseworkResult().length > 1) {
						Map<String, Integer> cwW = coursemanager.getCourseworkComponent(sC.getCourseCode());
						int[] cwR = sC.getCourseworkResult();
						int j = 0;
						System.out.println("Coursework");
						for (Map.Entry<String, Integer> entry : cwW.entrySet()) {
							System.out.print("   ");
							System.out.format("%-12s %-10s %-15s%n", entry.getKey(), cwR[j++], entry.getValue()*(100-w)/100);
						}	
					}
					// for courseworks as the main component only
					else {
						System.out.format("%-15s %-10s %-15s%n", "Coursework", sC.getCourseworkResult()[0], (100-w));
					}
				}
				else {
					System.out.println("Results for this course is not available yet");
				}
				System.out.println();
			}
			System.out.println("----------------------------------------------");
		}
		// student not found
		else System.out.println("Invalid student.");
	}
	
	/**
	 * Calculates the CGPA of a student
	 * @param student This student's ID. 
	 * @return This student's CGPA. -1 to indicate invalid student ID.
	 */
	public double calCGPA(String student) {
		double totalGPA = 0;
		int mark;
		int n = 0;
		if (byStudent.containsKey(student)) {
			LinkedList indexList = byStudent.get(student);
			for (int i = 0; i < indexList.size(); i++) {
				mark = calTotalMarks(list.get((int) indexList.get(i)));
				if (mark >= 0) {
					totalGPA = totalGPA + convertToGPA(mark);
					n++;
				}
			}
			return (totalGPA/n);
		}
		return -1;
	}
	
	/**
	 * Converts mark to grade.
	 * @param mark Marks to be converted. Only values within range 0-100 is accepted.
	 * @return Grades represented by a number.
	 * 		   1 to indicate grade A, 2 to indicate grade B, ... 5 to indicate grade F.
	 */
	private int convertToGrade(int mark) {
		if (mark >= 0 && mark <= 100) { // check if mark is in range
			if (mark >= 80) return 1;
			else if (mark >= 70) return 2;
			else if (mark >= 60) return 3;
			else if (mark >= 50) return 4;
			else return 5;
		}
		return -1; // invalid mark
	}
	
	/**
	 * Converts marks to GPA.
	 * @param mark Marks to be converted. Only values with range 0-100 is accepted.
	 * @return	This studentCourse's GPA. -1 to indicate invalid marks passed inside.
	 */
	private double convertToGPA(int mark) {
		if (mark >= 0 && mark <= 100) { // check if mark is in range
			if (mark >= 80) return 5.00;
			else if (mark >= 70) return 4.00;
			else if (mark >= 60) return 3.00;
			else if (mark >= 50) return 2.00;
			else return 0;
		}
		return -1; // invalid mark
	}
	
	/**
	 * Calculates the total marks of a student for a particular course. 
	 * Takes both exam mark and coursework marks adjusted according to its weightage into calculation.
	 * @param sC The StudentCourse object 
	 * @return	The total marks of this StudentCourse. -1 to indicate that marks data have not been entered yet.
	 */
	private int calTotalMarks(StudentCourse sC) {
		// check if marks have been entered
		if (sC.getExamResult() != -1 && sC.getCourseworkResult()[0] != -1) {
			int w = coursemanager.getExamWeightage(sC.getCourseCode());
			int totalMarks = (sC.getExamResult() * w)/ 100; // adjust exam mark according to weightage
			int[] cwR = sC.getCourseworkResult();
			int i = 0;
			Map<String, Integer> cwW = coursemanager.getCourseworkComponent(sC.getCourseCode());
			
			for (Map.Entry<String, Integer> entry : cwW.entrySet()) {
				// adjust component marks according to each weightage
				totalMarks = totalMarks + (cwR[i++] * (100-w) * entry.getValue())/10000;
			}
			return totalMarks;
		}
		else { // if not then grade is not calculated 
			return -1;
		}
	}
	
	/**
	 * Updates the byStudent TreeMap.
	 * @param student The student ID.
	 */
	public void updateStudentTM(String student) {
		byStudent.put(student, new LinkedList<Integer>());
	}
	
	/**
	 * Updates the byCourse TreeMap.
	 * @param course The course code.
	 */
	public void updateCourseTM(String course) {
		byCourse.put(course, new LinkedList<Integer>());
	}
	
}