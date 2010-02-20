package ru.roulette.chat;


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
        msgOutput = (EditText) findViewById(R.id.msgOutput);
    }
    
    private void sendMsgInput() {
    	if (this.msgInput.getText() != null) {
    		String msg = this.msgInput.getText().toString();
    		//TODO send to server
    	}
    }
    
    private void nextChatContact() {
    	// TODO get next chat contact from server
    	
    }
}