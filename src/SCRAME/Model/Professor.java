package SCRAME.Model;

/**
 * Professor object contains the details of professor
 * String name
 * String email
 * int contact
 * String office
 */
public class Professor {
	private String name;
	private String email;
	private int contact;
	private String office;

	/**
	 * Constructs a professor object,
	 * with details based on the parameters passed
	 */
	public Professor(String n, String e, int c, String o)  {

		name = n ;
		email = e ;
		contact = c ;
		office = o ;
	}
	
	public String getName() { return name ; }
	public int getContact() { return contact ; }
	public String getEmail() { return email ; }
	public String getOffice() { return office ; }

	/**
	 * returns true if the caller professor object is the same as the parameter 
	 * else return false
	 */
	public boolean equals(Object o) {
		if (o instanceof Professor) {
			Professor p = (Professor)o;
			return (getName().equals(p.getName()));
		}
		return false;
	}
}