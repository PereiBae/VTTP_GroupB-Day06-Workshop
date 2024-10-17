package fc;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server{

    public static void main (String[] args) throws IOException {

        ExecutorService thrPool = Executors.newFixedThreadPool(2);
        
        int port = 3000;
        if(args.length > 0){
            port = Integer.parseInt(args[0]);
        }
        String file = args[1];
        ServerSocket server = new ServerSocket(port);

        Cookie cookieManager = new Cookie(file);

        System.out.println("Waiting for connection");

        while (true){
            Socket sock = server.accept();

            System.out.println("Got a new conection");

            CookieClientHandler handler = new CookieClientHandler(sock, file);

            // Pass the work to the worker
            thrPool.submit(handler);

        }

    }

}