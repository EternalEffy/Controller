public class Main {
    public static void main(String[] args) {
        FileHandler test = new FileHandler("test.txt",4545);
        test.loadFile();
        test.start();
    }
}
