import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class FileHandler {
    private String request,response,fileName,file;
    private int port;
    List<String> wordList = new LinkedList<String>();
    private Socket client, service;
    private ServerSocket serverSocket;
    private ObjectOutputStream outputStream;
    private DataInputStream inputStream;


    public FileHandler(String fileName,int port){
        this.fileName = fileName;
        this.port = port;
    }

    public void start(){
        System.out.println("Server controller started");
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                client = serverSocket.accept();
                DataInputStream in = new DataInputStream(client.getInputStream());
                DataOutputStream out = new DataOutputStream(client.getOutputStream());
                    request = in.readUTF();
                    System.out.println(request);
                    if(request.startsWith(Command.searchWord)) {
                        sendRequest(request.substring(7));
                        out.writeUTF(response);
                        out.flush();
                    }else {
                        stop();
                        return;
                    }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stop() {
        try {
            service = new Socket("localhost",4646);
            outputStream = new ObjectOutputStream(service.getOutputStream());
            outputStream.writeUTF("stop");
            outputStream.flush();
            serverSocket.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFile(){
        try {
            Scanner reader = new Scanner(new File(fileName));
            while(reader.hasNext()){
                file = reader.nextLine();
            }
            String [] strings = file.split(";");
            for(String s : strings){
                wordList.add(s);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void sendRequest(String key){
        try {
            service = new Socket("localhost",4646);
            outputStream = new ObjectOutputStream(service.getOutputStream());
            inputStream = new DataInputStream(service.getInputStream());
            outputStream.writeUTF("search");
            outputStream.writeObject(wordList);
            outputStream.writeUTF(key);
            outputStream.flush();
            response = inputStream.readUTF();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
