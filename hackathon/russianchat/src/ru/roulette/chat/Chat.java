package ru.roulette.chat;


import ru.roulette.comm.CommHandler;
import ru.roulette.comm.Identity;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Chat extends Activity {
	
	final private static String TAG="Chat";
	
	private static Button sendButton;
	private static Button nextButton;
	
	private static EditText msgInput;
	private static EditText msgOutput;
	
	private int myId = 0;
	private Identity destId = null;
	private byte[] image;
	
	CommHandler commHandler;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        
        sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sendMsgInput();
			}
		});
        nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				nextChatContact();
			}
		});
        msgInput = (EditText) findViewById(R.id.msgInput);
        msgInput.requestFocus();
        msgOutput = (EditText) findViewById(R.id.msgOutput);
    }
    
    private void sendMsgInput() {
    	if (msgInput.getText() != null && this.destId != null) {
    		String message = msgInput.getText().toString();
    		commHandler.message(this.myId, this.destId.getId(), message);
    		msgInput.requestFocus();
    	}
    }
    
    private void nextChatContact() {
    	if (this.myId == 0) {
    		// login
    		this.myId = commHandler.login(this.image);
    	}
		// get next chat contact from server
    	this.destId = commHandler.nextIdentity();

    	// TODO ? no next id found
    	// TODO ? old id == new id
    }
}