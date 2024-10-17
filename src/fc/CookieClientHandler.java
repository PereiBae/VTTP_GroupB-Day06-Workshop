package fc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.*;
import java.util.*;

public class CookieClientHandler implements Runnable {

    private final Socket sock;
    private String file;

    public CookieClientHandler(Socket s, String file) {
        sock = s;
        this.file=file;
    }

    @Override
    public void run(){

        String threadName = Thread.currentThread().getName();
        try{
            Cookie cookieManager = new Cookie(file);

            InputStream is = sock.getInputStream();
            Reader reader = new InputStreamReader(is);
            BufferedReader serverBr = new BufferedReader(reader);

            OutputStream os = sock.getOutputStream();
            Writer writer = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(writer);

            String fromClient = serverBr.readLine();
            while (fromClient != null){

                System.out.println("Received command from client: " + fromClient);

                if(fromClient.equalsIgnoreCase("get-cookie")){
                    String randomCookie = cookieManager.getCookie();
                    String response = "cookie-text " + randomCookie;
                    bw.write(response + "\n");
                    bw.flush();
                    System.out.println("Sent cookie to client: " + randomCookie);
                } else if (fromClient.equalsIgnoreCase("close")){
                    System.out.println("Closing connection to client: " + sock.getRemoteSocketAddress());
                    break;
                } else {
                    System.out.println("Unknown command received: " + fromClient);
                }


                fromClient = serverBr.readLine();
            }
            
            System.out.println("Closing socket and streams for client: " + sock.getRemoteSocketAddress());
            bw.close();
            serverBr.close();
            sock.close();
        } catch (IOException ex){
            // Exception Handler
            ex.printStackTrace();
        }

    }
    
}
