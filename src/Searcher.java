import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;

public class Searcher extends Handler implements Runnable{
    protected int count,port;
    List<String> wordList = new LinkedList<String>();
    private String keyWord;

    public Searcher(int port) {
        super(port);
        this.port = port;
    }

    @Override
    public void start() {
        super.start();
        System.out.println("Searcher started");
        while (true) {
            try {
                client = serverSocket.accept();
                in = new ObjectInputStream(client.getInputStream());
                out = new ObjectOutputStream(client.getOutputStream());
                request = in.readUTF();
                Object object = in.readObject();
                wordList = (LinkedList<String>) object;
                keyWord = in.readUTF();
                out.writeUTF(search());
                out.flush();
                count = 0;
                stop();
                return;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private String search() {
        for (String str : wordList) {
            count = (str.equals(keyWord)) ? count+1 : count;
        }
        return "Count of search word " + keyWord + " = " + count;
    }

    @Override
    public void run() {
        start();
    }
}
