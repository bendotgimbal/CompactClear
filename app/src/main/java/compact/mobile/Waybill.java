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

public class Waybill
{
	private String	waybill;
	private String	penerima;
	private String	kota;
	private String	tujuan;
	private String	mswb_pk;
	private String	locpk;
	private String	username;
	private String	tiperem;
	private String	telp;
	private String	tipe;
	private String	keterangan;
	private String	lat_long;
	private String	waktu;
	private String	status;
	private String	po;
//	private String	assigment;
	
	public Waybill()
	{
		// TODO Auto-generated constructor stub
	}

	 
	public Waybill(String	po, String	waybill, String	penerima, String	kota,	 String	tujuan,	 String	mswb_pk,	 String	locpk,	 String	username,	 String	tiperem,	 String	telp,	 String	tipe,	 String	keterangan,	 String	lat_long,	 String	waktu,	 String	status)
	{
		super();
				
		this.waybill=waybill;
		this.penerima=penerima;
		this.kota=kota;
		this.tujuan=tujuan;
		this.mswb_pk=mswb_pk;
		this.locpk=locpk;
		this.username=username;
		this.tiperem=tiperem;
		this.telp=telp;
		this.tipe=tipe;
		this.keterangan=keterangan;
		this.lat_long=lat_long;
		this.waktu=waktu;
		this.status=status;
		this.po=po;
//		this.assigment=assigment;
	}
	public String getPo()
	{
		return po;
	}

	public void setPo(String po)
	{
		this.po = po;
	}
//	public String getAssigment()
//	{
//		return assigment;
//	}
//
//	public void setAssigment(String assigment)
//	{
//		this.assigment = assigment;
//	}
	public String getWaybill()
	{
		return waybill;
	}

	public void setWaybill(String waybill)
	{
		this.waybill = waybill;
	}
	
	public String getPenerima()
	{
		return penerima;
	}

	public void setPenerima(String penerima)
	{
		this.penerima = penerima;
	}
	
	public String getKota()
	{
		return kota;
	}

	public void setKota(String kota)
	{
		this.kota = kota;
	}
	
	public String getTujuan()
	{
		return tujuan;
	}

	public void setTujuan(String tujuan)
	{
		this.tujuan = tujuan;
	}
	
	public String getMswb_pk()
	{
		return mswb_pk;
	}

	public void setMswb_pk(String mswb_pk)
	{
		this.mswb_pk = mswb_pk;
	}
	
	public String getLocpk()
	{
		return locpk;
	}

	public void setLocpk(String locpk)
	{
		this.locpk = locpk;
	}
	
	public String getUser()
	{
		return username;
	}

	public void setUser(String user)
	{
		this.username = user;
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
	
	public String getTipe()
	{
		return tipe;
	}

	public void setTipe(String tipe)
	{
		this.tipe = tipe;
	}
	
	public String getKeterangan()
	{
		return keterangan;
	}

	public void setKeterangan(String keterangan)
	{
		this.keterangan = keterangan;
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
	
	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}
	
}
