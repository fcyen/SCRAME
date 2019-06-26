package SCRAME.Controller;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import SCRAME.Model.*;
import SCRAME.View.IOE;

public class CourseManager
{
	protected ArrayList<Course> list = new ArrayList<Course>();
	/**
	 * dat file containing pre-exisitng records of Course objects.
	 */
	private String filename = "../SCRAME/src/SCRAME/Database/course.dat";
	private Scanner scan = new Scanner(System.in);
	
	/**
	 * courseCode is a string with 2 characters of abbreviation of course major in front, 
	 * followed by 4 numbers indicating their level and ID
	 */
	private String courseCode;
	
	/** 
	 * Stores the instance of CourseManager so that the same instance will be used throughout the program. 
	 */
	private static CourseManager theinstance = null;
	
	/** 
	 * Initiates ProfessorManager.
	 */
	private static ProfessorManager professormanager = ProfessorManager.initiate();

	/**
	 * Constructor of CourseManager.
	 * Catches exception and prints relevant message.
	 * Prints message when CourseManager is successfully created.
	 */
	private CourseManager()
	{
		System.out.println("Loading course data... Please wait...");
		try
		{
			list = (ArrayList) IOE.readSerializedObject(filename);
			if(list == null) list = new ArrayList<Course>();

		}
		catch(Exception e){System.out.println( "Exception CourseManager() >> "+e.getMessage());}
		System.out.println("Load course data, done.\n");
	}
	
	/**
	 * Initializes CourseManager.
	 * @return the instance of CourseManager if it has been initialized before.
	 * 		   Otherwise, instantiates a new CourseManager and stores in the attribute theinstance.
	 */
	public static CourseManager initiate()
	{
		if(theinstance == null)
			theinstance = new CourseManager();
		return theinstance;
	}

	/**
	 * Allows user to add a new course.
	 * The steps of a successful course addition are given below:
	 * - input course code (course code has to be 6 characters long and starts with 'CE')
	 * - input course name
	 * - select the course coordinator based on the list of Professors in the profesor.txt file.
	 * - indicate whether the course has lecture, tutorial and lab
	 * - input the vacancy of the course
	 * - input the tutorial and lab groups if applicable
	 * - input the exam and coursework weightages
	 * @return courseName if course if successfully added. null if adding is not successful.
	 */
	public String addCourse()
	{	
		String courseCode;
		String courseName;
		String coordinator;
		int[] numOfGroup = new int[3];  // [lec, tut, lab]
		Map <String, Integer> tutGroups = null;  // <index, vacancy>
		Map <String, Integer> labGroups = null;  // <index, vacancy>
		int overallVacancy;
		int examWeightage;
		Map <String, Integer> courseworkComponent = new Hashtable<String, Integer>();  // <type, weightage>
		int n;
		String temp;
		
		System.out.print("Adding course. ");
		Scanner read = new Scanner(System.in);
		System.out.println("Enter course code: ");
		courseCode = read.next().toUpperCase(); read.nextLine();
		while(courseCode.length() != 6 || courseCode.charAt(0) != 'C' || courseCode.charAt(1) != 'E')
		{
			System.out.println("Invalid course code. Please enter course code with 6 characters starting with 'CE'.");
			System.out.println("Re-enter course code: ");
			courseCode = read.next().toUpperCase(); read.nextLine();
		}
		System.out.println("Enter course name: ");
		courseName = read.nextLine().toUpperCase();
		System.out.println("Select course coordinator: ");
		professormanager.printProfessors();
		coordinator = professormanager.getProfName(IOE.scint());

		String[] ar = {"lecture", "tutorial", "lab"};
		for(int i = 0; i < 3; i++)
		{
			System.out.println("Does it have "+ar[i]+"? (yes/no)");
			temp = read.next().toLowerCase(); read.nextLine();
			while(!temp.equals("yes") && !temp.equals("y") && !temp.equals("no") && !temp.equals("n"))
			{
				System.out.print("Try again: ");
				temp = read.next().toLowerCase(); read.nextLine();
			}
			if(temp.equals("yes") || temp.equals("y")) numOfGroup[i] = 1;
			else numOfGroup[i] = 0;
		}
		
		System.out.print("Enter the total vacancy for this course: ");
		overallVacancy = IOE.scint();

		int k, l;
		boolean okflag;

		if(numOfGroup[1] != 0)
		{
			okflag = false;
			while(!okflag)
			{
				tutGroups = new Hashtable<String, Integer>();  // <index, vacancy>
				k = 0;
				System.out.print("Enter the number of tutorial groups available: ");
				n = IOE.scint();
				numOfGroup[1] = n;
				for(int i = 0; i < n; i++)
				{
					System.out.print("Please enter tutorial group "+(i+1)+" index: ");
					temp = read.next(); read.nextLine();
					System.out.print("Please enter tutorial group "+(i+1)+" vacancy: ");
					l = IOE.scint();
					tutGroups.put(temp.toUpperCase(), l);
					k += l;
				}
				if(k < overallVacancy)
				{
					System.out.println("Total vacancy of lab group must be not less than overall vacancy!");
					System.out.println("Please re-enter.");
				}
				else okflag = true;
			}
		}

		if(numOfGroup[2] != 0)
		{
			okflag = false;
			while(!okflag)
			{
				labGroups = new Hashtable<String, Integer>();  // <index, vacancy>
				k = 0;
				System.out.print("Enter the number of lab groups available: ");
				n = IOE.scint();
				numOfGroup[2] = n;
				for(int i = 0; i < n; i++)
				{
					System.out.print("Please enter lab group "+(i+1)+" index: ");
					temp = read.next(); read.nextLine();
					System.out.print("Please enter lab group "+(i+1)+" vacancy: ");
					l = IOE.scint();
					labGroups.put(temp.toUpperCase(), l);
					k += l;
				}
				if(k < overallVacancy)
				{
					System.out.println("Total vacancy of lab group must not be less than overall vacancy!");
					System.out.println("Please re-enter.");
				}
				else okflag = true;
			}
		}

		int total, lol;
		do
		{
			total = 0;
			System.out.print("Enter the exam weightage: "); 
			examWeightage = IOE.scint();
			while (examWeightage >= 100)
			{
				System.out.println("Exam weightage has to be smaller than 100. Please try again.");
				System.out.print("Enter the exam weightage: "); 
				examWeightage = IOE.scint();
			}
			System.out.println("Enter the number of coursework components in this course: ");
			n = IOE.scint();
			while (n < 1)
			{
				System.out.println("There should be at least one coursework component. Please try again.");
				System.out.println("Enter the number of coursework components in this course: ");
				n = IOE.scint();
			}
			if (n == 1) {
				courseworkComponent.put("Coursework", 100);
				total = 100;
			}
			else
				for(int i = 0 ;i < n; i++)
				{
					System.out.print("Key in the name of component "+(i+1)+": ");
					temp = read.nextLine();
					System.out.print("Key in the weightage of component "+(i+1)+": ");
					lol = IOE.scint();
					courseworkComponent.put(temp, lol);
					total += lol;
				}
			if(total != 100) System.out.print("Total percentage of coursework weightage must be equal to 100!"); 
		}
		while(total != 100);

		Course course = new Course(courseCode, courseName, coordinator, numOfGroup,
		 tutGroups,labGroups, overallVacancy, examWeightage, courseworkComponent);
		courseCode = course.getCourseCode();
		try
		{
			for(Course temp1:list)
				if(courseCode.equals(temp1.getCourseCode()))
					{System.out.println("Course already exist!"); return null;}
			list.add(course);
			IOE.writeSerializedObject(filename, list);
			System.out.println("Course succesfully added!");
			return courseCode;
		}
		catch ( Exception e ){System.out.println( "Exception addCourse() >> " + e.getMessage());}
		return null;
	}

