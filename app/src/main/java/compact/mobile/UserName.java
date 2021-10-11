package compact.mobile;

public class UserName {
	private static UserName instance;  
	
	private String data;
	
	public String getData()
	 {
		  return this.data;
	 }
	
	public void setData(String S)
	 {
		  this.data = S;
	 }
	
	public static synchronized UserName getInstance(){
		 if(instance==null){
			 instance=new UserName();
		 }
		 return instance;
		 }
		
}
