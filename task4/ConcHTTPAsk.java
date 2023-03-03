import java.io.*;
import java.net.*;

public class ConcHTTPAsk {
    public static void main(String[] args) throws IOException{
        ServerSocket socket = new ServerSocket(Integer.parseInt(args[0]));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.toByteArray();

        while(true)try(Socket cSocket = socket.accept()){
            ConcAsk HTTPprocess = new ConcAsk(cSocket);
            HTTPprocess.run();
        }
    }
}
