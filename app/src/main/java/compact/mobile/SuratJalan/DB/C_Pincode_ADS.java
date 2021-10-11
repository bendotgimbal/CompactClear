package compact.mobile.SuratJalan.DB;

public class C_Pincode_ADS {
	private String	assigment;
	private String	customer;
	private String	username;
	private String	locpk;
	private String	pincode;
	private String	lat_long;
	private String	waktu;
	private String	waybill;
	private String	kota;
	
	public C_Pincode_ADS()
	{
		// TODO Auto-generated constructor stub
	}
	
	public C_Pincode_ADS(String assigment, String customer, String username, String locpk, String pincode, String lat_long, String waktu, String waybill, String kota)
	{
		super();
		
		this.assigment=assigment;
		this.customer=customer;
		this.username=username;
		this.locpk=locpk;
		this.pincode=pincode;
		this.lat_long=lat_long;
		this.waktu=waktu;
		this.waybill=waybill;
		this.kota=kota;
	}
	
	public String getAssigment()
	{
		return assigment;
	}
	
	public void setAssigment(String assigment)
	{
		this.assigment = assigment;
	}
	
	public String getCustomer()
	{
		return customer;
	}
	
	public void setCustomer(String customer)
	{
		this.customer = customer;
	}
	
	public String getUsername()
	{
		return username;
	}
	
	public void setUsername(String username)
	{
		this.username = username;
	}
	
	public String getLocpk()
	{
		return locpk;
	}
	
	public void setLocpk(String locpk)
	{
		this.locpk = locpk;
	}
	
	public String getPincode()
	{
		return pincode;
	}
	
	public void setPincode(String pincode)
	{
		this.pincode = pincode;
	}
	
	public String getLat_Long()
	{
		return lat_long;
	}
	
	public void setLat_Long(String lat_long)
	{
		this.lat_long = lat_long;
	}
	
	public String getWaktu()
	{
		return waktu;
	}
	
	public void setWaktu(String waktu)
	{
		this.waktu = waktu;
	}
	
	public String getWaybill()
	{
		return waybill;
	}
	
	public void setWaybill(String waybill)
	{
		this.waybill = waybill;
	}
	
	public String getKota()
	{
		return kota;
	}
	
	public void setKota(String kota)
	{
		this.kota = kota;
	}
}
