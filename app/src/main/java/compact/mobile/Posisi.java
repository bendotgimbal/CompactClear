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

public class Posisi
{
	private String	lat_long;
	private String	username;
	private String	imei;
	private String 	time;
	private String 	status;
	public Posisi()
	{
		// TODO Auto-generated constructor stub
	}

	public Posisi(String imei, String username, String time, String lat_long, String status)
	{
		super();
		this.lat_long = lat_long;
		this.username = username;
		this.imei = imei;
		this.time = time;
		this.status = status;
	}

	public String getUser()
	{
		return username;
	}

	public void setUser(String user)
	{
		this.username = user;
	}

	public String getImei()
	{
		return imei;
	}

	public void setImei(String imei)
	{
		this.imei = imei;
	}

	public String getLat_Long()
	{
		return lat_long;
	}

	public void setLat_Long(String lat_long)
	{
		this.lat_long = lat_long;
	}
	

	public String getTime()
	{
		return time;
	}

	public void setTime(String time)
	{
		this.time = time;
	}	
	
	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}
}
