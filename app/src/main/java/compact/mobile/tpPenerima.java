package compact.mobile;

public class tpPenerima {
private static tpPenerima instance;  
	
	private String data;
	
	public String getData()
	 {
		  return this.data;
	 }
	
	public void setData(String S)
	 {
		  this.data = S;
	 }
	
	public static synchronized tpPenerima getInstance(){
		 if(instance==null){
			 instance=new tpPenerima();
		 }
		 return instance;
		 }
	
}
