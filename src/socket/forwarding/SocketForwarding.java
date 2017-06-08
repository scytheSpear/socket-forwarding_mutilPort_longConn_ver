package socket.forwarding;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketForwarding extends Thread {

    public int SOURCE_PORT;
    public String DESTINATION_HOST;
    public int DESTINATION_PORT;

    public SocketForwarding(int sPort, String dHost, int dPort) {
        this.SOURCE_PORT = sPort;
        this.DESTINATION_HOST = dHost;
        this.DESTINATION_PORT = dPort;
    }

    @Override
    public void run() {

        Socket aServerSocket;
        ServerSocket serverSocket;

        while (true) {
            try {
                serverSocket = new ServerSocket(SOURCE_PORT);
                System.out.println("socket server started, forwarding port " + SOURCE_PORT);

                aServerSocket = new Socket(DESTINATION_HOST, DESTINATION_PORT);
                aServerSocket.setKeepAlive(true);
//            aServerSocket.setSoTimeout(60000);
                System.out.println("start forward client socket to " + DESTINATION_HOST + ":" + DESTINATION_PORT);

                synchronized (aServerSocket) {
                    while (true) {
                        Socket clientSocket = serverSocket.accept();
//                        System.out.println("recive incomming socket connection, start handlethread");
                        ClientThread clientThread
                                = new ClientThread(clientSocket, aServerSocket, DESTINATION_HOST, DESTINATION_PORT);
                        clientThread.start();

                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(SocketForwarding.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            System.err.println("server socket error retry in 10");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                Logger.getLogger(SocketForwarding.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }

    }
}