	/**
	 * Allows admin to delete a course.
	 * Asks admin to input course code.
	 */
	public void deleteCourse()
	{
		System.out.print("Enter course code to delete: ");
		courseCode = scan.next().toUpperCase();
		for(Course temp: list)
			if(courseCode.equals(temp.getCourseCode()))
			{
				list.remove(temp);
				IOE.writeSerializedObject(filename, list);
				return;
			}
		System.out.println("Course not found!");
	}

	/**
	 * Allows user to check the vacancy of a course.
	 * Asks user to input course code.
	 * Prints out the overall vacancy and also the vacancy of tutorial and lab groups if applicable.
	 */
	public void checkVacancy()
	{
		System.out.print("Enter course code to check the vacancy: ");
		courseCode = scan.next().toUpperCase(); scan.nextLine();
		for(Course temp: list)
			if(courseCode.equals(temp.getCourseCode()))
			{
				System.out.println("Overall vacancy for "+courseCode+": "+temp.getOverallVacancy() + '/' + temp.getInitialVacancy());
				checkAvailableTutGroup(courseCode);
				checkAvailableLabGroup(courseCode);
				return;
			}
		System.out.println("Course not found!");
	}
	
	/**
	 * Gets the vacancy of a course.
	 * @param courseCode The Course's course code.
	 * @return Vacancy of the course. -1 to indicate that course is not found.
	 */
	public int getVacancy(String courseCode){ // for StudentCourse
		for(Course temp: list)
			if(courseCode.equals(temp.getCourseCode())) { 
				return temp.getOverallVacancy();
			}
		return -1;
	}
	
	/**
	 * Prints the available tutorial groups.
	 * @param courseCode The Course's coursecode.
	 * @return Map with tutorial group as the key and its vacancy as the value.
	 */
	public Map checkAvailableTutGroup(String courseCode) 
	{
		for(Course temp: list)
			if(courseCode.equals(temp.getCourseCode())){
				Map<String, Integer> groups = temp.getTutGroup();
				try {
					if (groups != null)
						System.out.println("\nTutorial groups");
					for(Map.Entry<String, Integer> e : groups.entrySet()) 
					{
						System.out.format("%-10s %-10s%n", e.getKey(), e.getValue());
					}
					return groups;
				}
				catch (Exception e) {}
			}
		return null;
	}
	
