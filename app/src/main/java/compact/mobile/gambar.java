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

public class gambar
{
	private String	image;
	private String	name;
	private String	penerima;
	private String	status;
	
	public gambar()
	{
		// TODO Auto-generated constructor stub
	}

	public gambar(String image, String name, String penerima )
	{
		super();
		this.image = image;
		this.name = name;
		this.penerima = penerima;
	}

	public String getImage()
	{
		return image;
	}

	public void setImage(String image)
	{
		this.image = image;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getPenerima()
	{
		return penerima;
	}

	public void setPenerima(String penerima)
	{
		this.penerima = penerima;
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
