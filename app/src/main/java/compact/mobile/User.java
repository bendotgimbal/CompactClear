/*
 * (c) 2013 
 * @author Pratama Nur Wijaya
 * 
 * Project       : SQLiteTutorials
 * Filename      : Contact.java
 * Creation Date : Feb 23, 2013 time : 9:17:15 PM
 *
 */

package compact.mobile;

public class User
{
	private String	location;
	private String	username;
	private String	password;

	public User()
	{
		// TODO Auto-generated constructor stub
	}

	public User(String username, String password, String location )
	{
		super();
		this.location = location;
		this.username = username;
		this.password = password;
	}

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

}
