package compact.mobile.SuratJalan.DB;

public class C_PoOutstanding {
//	private String	STR_asigment;
	private String	STR_no_po;
	private String	STR_kode_customer;
	private String	STR_nama_customer;
	private String	STR_status;
	
//	private String	STR_waybill;
//	private String	STR_locpk;
//	private String	STR_lat_long;
//	private String	STR_username;
//	private String	STR_po;
//	private String	STR_assigment;
//	private String	STR_image;
//	private String	STR_waktu;
	
	private String	waybill;
	private String	locpk;
	private String	lat_long;
	private String	username;
	private String	po;
	private String	assigment;
	private String	image;
	private String	kota;
	private String	waktu;
	
	public C_PoOutstanding()
	{
		// TODO Auto-generated constructor stub
	}
	
	public C_PoOutstanding(String	waybill,String	locpk, String lat_long, String username, String po, String assigment, String image, String kota, String waktu)
	{
		super();
		
		this.waybill=waybill;
		this.locpk=locpk;
		this.lat_long=lat_long;
		this.username=username;
		this.po=po;
		this.assigment=assigment;
		this.image=image;
		this.kota=kota;
		this.waktu=waktu;
	}
	
	public String getWaybill()
	{
		return waybill;
	}
	
	public void setWaybill(String waybill)
	{
		this.waybill = waybill;
	}
	
	public String getLocpk()
	{
		return locpk;
	}
	
	public void setLocpk(String locpk)
	{
		this.locpk = locpk;
	}
	
	public String getLat_Long()
	{
		return lat_long;
	}
	
	public void setLat_Long(String lat_long)
	{
		this.lat_long = lat_long;
	}
	
	public String getUsername()
	{
		return username;
	}
	
	public void setUsername(String username)
	{
		this.username = username;
	}
	
	public String getPo()
	{
		return po;
	}
	
	public void setPo(String po)
	{
		this.po = po;
	}
	
	public String getAssigment()
	{
		return assigment;
	}
	
	public void setAssigment(String assigment)
	{
		this.assigment = assigment;
	}
	
	public String getImage()
	{
		return image;
	}
	
	public void setImage(String image)
	{
		this.image = image;
	}
	
	public String getKota()
	{
		return kota;
	}
	
	public void setKota(String kota)
	{
		this.kota = kota;
	}
	
	public String getWaktu()
	{
		return waktu;
	}
	
	public void setWaktu(String waktu)
	{
		this.waktu = waktu;
	}
}
