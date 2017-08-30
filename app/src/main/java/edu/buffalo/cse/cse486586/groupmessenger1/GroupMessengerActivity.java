package edu.buffalo.cse.cse486586.groupmessenger1;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.UnknownHostException;

/**
 * GroupMessengerActivity is the main Activity for the assignment.
 * 
 * @author stevko
 *
 */
public class GroupMessengerActivity extends Activity {
    static final String REMOTE_PORT0 = "11108";
    static final String REMOTE_PORT1 = "11112";
    static final String REMOTE_PORT2 = "11116";
    static final String REMOTE_PORT3 = "11120";
    static final String REMOTE_PORT4 = "11124";
    static final int SERVER_PORT = 10000;
    static final String TAG = GroupMessengerActivity.class.getSimpleName();
    int counter = 0;
    //Uri providerUri = Uri.parse("content://edu.buffalo.cse.cse486586.groupmessenger1.provider");
    private final Uri mUri = buildUri("content", "edu.buffalo.cse.cse486586.groupmessenger1.provider");
    private Uri buildUri(String scheme, String authority) {
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.authority(authority);
        uriBuilder.scheme(scheme);
        return uriBuilder.build();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_messenger);

        //added by akhil from simple messenger
        TelephonyManager tel = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String portStr = tel.getLine1Number().substring(tel.getLine1Number().length() - 4);
        final String myPort = String.valueOf((Integer.parseInt(portStr) * 2));
        try {
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            new ServerTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, serverSocket);
        } catch (IOException e) {
            Log.e(TAG, "Can't create a ServerSocket");
            return;
        }

        /*
         * TODO: Use the TextView to display your messages. Though there is no grading component
         * on how you display the messages, if you implement it, it'll make your debugging easier.
         */
        final TextView tv = (TextView) findViewById(R.id.textView1);
        tv.setMovementMethod(new ScrollingMovementMethod());
        
        /*
         * Registers OnPTestClickListener for "button1" in the layout, which is the "PTest" button.
         * OnPTestClickListener demonstrates how to access a ContentProvider.
         */
        findViewById(R.id.button1).setOnClickListener(
                new OnPTestClickListener(tv, getContentResolver()));

        //added by akhil
        final EditText editText = (EditText) findViewById(R.id.editText1);
        final Button send = (Button) findViewById(R.id.button4);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = editText.getText().toString() + "\n";
                editText.setText("");
                tv.append("\t" + msg);
                new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, msg, myPort);
            }
        });
        /*
         * TODO: You need to register and implement an OnClickListener for the "Send" button.
         * In your implementation you need to get the message from the input box (EditText)
         * and send it to other AVDs.
         */
    }

    //added by akhil from simple messenger
    private class ServerTask extends AsyncTask<ServerSocket, String, Void> {

        @Override
        protected Void doInBackground(ServerSocket... sockets) {
            ServerSocket serverSocket = sockets[0];
            try {
                String str = "";
                while (true) {
                    Socket clSocket = serverSocket.accept();
                    DataOutputStream out = new DataOutputStream(clSocket.getOutputStream());
                    out.writeUTF("All Well");
                    try {
                        DataInputStream br = new DataInputStream(clSocket.getInputStream());
                        str = br.readUTF();

                        saveMessageMethod(Integer.toString(counter),str);
                        counter++;
                        publishProgress(str);
                        //br.close();
                    }catch (NullPointerException e){
                        Log.e(TAG,"null pointer data input stream");
                    }

                    clSocket.close();
                }
            }catch (IOException e){
                Log.e(TAG,"IOException in server");
            }
            return null;
        }

        private void saveMessageMethod(String count,String str) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("key", count);
            contentValues.put("value", str);
            getContentResolver().insert(mUri,contentValues);
        }


        protected void onProgressUpdate(String...strings) {
            /*
             * The following code displays what is received in doInBackground().
             */
            String strReceived = strings[0].trim();
            TextView localTextView = (TextView) findViewById(R.id.textView1);
            localTextView.append(strReceived + "\t\n");

            return;
        }
    }

    private class ClientTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... msgs) {
                String[] array = new String[5];
                array[0] = REMOTE_PORT0;
                array[1] = REMOTE_PORT1;
                array[2] = REMOTE_PORT2;
                array[3] = REMOTE_PORT3;
                array[4] = REMOTE_PORT4;
                for (int i = 0; i < 5; i++) {
                    try{
                    Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),
                            Integer.parseInt(array[i]));

                    String msgToSend = msgs[0];
                    if (msgToSend != null)
                        msgToSend = msgToSend.trim();
                    DataOutputStream pw = new DataOutputStream(socket.getOutputStream());
                    pw.writeUTF(msgToSend);
                    pw.flush();
                        while (true){
                            InputStream inFromServer = socket.getInputStream();
                            DataInputStream in = new DataInputStream(inFromServer);
                            String comp = in.readUTF();
                            if(comp.equals("All Well"))
                                break;
                        }
                    }catch(UnknownHostException e){
                        Log.e(TAG, "ClientTask UnknownHostException");
                    }catch(IOException e){
                        Log.e(TAG, "ClientTask socket IOException");
                    }
                }

            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_group_messenger, menu);
        return true;
    }
}
