package compact.mobile.SuratJalan.DB;

public class C_POD {

	private String	waybill;
	private String	locpk;
	private String	lat_long;
	private String	username;
	private String	image;
	private String	kota;
	private String	assigment;
	private String	keterangan;
	private String	penerima;
	private String	tiperem;
	private String	telp;
	private String	waktu;
	
	public C_POD()
	{
		// TODO Auto-generated constructor stub
	}
	
	public C_POD(String waybill,String locpk, String lat_long, String username, String image, String assigment, String keterangan, String penerima, String tiperem, String telp, String waktu)
	{
		super();
		
		this.waybill=waybill;
		this.locpk=locpk;
		this.lat_long=lat_long;
		this.username=username;
		this.image=image;
//		this.kota=kota;
		this.assigment=assigment;
		this.keterangan=keterangan;
		this.penerima=penerima;
		this.tiperem=tiperem;
		this.telp=telp;
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
	
	public String getImage()
	{
		return image;
	}
	
	public void setImage(String image)
	{
		this.image = image;
	}

//	public String getKota()
//	{
//		return kota;
//	}
//	
//	public void setKota(String kota)
//	{
//		this.kota = kota;
//	}

	public String getAssigment()
	{
		return assigment;
	}
	
	public void setAssigment(String assigment)
	{
		this.assigment = assigment;
	}

	public String getKeterangan()
	{
		return keterangan;
	}
	
	public void setKeterangan(String keterangan)
	{
		this.keterangan = keterangan;
	}

	public String getPenerima()
	{
		return penerima;
	}
	
	public void setPenerima(String penerima)
	{
		this.penerima = penerima;
	}

	public String getTiperem()
	{
		return tiperem;
	}
	
	public void setTiperem(String tiperem)
	{
		this.tiperem = tiperem;
	}

	public String getTelp()
	{
		return telp;
	}
	
	public void setTelp(String telp)
	{
		this.telp = telp;
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
