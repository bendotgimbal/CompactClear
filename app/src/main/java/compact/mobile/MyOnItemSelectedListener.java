package compact.mobile;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;


public class MyOnItemSelectedListener implements OnItemSelectedListener {
	  public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
		tpPenerima tppenerima = tpPenerima.getInstance(); 
		tppenerima.setData(parent.getItemAtPosition(pos).toString());
	  }
	 
	  
 	  public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
	   }
	 
	}
