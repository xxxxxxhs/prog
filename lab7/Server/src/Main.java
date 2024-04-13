import Managers.*;

import java.net.SocketException;

public class Main {
    public static void main(String[] args) {
        if (args.length != 3) {
            new Console().println("expected 3 arguments");
            System.exit(-1);
        }
        String url = args[0];
        String user = args[1];
        String password = args[2];
        SqlDumper sqlDumper = new SqlDumper(url, user, password);
        CollectionManager collectionManager = new CollectionManager(sqlDumper);
        SqlUserManager sqlUserManager = new SqlUserManager(url, user, password);
        Commander commander = new Commander(collectionManager, sqlUserManager);
        try {
            Server server = new Server(commander);
            server.start();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }
}