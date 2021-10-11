package compact.mobile;

import java.io. BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
 
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

public class MyFile {
	 String TAG = "MyFile";
	    Context context;
	    public MyFile(Context context){
	        this.context = context;
	    }
	     
	    public Boolean writeToSD(String text){
	        Boolean write_successful = false;
	         File root=null; 
	            try { 
	                // check for SDcard  
	                root = Environment.getExternalStorageDirectory(); 
	                Log.i(TAG,"path.." +root.getAbsolutePath()); 
	       
	                //check sdcard permission 
	                if (root.canWrite()){ 
	                    File fileDir = new File(root.getAbsolutePath()); 
	                    fileDir.mkdirs(); 
	       
	                    File file= new File(fileDir, "clear.ini"); 
	                    FileWriter filewriter = new FileWriter(file); 
	                    BufferedWriter out = new BufferedWriter(filewriter); 
	                    out.write(text); 
	                    out.close(); 
	                    write_successful = true;
	                } 
	            } catch (IOException e) { 
	                Log.e("ERROR:---", "Could not write file to SDCard" + e.getMessage()); 
	                write_successful = false;
	            } 
	        return write_successful;
	    }
	     
	    public String readFromSD(){
	    	
	    	
	        File sdcard = Environment.getExternalStorageDirectory();
	        File file = new File(sdcard,"clear.ini");
	        StringBuilder text = new StringBuilder();
	        try {
	            BufferedReader br = new BufferedReader(new FileReader(file));
	            String line;
	            while ((line = br.readLine()) != null) {
	                text.append(line);
	                text.append('\n');
	            }
	        }
	        catch (IOException e) {
	        }
	        return text.toString();
	    }
	 
	    public String readini() throws IOException{
	    	AssetManager am = context.getAssets();
	    	InputStream is = am.open("clear.ini");
	    	InputStreamReader inputStreamReader = new InputStreamReader(is);
	    	StringBuilder text = new StringBuilder();
	        try {
	            BufferedReader br = new BufferedReader(inputStreamReader);
	            String line;
	            while ((line = br.readLine()) != null) {
	                text.append(line);
	                text.append('\n');
	            }
	        }
	        catch (IOException e) {
	        }
	        return text.toString();
	    }
	    
	    @SuppressLint("WorldReadableFiles")
	    @SuppressWarnings("static-access")
	    public Boolean writeToSandBox(String text){
	        Boolean write_successful = false;
	        try{
	            FileOutputStream fOut = context.openFileOutput("clear.ini",
	                    context.MODE_WORLD_READABLE);
	            OutputStreamWriter osw = new OutputStreamWriter(fOut);
	            osw.write(text);
	            osw.flush();
	            osw.close();
	        }catch(Exception e){
	            write_successful = false;
	        }
	        return write_successful;
	    }
	    public String readFromSandBox(){
	        String str ="";
	        String new_str = "";
	        try{
	            FileInputStream fIn = context.openFileInput("clear.ini");
	            InputStreamReader isr = new InputStreamReader(fIn);
	            BufferedReader br=new BufferedReader(isr);
	            
	            while((str=br.readLine())!=null)
	            {
	                new_str +=str;
	                System.out.println(new_str);
	            }           
	        }catch(Exception e)
	        {          
	        }
	        return new_str;
	    }
	
}
