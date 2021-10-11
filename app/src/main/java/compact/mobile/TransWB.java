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

public class TransWB
{
	private String	mswb_pk;
	private String	mswb_no;
	private String	customer;
	private String	shipper;
	private String	consignee;
	private String	org;
	private String	origin;
	private String	dest;
	private String	destination;
	private String	content;
	private String	qty;
	private String	kg;
	private String	status;
	
	public TransWB()
	{
		// TODO Auto-generated constructor stub
	}

	public TransWB(String mswb_pk, String mswb_no, String customer, String shipper, String consignee,String org,String  origin,String  dest,String  destination,String  content,String  qty,String  kg, String  status)
	{
		super();
		
		this.mswb_pk = mswb_pk;
		this.mswb_no = mswb_no;
		this.customer = customer;
		this.shipper = shipper;
		this.consignee = consignee;
		this.org = org;
		this.origin = origin;
		this.dest = dest;
		this.destination = destination;
		this.content = content;
		this.qty = qty;
		this.kg = kg;
		this.status = status;
	}

	public String getMswb_pk()
	{
		return mswb_pk;
	}
	public void setMswb_pk(String mswb_pk)
	{
		this.mswb_pk = mswb_pk;
	}
	
	public String getMswb_no()
	{
		return mswb_no;
	}
	public void setMswb_no(String mswb_no)
	{
		this.mswb_no = mswb_no;
	}
	
	public String getCustomer()
	{
		return customer;
	}
	public void setCustomer(String customer)
	{
		this.customer = customer;
	}
	public String getShipper()
	{
		return shipper;
	}
	public void setShipper(String shipper)
	{
		this.shipper = shipper;
	}
	public String getConsignee()
	{
		return consignee;
	}
	public void setConsignee(String consignee)
	{
		this.consignee = consignee;
	}
	public String getOrg()
	{
		return org;
	}
	public void setOrg(String org)
	{
		this.org = org;
	}
	public String getOrigin()
	{
		return origin;
	}
	public void setOrigin(String origin)
	{
		this.origin = origin;
	}
	public String getDest()
	{
		return dest;
	}
	public void setDest(String dest)
	{
		this.dest = dest;
	}
	public String getDestination()
	{
		return destination;
	}
	public void setDestination(String destination)
	{
		this.destination = destination;
	}
	public String getContent()
	{
		return content;
	}
	public void setContent(String content)
	{
		this.content = content;
	}
	public String getQTY()
	{
		return qty;
	}
	public void setQTY(String qty)
	{
		this.qty = qty;
	}
	public String getKG()
	{
		return kg;
	}
	public void setKG(String kg)
	{
		this.kg = kg;
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
