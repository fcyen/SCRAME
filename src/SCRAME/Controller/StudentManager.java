package SCRAME.Controller;

import java.util.ArrayList;
import java.util.Scanner;
import SCRAME.Model.Student;
import SCRAME.View.IOE;

public class StudentManager
{ //this is a control class
	
	protected ArrayList<Student> list = new ArrayList<Student>();
	
	/**
	 * Path to the dat file storing Student's data.
	 */
	private String filename = "../SCRAME/src/SCRAME/Database/student.dat";
	private Scanner scan = new Scanner(System.in);
	
	/**
	 * A string of identification number for Student which encodes some details
	 * of the students.
	 * studentID is not case-sensitive.
	 */
	private String studentID;
	
	/**
	 * Instance of StudentManager class.
	 * null if StudentManager have not been constructed yet.
	 */
	private static StudentManager theinstance = null;

	/**
	 * Constructor of StudentManager.
	 * Catches exception and prints relevant message.
	 * Prints message when StudentManager is successfully created.
	 */
	private StudentManager()
	{
		System.out.println("Loading student data... Please wait...");
		try
		{
			list = (ArrayList<Student>) IOE.readSerializedObject(filename);
			if(list == null) list = new ArrayList<Student>();
		}
		catch(Exception e){System.out.println( "Exception StudentManager() >> "+e.getMessage());}
		System.out.println("Load student data, done.\n");
	}

	/**
	 * Initializes StudentManager.
	 * @return the instance of StudentManager if it has been initialized before.
	 * 		   Otherwise, instantiates a new StudentManager and stores in the attribute theinstance.
	 */
	public static StudentManager initiate() 
	{
		if(theinstance == null)
			theinstance = new StudentManager();
		return theinstance;
	}

	/**
	 * Allows user to add student.
	 * Asks user for student's information: ID, name and password.
	 * ID has to be at least 9 characters long and starts with a 'U'.
	 * Updates the dat file after student is successfully added.
	 * @return Name of the student added. null if student is not successfully added.
	 */
	public String addStudent()
	{
		System.out.print("Adding student. ");
		System.out.println("Enter the student ID: ");
		String sID = scan.next().toUpperCase(); scan.nextLine();
		while (sID.length() < 9 || sID.charAt(0) != 'U')
		{
			System.out.println("Invalid student ID. Please enter ID of at least 9 characters long starting with 'U'.");
			System.out.println("Re-enter student ID: ");
			sID = scan.next().toUpperCase(); scan.nextLine();
		}
		System.out.println("Enter the student name: ");
		String n = scan.nextLine();
		System.out.println("Enter a temporary password: ");
		String p = scan.nextLine();
		Student student = new Student(sID, n, p);
		studentID = student.getID();
		try
		{
			for(Student temp:list)
				if(studentID.equals(temp.getID()))
				{
					System.out.println("Student already exist."); 
					return null;
				}
			list.add(student);
			IOE.writeSerializedObject(filename, list);
			System.out.println("Successfully added student.");
			return sID;
		}
		catch ( Exception e ){
			System.out.println( "Exception addStudent() >> " + e.getMessage());
		}
		return null;
	}

	/**
	 * Allows user to delete a student.
	 * Asks user to enter student ID.
	 * Prints error message if student not found.
	 */
	public void deleteStudent()
	{
		System.out.print("Enter student matric to delete: ");
		studentID = scan.next().toUpperCase();
		for(Student temp: list)
			if(studentID.equals(temp.getID()))
			{
				list.remove(temp);
				IOE.writeSerializedObject(filename, list);
				return;
			}
		System.out.println("Student not found.");
	}
	
	/**
	 * Allows user to retrieve student's details by entering student ID.
	 * @param profile This student's ID.
	 * @return The Student object.
	 */
	public Student getStudent(String profile)
	{
		studentID = profile;
		if(studentID == null)
		{
			System.out.print("Enter student matric to show info: ");
			studentID = scan.next().toUpperCase(); 
		}
		for(Student temp: list)
			if(studentID.equals(temp.getID()))
				return temp;
		System.out.println("Student not found.");
		return null;
	}
	
	/**
	 * Gets the student name of a student ID.
	 * @param sID Student's student ID.
	 * @return Student's student name. "NA" if student is not found.
	 */
	public String getStudentName(String sID)
	{
		for(Student temp: list)
			if(sID.equals(temp.getID()))
				return temp.getName();
		System.out.println("Student not found.");
		return "NA";
	}

	/**
	 * Prints list of all students in the system.
	 * Catches exception and prints out relevant error message.
	 */
	public void printStudents()
	{
		System.out.println("\nComplete list of students");
		System.out.println("-------------------------");
		try
		{
			for(Student temp:list)
				System.out.println(temp);
			System.out.println();
		}
		catch ( Exception e ){System.out.println( "Exception printStudents() >> " + e.getMessage());}
	}
}
