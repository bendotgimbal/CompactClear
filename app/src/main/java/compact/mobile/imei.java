package compact.mobile;

public class imei {
private static imei instance;  
	
	private String data;
	
	public String getData()
	 {
		  return this.data;
	 }
	
	public void setData(String S)
	 {
		  this.data = S;
	 }
	
	public static synchronized imei getInstance(){
		 if(instance==null){
			 instance=new imei();
		 }
		 return instance;
		 }
}