package compact.mobile;

public class urlgw {
private static urlgw instance;  
	
	private String data;
	
	public String getData()
	 {
		  return this.data;
	 }
	
	public void setData(String S)
	 {
		  this.data = S;
	 }
	
	public static synchronized urlgw getInstance(){
		 if(instance==null){
			 instance=new urlgw();
		 }
		 return instance;
		 }
}
