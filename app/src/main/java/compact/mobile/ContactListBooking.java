
package compact.mobile;

public class ContactListBooking
{
	private String	kode_booking;
	private String	nama;
	private String	alamat;
	private String	latlang;

	public ContactListBooking()
	{
		// TODO Auto-generated constructor stub
	}

	public ContactListBooking(String kode_booking, String nama, String alamat,String latlang)
	{
		super();
		this.kode_booking = kode_booking;
		this.nama = nama;
		this.alamat = alamat;
		this.latlang = latlang;
	}

	public String getBooking()
	{
		return kode_booking;
	}

	public void setBooking(String kode_booking)
	{
		this.kode_booking = kode_booking;
	}

	public String getNama()
	{
		return nama;
	}

	public void setNama(String nama)
	{
		this.nama = nama;
	}

	public String getAlamat()
	{
		return alamat;
	}

	public void setAlamat(String alamat)
	{
		this.alamat = alamat;
	}
	
	public String getLatlang()
	{
		return latlang;
	}

	public void setLatlang(String latlang)
	{
		this.latlang = latlang;
	}

}
