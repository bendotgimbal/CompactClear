package compact.mobile.SuratJalan.DB;

public class C_Finish_PoOutstanding {
	private String	po;
	private String	assigment;
	
	public C_Finish_PoOutstanding()
	{
		// TODO Auto-generated constructor stub
	}
	
	public C_Finish_PoOutstanding(String po, String assigment)
	{
		super();
		
		this.po=po;
		this.assigment=assigment;
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
}
