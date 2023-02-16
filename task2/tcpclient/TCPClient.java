package tcpclient;

import java.net.*;
import java.io.*;

public class TCPClient {
    Integer timeout;
    Integer limit;
    boolean shutdown;

    public TCPClient(boolean shutdown, Integer timeout, Integer limit) {
        this.shutdown = shutdown;
        this.timeout = timeout;
        this.limit = limit;
    }

    public byte[] askServer(String hostname, int port, byte[] toServerBytes) throws IOException {
        Socket client = new Socket(hostname, port);
        client.setSoTimeout(timeout);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            output.toByteArray();

            client.getOutputStream().write(toServerBytes);
            if (shutdown) {
                client.shutdownOutput();
            }

            if (limit != null) {

                output.write(client.getInputStream().readNBytes(limit));
            } else {
                output.write(client.getInputStream().readAllBytes());
            }

        } catch (SocketTimeoutException e) {
            client.close();
            return output.toByteArray();
        }
        client.close();
        return output.toByteArray();
    }
}