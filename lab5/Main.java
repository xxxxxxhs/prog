import Managers.CollectionManager;
import Managers.Commander;
import Managers.Console;
import Managers.Dumper;
import java.io.File;

/*
 * main class, creates console, dumper, managers
 */

public class Main {
    /*
     * @param args  path to json-file
     */
    public static void main(String[] args) {
        //String fileName = "movies.json";
        if (args.length != 1) {
            System.out.println("argument must be a src to json-file");
        } else {
            Console console = new Console();
            Dumper dumper = new Dumper(new File(args[0]).getAbsolutePath(), console);
            CollectionManager collectionManager = new CollectionManager(dumper, console);
            Commander commander = new Commander(console, collectionManager);
            commander.asker();
        }
    }
}
