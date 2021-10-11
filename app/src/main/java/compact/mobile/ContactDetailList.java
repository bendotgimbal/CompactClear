
package compact.mobile;

public class ContactDetailList
{
	private String	waybill;
	private String	tanggal;
	private String	asigment;
	private String	jenis;

	public ContactDetailList()
	{
		// TODO Auto-generated constructor stub
	}

	public ContactDetailList(String waybill, String tanggal)
	{
		super();
		this.waybill = waybill;
		this.tanggal = tanggal;

	}

	public String getWaybill()
	{
		return waybill;
	}

	public void setWaybill(String waybill)
	{
		this.waybill = waybill;
	}

	public String getAsigment()
	{
		return asigment;
	}

	public void setAsigment(String asigment)
	{
		this.asigment = asigment;
	}
	public String getTanggal()
	{
		return tanggal;
	}

	public void setTanggal(String tanggal)
	{
		this.tanggal = tanggal;
	}
	public String getJenis()
	{
		return jenis;
	}

	public void setJenis(String jenis)
	{
		this.jenis = jenis;
	}
}
