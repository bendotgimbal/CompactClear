package compact.mobile;

public class noWaybill {
	private static noWaybill instance;  
	
	private String data;
	
	public String getData()
	 {
		  return this.data;
	 }
	
	public void setData(String S)
	 {
		  this.data = S;
	 }
	
	public static synchronized noWaybill getInstance(){
		 if(instance==null){
			 instance=new noWaybill();
		 }
		 return instance;
		 }
	
}