	/**
	 * Prints the available lab groups.
	 * @param courseCode The Course's course code.
	 * @return Map with lab group as the key and its vacancy as the value.
	 */
	public Map checkAvailableLabGroup(String courseCode) 
	{
		for(Course temp: list)
			if(courseCode.equals(temp.getCourseCode())){
				Map<String, Integer> groups = temp.getLabGroup();
				try {
					if (groups != null)
						System.out.println("\nLab groups");
					for(Map.Entry<String, Integer> e : groups.entrySet()) {
						System.out.format("%-10s %-10s%n", e.getKey(), e.getValue());
					}
					return groups;
				}
				catch (Exception e) {}
			}
		return null;
	}

	/**
	 * Gets the number of coursework component. 
	 * @param courseCode The Course's course code.
	 * @return The number of coursework component. 
	 */
	public int getNumOfComponent(String courseCode)
	{ // for StudentCourse
		for(Course temp: list)
			if(courseCode.equals(temp.getCourseCode())){
				return temp.getCourseworkComponent().size();
			}
		return -1;
	}

	/**
	 * Prints the course coordinator information.
	 * Asks user to input course code.
	 */
	public void showCoordinator()
	{
		System.out.print("Enter course code to show coordinator: ");
		courseCode = scan.next().toUpperCase();
		for(Course temp: list)
			if(courseCode.equals(temp.getCourseCode()))
				{System.out.println("Coordinator for "+courseCode+": "+temp.getCoordinator()); return;}
		System.out.println("Course not found!");
	}

	/**
	 * Prints out all the courses
	 */
	public void printCourses()
	{
		System.out.println("\nAll courses available");
		System.out.println("---------------------");
		try
		{
			for(Course temp:list) {
				System.out.println(temp);
				professormanager.printDetails(temp.getCoordinator());
			}
			System.out.println();
		}
		catch ( Exception e ){System.out.println( "Exception printCourses() >> " + e.getMessage());}
	}
	
	/**
	 * get all the tutorial group of this course
	 * @param courseCode the course code
	 * @return the list of tutorial group available in this course
	 */
	public Set getTutGroup (String courseCode)
	{ // for StudentCourse
		for(Course temp: list) {
			if(courseCode.equals(temp.getCourseCode())) 
				if (temp.getTutGroup() != null)
					return temp.getTutGroup().keySet();
				else
					return null;
		}
		return null;
	}

	/**
	 * get all the lab group of this course
	 * @param courseCode the course code
	 * @return the list of tutorial group available in this course
	 */
	public Set getLabGroup (String courseCode)
	{ // for StudentCourse
		for(Course temp: list) {
			if(courseCode.equals(temp.getCourseCode())) 
				if (temp.getLabGroup() != null)
					return temp.getLabGroup().keySet();
				else
					return null;
		}
		return null;
	}
	
	/**
	 * update the vacancy list of the course
	 * @param courseCode course code 
	 * @param tutGroup tutorial group
	 * @param labGroup lab group
	 */
	public void updateVacancy(String courseCode, String tutGroup, String labGroup)
	{ // for StudentCourse 
	  // "NA" for non tut or lab groups
		for(Course temp: list) {
			if(courseCode.equals(temp.getCourseCode())) {
				temp.setVacancy(temp.getOverallVacancy()-1);
				if (!tutGroup.equals("NA")) {
					if (temp.getTutGroup().containsKey(tutGroup)) {
						temp.setTutVacancy(tutGroup, (int) temp.getTutGroup().get(tutGroup)-1); 
					}
					else System.out.println("Invalid tutorial group.");
				}
				if (!labGroup.equals("NA")) {
					if (temp.getLabGroup().containsKey(labGroup)) {
						temp.setLabVacancy(labGroup, (int) temp.getLabGroup().get(labGroup)-1); 
					}
					else System.out.println("Invalid lab group.");
				}
				IOE.writeSerializedObject(filename, list);
			}
		}
	}
	
	/**
	 * get the exam weitage of this course
	 * @param courseCode the coursecode
	 * @return the exam weitage in integer type
	 */
	public int getExamWeightage(String courseCode)
	{
		for(Course temp: list)
			if(courseCode.equals(temp.getCourseCode())) 
				return temp.getExamWeightage();
		return -1;
	}

	/**
	 * get the course name of the course code given in parameter
	 * @param courseCode the course code
	 * @return the name of the course as string
	 */
	public String getCourseName(String courseCode)
	{
		for(Course temp: list)
			if(courseCode.equals(temp.getCourseCode())) 
				return temp.getCourseName();
		return "No such course.";
	}

	/**
	 * return the coursework component
	 * @param courseCode coursecode
	 * @returnreturn the coursework component odf this course
	 */
	public Map<String, Integer> getCourseworkComponent(String courseCode)
	{
		for(Course temp: list)
			if(courseCode.equals(temp.getCourseCode())) 
				return temp.getCourseworkComponent();
		return null;
	}

}
