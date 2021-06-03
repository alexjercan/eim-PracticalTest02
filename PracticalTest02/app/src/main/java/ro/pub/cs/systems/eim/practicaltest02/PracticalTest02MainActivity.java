package ro.pub.cs.systems.eim.practicaltest02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ro.pub.cs.systems.eim.practicaltest02.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02.network.ClientThread;
import ro.pub.cs.systems.eim.practicaltest02.network.ServerThread;

public class PracticalTest02MainActivity extends AppCompatActivity {

    private EditText pokemonNameEditText = null;
    private TextView pokemonInformationTextView = null;
    private Button sendRequestButton = null;

    private ServerThread serverThread = null;
    private ClientThread clientThread = null;

    private SendButtonClickListener connectButtonClickListener = new SendButtonClickListener();
    private class SendButtonClickListener implements Button.OnClickListener {
        public void onClick(View view) {
            serverThread = new ServerThread(Integer.parseInt(Constants.PORT));
            if (serverThread.getServerSocket() == null) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }
            serverThread.start();

            String pokemonName = pokemonNameEditText.getText().toString();
            if (pokemonName == null || pokemonName.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }

            pokemonInformationTextView.setText(Constants.EMPTY_STRING);

            clientThread = new ClientThread("localhost", Integer.parseInt(Constants.PORT), pokemonName, pokemonInformationTextView);
            clientThread.start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onCreate() callback method has been invoked");

        sendRequestButton = (Button)findViewById(R.id.sendRequest);
        sendRequestButton.setOnClickListener(connectButtonClickListener);

        pokemonNameEditText = (EditText)findViewById(R.id.pokemonNameText);
        pokemonInformationTextView = (TextView)findViewById(R.id.informationTextView);
    }

    @Override
    protected void onDestroy() {
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onDestroy() callback method has been invoked");
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }
}