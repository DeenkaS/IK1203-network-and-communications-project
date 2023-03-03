import java.net.*;
import java.io.*;

public class ConcAsk extends Thread {
    Socket pSocket;

    public ConcAsk(Socket pSocket) {
        this.pSocket = pSocket;
    }

    @Override
    public void run() {
        try {
            InputStream inStream = pSocket.getInputStream();
            OutputStream outStream = pSocket.getOutputStream();
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            output.toByteArray();
            int i;
            while ((i = inStream.read()) != 10) {
                output.write(i);
            }

            String request = new String(output.toString());

            String splitRequestArray[] = request.split("[ ?=&]");

            // for (int j = 0; j < splitRequestArray.length; j++) {
            // System.out.println(splitRequestArray[j]);
            // }

            if (!splitRequestArray[0].equals("GET")) {
                System.out.println("Bad request1");
                outStream.write("HTTP/1.1 400 Bad request \r\n\r\n".getBytes());
                pSocket.close();
                return;
            }

            if (!splitRequestArray[1].equals("/ask")) {
                System.out.println("Bad request2");
                outStream.write("HTTP/1.1 404 Object not found \r\n\r\n".getBytes());
                pSocket.close();
                return;
            }

            Integer timeout = stringparser_int(splitRequestArray, "timeout");
            Integer limit = stringparser_int(splitRequestArray, "limit");
            Integer port = stringparser_int(splitRequestArray, "port");
            boolean shutdown = stringparser_shutdown(splitRequestArray);
            String hostname = stringparser_hostname(splitRequestArray);
            byte[] toserver = stringparser_toserver(splitRequestArray);

            if (hostname.equals(null)) {
                System.out.println("Bad request3");
                outStream.write("HTTP/1.1 404 Object not found \r\n\r\n".getBytes());
                pSocket.close();
                return;
            }

            System.out.println(
                    "timeout: " + timeout + "\nlimit: " + limit + "\nport: " + port + "\nshutdown:" + shutdown +
                            "\nhostname: " + hostname + "\ntoserver: " + toserver);

            TCPClient tClient = new TCPClient(shutdown, timeout, limit);
            String message = new String(tClient.askServer(hostname, port, toserver));

            outStream.write("HTTP/1.1 200 OK \r\n".getBytes());
            outStream.write(("Content-length: " + message.length() + "\r\n").getBytes());
            outStream.write(("Content-type: text/plain \r\n\r\n").getBytes());
            outStream.write((message).getBytes());
            outStream.flush();
        } catch (Exception e) {

        }
    }

    static Integer stringparser_int(String[] httpArray, String inputString) {

        for (int i = 0; i < httpArray.length; i++) {
            if ((httpArray[i].toLowerCase()).equals(inputString.toLowerCase())) {
                return Integer.parseInt(httpArray[i + 1]);
            }
        }
        return null;
    }

    public static boolean stringparser_shutdown(String[] httpArray) {
        for (int i = 0; i < httpArray.length; i++) {
            if ((httpArray[i].toLowerCase()).equals("shutdown")) {
                if ((httpArray[i + 1].toLowerCase()).equals("true")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String stringparser_hostname(String[] httpArray) {
        for (int i = 0; i < httpArray.length; i++) {
            if ((httpArray[i].toLowerCase()).equals("hostname")) {
                return httpArray[i + 1];
            }
        }
        return null;
    }

    public static byte[] stringparser_toserver(String[] httpArray) {
        for (int i = 0; i < httpArray.length; i++) {
            if ((httpArray[i].toLowerCase()).equals("string")) {
                byte[] b = httpArray[i + 1].getBytes();
                return b;
            }
        }
        byte[] b = new byte[0];
        return b;

    }
}