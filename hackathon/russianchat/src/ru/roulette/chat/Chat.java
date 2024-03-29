package ru.roulette.chat;

import ru.roulette.comm.AndroidCommHandler;
import ru.roulette.comm.CommHandler;
import ru.roulette.comm.Identity;
import android.app.Activity;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Chat extends Activity implements Runnable {
	
	private static final int DIALOG_ABOUT = 0;

    final private static String TAG="Chat";
	
	private static Button sendButton;
	private static Button nextButton;
	private static ImageView userImage;
	private static EditText msgInput;
	private static EditText msgOutput;
	
	private int myId = 0;
	private Identity destId = null;
	private static byte[] ownImage;
	
	private String message;
	
	CommHandler commHandler;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
        
    public void onStart(){
    	super.onStart();
    
        setContentView(R.layout.chat);
        
        sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
		
			public void onClick(View v) {
				sendMsgInput();
			}
		});
        nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				nextChatContact();
			}
		});
        msgInput = (EditText) findViewById(R.id.msgInput);
        msgInput.requestFocus();
        msgOutput = (EditText) findViewById(R.id.msgOutput);
        userImage = (ImageView) findViewById(R.id.picture);
        //set a default offline user image...
		userImage.setImageDrawable(getResources().getDrawable( R.drawable.offlineuser));
        commHandler = new AndroidCommHandler();
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE); 
        String imei = telephonyManager.getDeviceId();
        Log.i(TAG,"IMEI = "+imei);
        //TODO use IMEI = ID ;)
        
        updateNextButton();
    }

	private void updateNextButton() {
		if (this.destId == null) {
        	nextButton.setText("Login");
        } else {
        	nextButton.setText("Next");
        }
	}
    
    /**
	 * Create the options menu for this activity {@inheritDoc}
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.mainmenu, menu);
		return true;
	}
	
	/**
	 * Gets called when an option from the menu got chosen. {@inheritDoc}
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle all of the possible menu actions.
		switch (item.getItemId()) {
		case R.id.menuAbout:
			showDialog(DIALOG_ABOUT);
			break;
		case R.id.menuSettings:
			Intent intent = new Intent();
			intent.setClass(Chat.this, Settings.class);	
			// start the new activity
			startActivityForResult(intent, 1);
			break;
		case R.id.menuLogout:
			commHandler.logoff(this.myId);
			//set offline user image
			userImage.setImageDrawable(getResources().getDrawable( R.drawable.offlineuser));
			this.destId = null;
			this.myId = 0;
			break;
		default:
			Log.e(TAG, "Unknown item selected in menu: " + item);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
	    Dialog dialog;
	    switch (id) {
        case DIALOG_ABOUT:
            Builder builder = new Builder(this);
            builder.setView(getLayoutInflater().inflate(R.layout.about, null));
            builder.setPositiveButton(R.string.okButton, null);
            builder.setIcon(R.drawable.icon);
            dialog = builder.create();
            break;
        default:
            dialog = null;
            break;
        }
	    return dialog;
	}
    
    private void sendMsgInput() {
    	if (this.destId == null) {
    		Toast.makeText(Chat.this,
					"Please click 'Login' first to log in.",
					Toast.LENGTH_LONG).show();
    	}
    	updateNextButton();
    	if (msgInput.getText() != null && this.destId != null) {
    		String message = msgInput.getText().toString();
    		Log.i(TAG,"sending message="+message+" to id="+this.destId.getId());
    		commHandler.message(this.myId, this.destId.getId(), message);
    		Log.i(TAG,"message sent");
    		msgInput.setText(""); //clear input
    		msgInput.requestFocus();
    	}
    }
    
    private void nextChatContact() {
    	if (this.myId == 0) {
    		// login
    		Log.i(TAG,"starting login....");
    		this.myId = commHandler.login(ownImage);
    		if (this.myId == 0) {
    			Log.w(TAG,"Login failed...");
    			Toast.makeText(Chat.this,
    					"Sorry - Login failed. Please try again later...",
    					Toast.LENGTH_LONG).show();
    			return;
    			
    		} else {
	    		Log.i(TAG,"Logged in with id="+this.myId);
	    		updateNextButton();
	    		//set default online image if no ownImage was provided...
	    		if(ownImage == null) {
	    			userImage.setImageDrawable(getResources().getDrawable( R.drawable.onlineuser));
	    		}
	    		Thread thread = new Thread(this);
	    		thread.start();
    		}
    	}
		// get next chat contact from server
    	this.destId = commHandler.nextIdentity();
    	if (this.destId != null && this.destId.getImage() != null && this.destId.getImage().length > 0) {
			userImage.setImageBitmap(BitmapFactory.decodeByteArray(this.destId.getImage(), 0, this.destId.getImage().length));
		}
    	// TODO ? no next id found
    	// TODO ? old id == new id
    }
    

	public void run() {
		while (true) {
			if (this.myId != 0) {
				this.message = commHandler.getMyMessages(this.myId);
				handler.sendEmptyMessage(0);				
				try {
					 Thread.sleep(800);
				} catch (InterruptedException e) {
					Log.e(TAG,"Polling sleep error"+e);
				}
			}
		}
	}
    
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			if(Chat.this.message != null) {
				msgOutput.setText(Chat.this.message);
			} 
		}

	};
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Bundle bundle = data.getExtras();
		if (data != null && bundle != null) {
			ownImage = (byte[]) bundle.getSerializable("picture");
			userImage.setImageBitmap(BitmapFactory.decodeByteArray(ownImage, 0, ownImage.length));
		}
		
	};
	
	@Override
	protected void onStop() {
		super.onStop();
		commHandler.logoff(this.myId);
		this.myId = 0;
		this.destId = null;
		updateNextButton();
	}
	
}