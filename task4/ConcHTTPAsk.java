import java.io.*;
import java.net.*;
import java.net.http.HttpResponse;

public class ConcHTTPAsk {
    public static void main(String[] args) throws Exception {
        ServerSocket socket = new ServerSocket(Integer.parseInt(args[0]));
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        while (!socket.isClosed()) {
            Socket cSocket = socket.accept();
            ConcAsk HTTPprocess = new ConcAsk(cSocket);
            Thread newthread = new Thread(HTTPprocess);
            newthread.start();

        }
    }
}
