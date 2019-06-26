package SCRAME.Controller;

import java.util.ArrayList;
import SCRAME.Controller.*;
import SCRAME.Model.*;

/**
 * this class is responsible for identification and authorization purpose
 */
public class LoginManager
{
	protected ArrayList<Student> list;
	private static LoginManager theinstance = null;
	private static StudentManager studentmanager = StudentManager.initiate();

	private LoginManager()
	{
		list = studentmanager.list; //pointer to the real list
	}

	public static LoginManager initiate()
	{
		if(theinstance == null)
			theinstance = new LoginManager();
		return theinstance;
	}

	/**
	 * this method will identify the user based on the data in database
	 * @param username the username for the account
	 * @param password the password for the account
	 * @return the ID of the account if login successful, else return null
	 */
	public String login(String username, String password)
	{
		if(username.equals("ADMIN") && password.equals("admin"))
			return "admin";
		else
		{
			for(Student temp: list)
				if(username.equals(temp.getID()))
					if(temp.checkPassword(password))
						return temp.getID();
		}
		System.out.println("The username or password you provided is incorrect.");
		System.out.println("Please try again.");
		return null;
	}
}