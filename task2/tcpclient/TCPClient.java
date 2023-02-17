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
        if(this.timeout != null){
            client.setSoTimeout(this.timeout);
        }
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        output.toByteArray();
        
        client.getOutputStream().write(toServerBytes);
        if (this.shutdown) {
            client.shutdownOutput();
        }
        
        try {
            if (limit != null) {

                output.write(client.getInputStream().readNBytes(this.limit));
            } else {
                byte[] b = new byte[1];
                while(client.getInputStream().read(b) != -1){
                    output.write(b);
                    //System.out.println("hejhej");
                }
            }

        } catch (SocketTimeoutException e) {
            client.close();
            return output.toByteArray();
        }
        client.close();
        return output.toByteArray();
    }
}
