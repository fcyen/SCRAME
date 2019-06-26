package SCRAME.View;

import java.util.Scanner;
import SCRAME.Controller.CourseManager;
import SCRAME.Controller.LoginManager;
import SCRAME.Controller.StudentCourseManager;
import SCRAME.Controller.StudentManager;

public class UniversityApp
{
	public static void main(String[] args)
	{
		Scanner scan = new Scanner(System.in);
		String profile = null;
		int token;
		CourseManager coursemanager = CourseManager.initiate();
		StudentManager studentmanager = StudentManager.initiate();
		StudentCourseManager studentcoursemanager = StudentCourseManager.initiate();
		LoginManager loginmanager = LoginManager.initiate();

		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("           Welcome to NTU Course Registration System");
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		while(profile == null)
		{
			System.out.println("Please log in to your account");
			System.out.print("Matric number: ");
			String username = scan.next().toUpperCase();
			System.out.print("Password: ");
			profile = loginmanager.login(username, scan.next());

			if(profile != null && profile.equals("admin"))
			{
				System.out.println("------------------------------------------------------------------");
				System.out.println("             You are logged in as an administrator.");
				System.out.println("------------------------------------------------------------------\n");
				do
				{
					System.out.print("\n==================================================================\n");
					System.out.println("Main menu");
					System.out.println("1: Print all student list");
					System.out.println("2: Print all course list");
					System.out.println("3: Add a new student");
					System.out.println("4: Add a new course");
					System.out.println("5: Register student for a course");
					System.out.println("6: Check available slot in a class");
					System.out.println("7: Print student list by lecture, tutorial, or laboratory group");
					System.out.println("8: Enter coursework mark");
					System.out.println("9: Enter exam mark");
					System.out.println("10: Print course statistics");
					System.out.println("11: Print student transcript");
					System.out.println("12: Log out");
					System.out.println();
					System.out.print("Enter an option: ");
					token = IOE.scint();
					System.out.println("-----------------------------------\n\n");
					switch(token)
					{
						case 1: studentmanager.printStudents();
								break;
						case 2: coursemanager.printCourses();
								break;
						case 3: String sID = studentmanager.addStudent();
								if (sID != null)
								{
									studentmanager.printStudents();
									studentcoursemanager.updateStudentTM(sID); 
								}
								break;
						case 4: String cC = coursemanager.addCourse();
								if (cC != null)
								{
									coursemanager.printCourses();
									studentcoursemanager.updateCourseTM(cC);
								}
								break;
						case 5: studentcoursemanager.registerCourse(null);
								break;
						case 6: coursemanager.checkVacancy();
								break;
						case 7: studentcoursemanager.printStudentList();
								break;
						case 8: studentcoursemanager.inputCourseworkMark();
								break;
						case 9: studentcoursemanager.inputExamMark();
								break;
						case 10: studentcoursemanager.printCourseStatistics();
								break;
						case 11:studentcoursemanager.printStudentTranscript(null);
								break;
						case 12:profile = null;
								System.out.println("Log out successfully.");
								break;
						default:break;
					}
					System.out.print("\n==================================================================\n");
				}
				while(token != 12);
			}

			if(profile != null)
			{
				System.out.println("------------------------------------------------------------------");
				System.out.println("You are logged in as "+studentmanager.getStudent(profile).getName()+".");
				System.out.println("------------------------------------------------------------------\n");
				do
				{
					System.out.print("\n==================================================================\n");
					System.out.println("Main menu");
					System.out.println("1: Print all courses");
					System.out.println("2: Register for a course");
					System.out.println("3: Check available slot in a class");
					System.out.println("4: Print student list by lecture, tutorial, or laboratory group");
					System.out.println("5: Print course statistics");
					System.out.println("6: Print student transcript");
					System.out.println("7: Log out");
					System.out.println();
					System.out.print("Enter an option: ");
					token = IOE.scint();
					System.out.println("-----------------------------------\n\n");
					switch(token)
					{
						case 1: coursemanager.printCourses();
								break;
						case 2: studentcoursemanager.registerCourse(profile);
								break;
						case 3: coursemanager.checkVacancy();
								break;
						case 4: studentcoursemanager.printStudentList();
								break;
						case 5: studentcoursemanager.printCourseStatistics();
								break;
						case 6:studentcoursemanager.printStudentTranscript(profile);
								break;
						case 7:profile = null;
								System.out.println("Log out successfully.");
								break;
						default:break;
					}
					System.out.print("\n==================================================================\n");
				}
				while(token != 7);
			}
		}
	}
}