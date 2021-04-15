import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class Handler {
    protected int port;
    protected String request,response;
    protected List<String> wordList = new LinkedList<String>();
    protected Socket client, service;
    protected ServerSocket serverSocket;
    protected ObjectOutputStream out;
    protected ObjectInputStream in;

    public Handler(int port){
        this.port = port;
    }

    public void start(){
        try {
            serverSocket = new ServerSocket(port);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    protected void stop() {
        try {
            client.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
