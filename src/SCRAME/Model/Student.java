package SCRAME.Model;

import java.io.Serializable;
import java.util.Scanner;

public class Student implements Serializable
{
	/**
	 * ID of student is a string starting with U for undergraduate,
	 * 2numbers representing year of admission, followed by the student ID
	 */
	private String studentID;	//matric saved in capital should not case sensitive
	
	/**
	 * full name of student separated by a single spacebar
	 * case sensitive
	 */
	private String name;		//case sensitive
	
	/**
	 * password is case snsitive
	 * no restrictions on password
	 */
	private String password;
	
	/**
	 * CGPA is initialized to 0 until udated by getCGPA() function
	 */
	private double CGPA = 0;

	/**
	 * constructor of student object, creates object based on the parameters passed
	 * student object holds some parmaeters
	 * String studentID
	 * String name
	 * String password
	 */
	public Student(String sID, String n, String p)
	{
		this.studentID = sID;
		this.name = n;
		this.password = p;
	}

	public String getID()
	{
		return this.studentID;
	}

	public String getName()
	{
		return this.name;
	}

	/**
	 * checks if the password entered is equal to the password of the caller student object
	 */
	public boolean checkPassword(String password)
	{
		if(this.password.equals(password)) return true;
		return false;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public double getCGPA()
	{
		return this.CGPA;
	}

	public void setCGPA(Double cgpa)
	{
		this.CGPA = cgpa;
	}

	/**
	 * overrides the toString method for this class
	 * used for standardisation of formatting
	 */
	@Override
	public String toString()
	{
		return name+" : "+studentID;
	}

}
