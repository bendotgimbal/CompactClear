package compact.mobile.SuratJalan.DB;

/**
 * Created by Bendot Gimbal on 18/09/2018.
 */

public class C_POD_List {
    private String	mrem_kode;
    private String	mrem_keterangan;

    public C_POD_List()
    {
        // TODO Auto-generated constructor stub
    }

    public C_POD_List(String mrem_kode, String mrem_keterangan) {
        super();
        this.mrem_kode=mrem_kode;
        this.mrem_keterangan=mrem_keterangan;
    }

    public String getPOD_Kode()
    {
        return mrem_kode;
    }

    public void setPOD_Kode(String mrem_kode)
    {
        this.mrem_kode = mrem_kode;
    }

    public String getPOD_Keterangan()
    {
        return mrem_keterangan;
    }

    public void setPOD_Keterangan(String mrem_keterangan)
    {
        this.mrem_keterangan = mrem_keterangan;
    }
}
