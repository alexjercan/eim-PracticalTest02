package ro.pub.cs.systems.eim.practicaltest02.network;

import java.io.IOException;
import java.net.Socket;

public interface DataParser {
    void sendRequest(Socket socket) throws IOException;
}
