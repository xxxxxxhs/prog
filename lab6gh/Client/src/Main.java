import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Client client = new Client();
            client.readUserCommand();
            System.out.println("shut down");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}