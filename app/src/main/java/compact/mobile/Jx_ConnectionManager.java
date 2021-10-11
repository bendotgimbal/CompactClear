package compact.mobile;

import android.content.Context;
import android.content.ContextWrapper;
import android.net.ConnectivityManager;

public class Jx_ConnectionManager extends ContextWrapper {

	public Jx_ConnectionManager(Context base) {
		super(base);
		// TODO Auto-generated constructor stub
	}

	public boolean isNetOk(Context context) {
		// get Connectivity Manager object to check connection
		ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		// Check for network connections
		if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED
				|| connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING
				|| connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING
				|| connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

			return true;
		} else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED
				|| connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

			return false;
		}
		return false;
	}

}
