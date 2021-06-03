package ro.pub.cs.systems.eim.practicaltest02.network;

import android.util.Log;

import java.io.IOException;
import java.net.Socket;

import ro.pub.cs.systems.eim.practicaltest02.general.Constants;

public class ClientThread extends Thread {

    private String address;
    private int port;
    private DataParser dataParser;

    private Socket socket;

    public ClientThread(String address, int port, DataParser dataParser) {
        this.address = address;
        this.port = port;
        this.dataParser = dataParser;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);
            dataParser.sendRequest(socket);
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }
}