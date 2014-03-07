package tr.chat.chat_test;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	
	private EditText name, adress;
	private Button go;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        name   = (EditText)findViewById(R.id.name);
        adress = (EditText)findViewById(R.id.adress);
        go     = (Button)findViewById(R.id.btnGo);
        go.setOnClickListener(this);
        
        Constante.ip = adress.getText().toString();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


	@Override
	public void onClick(View v) {
		// Test de la connexion internet
		if(!Constante.isInternetAvailable(this)) {
			AlertDialog.Builder buider = new AlertDialog.Builder(this);
			buider.setTitle("Attention");
			buider.setMessage("La connexion Internet est désactivée");
			buider.create().show();
			return;
		}
		
		// Verifier la presence d'un pseudo
		if(this.name.getText().toString().isEmpty()) {
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("Erreur");
			alert.setMessage("Entrer un pseudo");
			alert.create().show();
			return;
		}
		
		//
		Intent chat = new Intent(this, Chat_room.class);
		try {
			startActivity(chat);
		}
		catch(ActivityNotFoundException ex) {
			Toast.makeText(this, "Activity not found", Toast.LENGTH_LONG).show();
		}
	}
    
}
