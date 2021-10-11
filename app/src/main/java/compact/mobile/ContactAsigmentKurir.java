
package compact.mobile;

public class ContactAsigmentKurir
{
	private String	nomor_manifes;
	private String	tanggal;
	private String	jenis;
	private String	po;


	public ContactAsigmentKurir()
	{
		// TODO Auto-generated constructor stub
	}

	public ContactAsigmentKurir(String nomor_manifes, String tanggal,String jenis,String po)
	{
		super();
		this.nomor_manifes = nomor_manifes;
		this.tanggal = tanggal;
		this.jenis = jenis;
		this.po = po;

	}

	public String getManifes()
	{
		return nomor_manifes;
	}

	public void setManifes(String nomor_manifes)
	{
		this.nomor_manifes = nomor_manifes;
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
	public String getPo()
	{
		return po;
	}

	public void setPo(String po)
	{
		this.po = po;
	}
}
