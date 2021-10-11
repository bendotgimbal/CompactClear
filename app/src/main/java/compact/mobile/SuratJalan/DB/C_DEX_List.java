package compact.mobile.SuratJalan.DB;

/**
 * Created by yosua on 29/06/2018.
 */

public class C_DEX_List {
    private String	mrem_kode;
    private String	mrem_keterangan;

    public C_DEX_List()
    {
        // TODO Auto-generated constructor stub
    }

    public C_DEX_List(String mrem_kode, String mrem_keterangan) {
        super();
        this.mrem_kode=mrem_kode;
        this.mrem_keterangan=mrem_keterangan;
    }

    public String getDEX_Kode()
    {
        return mrem_kode;
    }

    public void setDEX_Kode(String mrem_kode)
    {
        this.mrem_kode = mrem_kode;
    }

    public String getDEX_Keterangan()
    {
        return mrem_keterangan;
    }

    public void setDEX_Keterangan(String mrem_keterangan)
    {
        this.mrem_keterangan = mrem_keterangan;
    }
}
