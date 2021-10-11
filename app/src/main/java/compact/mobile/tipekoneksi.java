package compact.mobile;

public class tipekoneksi {
private static tipekoneksi instance;  
	
	private String data;
	
	public String getData()
	 {
		  return this.data;
	 }
	
	public void setData(String S)
	 {
		  this.data = S;
	 }
	
	public static synchronized tipekoneksi getInstance(){
		 if(instance==null){
			 instance=new tipekoneksi();
		 }
		 return instance;
		 }
}
