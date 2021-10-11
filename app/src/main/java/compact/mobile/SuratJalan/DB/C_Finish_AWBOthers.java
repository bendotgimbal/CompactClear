package compact.mobile.SuratJalan.DB;

public class C_Finish_AWBOthers {
	private String	waktu;
	private String	assigment;
	private String	customer;
	private String	username;
//	private String	kota;
	private String	locpk;
	
	public C_Finish_AWBOthers()
	{
		// TODO Auto-generated constructor stub
	}
	
	public C_Finish_AWBOthers(String waktu, String assigment, String customer, String username, String locpk)
	{
		super();
		
		this.waktu=waktu;
		this.assigment=assigment;
		this.customer=customer;
		this.username=username;
//		this.kota=kota;
		this.locpk=locpk;
	}
	
	public String getWaktu()
	{
		return waktu;
	}
	
	public void setWaktu(String waktu)
	{
		this.waktu = waktu;
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
	
//	public String getKota()
//	{
//		return kota;
//	}
//	
//	public void setKota(String kota)
//	{
//		this.kota = kota;
//	}
	
	public String getLocpk()
	{
		return locpk;
	}
	
	public void setLocpk(String locpk)
	{
		this.locpk = locpk;
	}
}
