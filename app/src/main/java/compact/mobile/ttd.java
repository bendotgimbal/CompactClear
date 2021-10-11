package compact.mobile;

public class ttd {
private static ttd instance;  
	
	private String data;
	
	public String getData()
	 {
		  return this.data;
	 }
	
	public void setData(String S)
	 {
		  this.data = S;
	 }
	
	public static synchronized ttd getInstance(){
		 if(instance==null){
			 instance=new ttd();
		 }
		 return instance;
		 }
}
