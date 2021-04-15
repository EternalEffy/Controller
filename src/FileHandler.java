import java.io.*;
import java.net.Socket;
import java.util.*;

public class FileHandler extends Handler{
    private String fileName,file;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;


    public FileHandler(String fileName,int port){
        super(port);
        this.fileName = fileName;
    }

    @Override
    public void start(){
        super.start();
        System.out.println("Server started");
        while(true) {
            try {
                client = serverSocket.accept();
                    inputStream = new DataInputStream(client.getInputStream());
                    outputStream = new DataOutputStream(client.getOutputStream());
                    request = inputStream.readUTF();
                    System.out.println("REQUEST: "+request);
                    if (request.startsWith(Command.searchWord)) {
                        sendRequest(request.substring(7));
                        outputStream.writeUTF(response);
                        outputStream.flush();
                    } else {
                        stop();
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
            Searcher searcher = new Searcher(4646);
            new Thread(searcher).start();
            service = new Socket("localhost", searcher.port);
            out = new ObjectOutputStream(service.getOutputStream());
            in = new ObjectInputStream(service.getInputStream());
            out.writeUTF("search");
            out.writeObject(wordList);
            out.writeUTF(key);
            out.flush();
            response = in.readUTF();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
