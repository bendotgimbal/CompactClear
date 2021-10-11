package compact.mobile;

public class IdUnik {
	private static IdUnik instance;  
	
	private String data;
	
	public String getData()
	 {
		  return this.data;
	 }
	
	public void setData(String I)
	 {
		  this.data = I;
	 }
	
	public static synchronized IdUnik getInstance(){
		 if(instance==null){
			 instance=new IdUnik();
		 }
		 return instance;
		 }
	
}

