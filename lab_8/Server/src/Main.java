import Managers.*;

import java.net.SocketException;

public class Main {
    public static void main(String[] args) {
/*
        if (args.length != 3) {
            new Console().println("expected 3 arguments");
            System.out.println(args.length);
            System.exit(-1);
        }

 */
        //String url = "jdbc:postgresql://pg:5432/studs";
        //String user = "s409870";
        //String password = "SXaqjlmUAlOlJfga";
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "123";
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