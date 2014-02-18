package tr.chat.chat_test;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class Constante {
	
	public static String ip = "";
	
	// Test de la presence d'une connexion internet
	public static boolean isInternetAvailable(Context context) {
		try {
			ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
	
			if(activeNetwork == null) {
				return false;
			}
			else {
				if(!(activeNetwork.isConnectedOrConnecting())) {
					return false;
				}
			}
		}
		catch(Exception ex) {
			Toast.makeText(context, "isInternetAvailable method exception\n" + ex.getCause() + "\n" + ex.getMessage(), Toast.LENGTH_LONG).show();
			return false;
		}
		
		return true;
	}

}
