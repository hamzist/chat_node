package tr.chat.chat_test;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Chat_room extends Activity implements OnClickListener {
	
	private TextView messages;
	private EditText newMsg;
	private Button send;
	private SocketIO socket;
	private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_room_view);
        
        // Getting views
        messages = (TextView)findViewById(R.id.messages);
        newMsg   = (EditText)findViewById(R.id.newMsg);
        send     = (Button)findViewById(R.id.btn_send);
        send.setOnClickListener(this);
        
        // Initialisation
        this.pd = new ProgressDialog(this);
        this.showDialog();
        
        // Connection to the socket
        try {
			socket = new SocketIO("http://192.168.2.22:5000");
		}
        catch (MalformedURLException ex) {
        	this.stopDialog();
        	AlertDialog.Builder buider = new AlertDialog.Builder(this);
			buider.setTitle("Exception Socket");
			buider.setMessage("Class: " + ex.getClass() + "\n\nCause: " + ex.getCause() + "\n\nMessage: " + ex.getMessage());
			buider.create().show();
			return;
		}
        
        socket.connect(new IOCallback() {
            @Override
            public void onMessage(final JSONObject json, IOAcknowledge ack) {
            	//Toast.makeText(Chat_room.this, "Server said: " + json.toString(), Toast.LENGTH_LONG).show();
            	new Thread() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            	Chat_room.this.messages.setText(Chat_room.this.messages.getText().toString() + "\n\n" + "Server said: " + json.toString());
                            }
                        });
                    }
                }.start();
            }

            @Override
            public void onMessage(final String data, IOAcknowledge ack) {
            	//Toast.makeText(Chat_room.this, "Server said: " + data, Toast.LENGTH_LONG).show();
            	new Thread() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            	Chat_room.this.messages.setText(Chat_room.this.messages.getText().toString() + "\n\n" + "Server said: " + data);
                            }
                        });
                    }
                }.start();
            }

            @Override
            public void onError(SocketIOException socketIOException) {
            	//Toast.makeText(Chat_room.this, "an Error occured", Toast.LENGTH_LONG).show();
            	final String error = "Class: " + socketIOException.getClass() + "\n\nCause: " + socketIOException.getClass() + "\n\nMessage: " + socketIOException.getMessage();
            	new Thread() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            	Chat_room.this.messages.setText(Chat_room.this.messages.getText().toString() + "\n\n" + "an Error occured\n\n" + error);
                            }
                        });
                    }
                }.start();
            }

            @Override
            public void onDisconnect() {
            	//Toast.makeText(Chat_room.this, "Connection terminated", Toast.LENGTH_LONG).show();
            	new Thread() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            	Chat_room.this.messages.setText(Chat_room.this.messages.getText().toString() + "\n\n" + "Connection terminated");
                            }
                        });
                    }
                }.start();
            }

            @Override
            public void onConnect() {
                //Toast.makeText(Chat_room.this, "Connection established", Toast.LENGTH_LONG).show();
            	Chat_room.this.socket.send("setPseudo");
            	new Thread() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            	Chat_room.this.messages.setText(Chat_room.this.messages.getText().toString() + "\n\n" + "Connection established");
                            }
                        });
                    }
                }.start();
            }
            
            @Override
            public void on(final String event, IOAcknowledge ack, final Object... args) {
            	new Thread() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            	// Test de la presence des données
                            	if(args.length == 0) {
                            		Chat_room.this.messages.setText("Pas de données recues");
                            		return;
                            	}
                            	
                            	JSONObject jo = (JSONObject) args[0];
                            	Chat_room.this.messages.setText(Chat_room.this.messages.getText().toString() + "\n\n" + "Server triggered event '" + event + "' " + jo.toString());
                            }
                        });
                    }
                }.start();
            }
        });
        
        this.stopDialog();
    }
    
    @Override
    protected void onStop() {
        super.onStop();  // Always call the superclass method first
        this.socket.disconnect();
    }
    
	public void onBackPressed() {
		this.socket.disconnect();
		finish();
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
		
		// Verifier la presence d'un message
		if(this.newMsg.getText().toString().isEmpty()) {
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("Erreur");
			alert.setMessage("Entrer un message");
			alert.create().show();
			return;
		}
		
		// Sending data
		if(!this.socket.isConnected()) {
			Toast.makeText(this, "You are not connected", Toast.LENGTH_SHORT).show();
		}
		socket.send("Hello Server!");
		//this.socket.emit("", "");
		
		
		/*JSONObject obj = new JSONObject();
		try {
			obj.put("message", "ssss");
		}
		catch (JSONException e) {
		}
		//socket.send(this.newMsg.getText().toString());
		socket.emit("message", obj);
		//socket.emit("message", this.newMsg.getText().toString());
		this.messages.setText(this.messages.getText().toString() + "\n\n" + this.newMsg.getText().toString());
		this.newMsg.setText("");*/
	}
	
	// Show the Progress Dialog
	private void showDialog() {
		this.pd.setCancelable(false);
		this.pd.setMessage("Connecting to server on " + Constante.ip + "...\n Please Wait");
		this.pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		this.pd.show();
	}
	
	// Dissmis the Progress Dialog
	private void stopDialog() {
		this.pd.dismiss();
	}
    
}
