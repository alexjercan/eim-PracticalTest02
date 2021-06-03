package ro.pub.cs.systems.eim.practicaltest02.network;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import ro.pub.cs.systems.eim.practicaltest02.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02.general.Utilities;

public class PokemonListParser implements DataParser {
    private TextView informationTextView;
    private String uri;

    public PokemonListParser(String uri, TextView informationTextView) {
        this.informationTextView = informationTextView;
        this.uri = uri;
    }

    @Override
    public void sendRequest(Socket socket) throws IOException {
        BufferedReader bufferedReader = Utilities.getReader(socket);
        PrintWriter printWriter = Utilities.getWriter(socket);

        printWriter.println(uri);
        printWriter.flush();

        String pageSourceCode;
        while ((pageSourceCode = bufferedReader.readLine()) != null) {
            final String information = pageSourceCode;
            informationTextView.post(new Runnable() {
                @Override
                public void run() {
                    informationTextView.setText(information);
                }
            });
        }
    }
}
