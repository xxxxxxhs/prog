import Managers.CollectionManager;
import Managers.Commander;
import Managers.Dumper;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        //String fileName = "/Users/fqy/edu/prog/lab6/movies.json";
        if (args.length != 1) {
            System.out.println("expexted 1 argument");
            System.exit(-1);
        }
        String fileName = args[0];
        Dumper dumper = new Dumper(fileName);
        CollectionManager collectionManager = new CollectionManager(dumper);
        try {
            Server server = new Server(new Commander(collectionManager));
            server.receive();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}