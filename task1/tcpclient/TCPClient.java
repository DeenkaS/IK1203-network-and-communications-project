package tcpclient;

import java.net.Socket;
import java.io.*;

public class TCPClient {

    public TCPClient() {
    }

    public byte[] askServer(String hostname, int port, byte[] toServerBytes) throws IOException {
        Socket client = new Socket(hostname, port);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.toByteArray();

        client.getOutputStream().write(toServerBytes);
        output.write(client.getInputStream().readAllBytes());

        client.close();

        return output.toByteArray();
    }
}
