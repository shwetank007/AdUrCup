package com.example.shwetank.contra;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.engineio.client.Socket;
import com.github.nkzawa.socketio.client.IO;

import java.net.URISyntaxException;


public class MainActivity extends ActionBarActivity {


    private com.github.nkzawa.socketio.client.Socket mSocket;
    private Button acceptButton, rejectButton, sendButton;
    private EditText textBox;
    private LinearLayout buttonLayout;
    private String message;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        message = "";
        textView = (TextView) findViewById(R.id.textview);
        acceptButton = (Button) findViewById(R.id.acceptbutton);
        rejectButton = (Button) findViewById(R.id.rejectbutton);
        sendButton = (Button) findViewById(R.id.sendbutton);
        textBox = (EditText) findViewById(R.id.textbox);
        buttonLayout = (LinearLayout) findViewById(R.id.buttonlayout);

        try {
            mSocket = IO.socket("http://192.168.61.1:8080");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        mSocket.on("receive", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        buttonLayout.setVisibility(View.VISIBLE);
//                        textView.setText(String.valueOf(args[0]));
                    }
                });
                message = (String) args[0];
            }
        });

        mSocket.on("accepted", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                Log.d("accepted", "recieved");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText("Message accepted");
//                        Toast.makeText(getApplicationContext(), "Message accepted", Toast.LENGTH_LONG);
                    }
                });

            }
        });

        mSocket.on("rejected", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("Rejected", "received");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText("Message rejected");
                    }
                });

            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = String.valueOf(textBox.getText());
                mSocket.emit("send", message);
            }
        });

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Message Accepted", Toast.LENGTH_LONG).show();
                mSocket.emit("acceptance", message);
                buttonLayout.setVisibility(View.GONE);
                textView.setText(message);
            }
        });

        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Message Rejected", Toast.LENGTH_LONG).show();
                mSocket.emit("rejectance", message);
                buttonLayout.setVisibility(View.GONE);
                textView.setText("");
            }
        });
        mSocket.connect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}