package SCRAME.Controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import SCRAME.Model.Professor;

public class ProfessorManager {
	
	/**
	 * separator to standardise formatting and partitioning
	 */
	public static final String SEPARATOR = "|";
	
	/**
	 * pre-existing file consisting of data of professors
	 */
	private String filename = "../SCRAME/src/SCRAME/Database/professor.txt";
	
	
	protected ArrayList<Professor> al = new ArrayList<Professor>();
	
	/**
	 * initializes instance to null
	 * esentially restarting the file reading and Manager
	 */
	private static ProfessorManager theinstance = null;

	/**
	 * Constructor to initialize the class,
	 * private and is a singleton
	 */
	private ProfessorManager() {
		try {
			al = readProfessors(filename);
			if(al == null) al = new ArrayList<Professor>();
		} catch (IOException e) {
			System.out.println("IOException > " + e.getMessage());
		}
	}
	
	/**
	 * Calls the contructor if there is no current instance
	 */
	public static ProfessorManager initiate() {
		if(theinstance == null)
			theinstance = new ProfessorManager();
		return theinstance;
	}
	
	/**
	 * print list of professors on the file
	 */
	public void printProfessors () {
		for (int i = 0 ; i < al.size() ; i++) {
			Professor prof = (Professor)al.get(i);
			System.out.println((i+1) + "- " + prof.getName());
		}	
	}
	
	public Professor getProfessor(int i) {
		return al.get(i-1);
	}
	
	public String getProfName(int i) {
		return al.get(i-1).getName();
	}
	
	/**
	 * prints complete details of professor given the name of a professor
	 */
	public void printDetails(String n) {
		for (int i = 0 ; i < al.size() ; i++) {
			Professor cur = al.get(i);
			if (cur.getName().equals(n)) {
				System.out.println("- Course Coordinator: " + cur.getName());
				System.out.println("- Contact Number: " + cur.getContact());
				System.out.println("- Email: " + cur.getEmail());
				System.out.println("- Office: " + cur.getOffice());
				System.out.println();
			}
		}
	}
	
	/**
	 * function to read professor objects in a file and returns them
	 */
	public static ArrayList readProfessors(String filename) throws IOException {
		// read String from text file
		ArrayList stringArray = (ArrayList)read(filename);
		ArrayList alr = new ArrayList() ;// to store Professors data

        for (int i = 0 ; i < stringArray.size() ; i++) {
				String st = (String)stringArray.get(i);
				StringTokenizer star = new StringTokenizer(st , SEPARATOR);	// pass in the string to the string tokenizer using delimiter ","

				String  name = star.nextToken().trim();	// first token
				String  email = star.nextToken().trim();	// second token
				int  contact = Integer.parseInt(star.nextToken().trim()); // third token
				String office = star.nextToken().trim(); // fourth token
				Professor prof = new Professor(name, email,contact, office);
				// add to Professors list
				alr.add(prof) ;
			}
			return alr ;
	}
	
	/**
	 * reads a file of professor objects and returns a list
	 */
	public static List read(String fileName) throws IOException {
		List data = new ArrayList() ;
	    Scanner scanner = new Scanner(new FileInputStream(fileName));
	    try {
	      while (scanner.hasNextLine()){
	        data.add(scanner.nextLine());
	      }
	    }
	    finally{
	      scanner.close();
	    }
	    return data;
	  }
}
