package compact.mobile;

public class LocPk {
	private static LocPk instance;  
	
	private String data;
	
	public String getData()
	 {
		  return this.data;
	 }
	
	public void setData(String S)
	 {
		  this.data = S;
	 }
	
	public static synchronized LocPk getInstance(){
		 if(instance==null){
			 instance=new LocPk();
		 }
		 return instance;
		 }
	
}
