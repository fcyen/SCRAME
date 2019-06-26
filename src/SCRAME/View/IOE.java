package SCRAME.View;

import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import SCRAME.Model.*;

/**
 * this method asking for a valid integer input 
 * and print an appropriate warning message for invalid input
 */
public class IOE
{
	public static int scint()
	{
		Scanner scan = new Scanner(System.in);
		while(!scan.hasNextInt())
		{
			scan.nextLine();
			System.err.println("Integers only, please.");
		}
		return scan.nextInt();
	}

	/**
	 * read the serialized data from a given file name
	 * @param filename going to be read
	 * @return pDetails, the data from the file
	 */
	public static ArrayList readSerializedObject(String filename) {
		ArrayList pDetails = null;
		FileInputStream fis = null;
		ObjectInputStream in = null;
		try {
			fis = new FileInputStream(filename);
			in = new ObjectInputStream(fis);
			pDetails = (ArrayList) in.readObject();
			in.close();
		} catch (IOException ex) {
			// ex.printStackTrace();
			System.out.println("Database empty.");
		} catch (ClassNotFoundException ex) {
			// ex.printStackTrace();
			System.out.println("Database error.");
		}
		// print out the size
		// System.out.println(" Details Size: " + pDetails.size());
		//System.out.println();
		return pDetails;
	}

	/**
	 * write the object to the serialized file
	 * @param filename the file going to be written
	 * @param list 
	 */
	public static void writeSerializedObject(String filename, ArrayList list) {
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try {
			fos = new FileOutputStream(filename);
			out = new ObjectOutputStream(fos);
			out.writeObject(list);
			out.close();
		//	System.out.println("Object Persisted");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}