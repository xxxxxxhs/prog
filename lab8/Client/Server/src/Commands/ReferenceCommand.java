package Commands;

import CollectionClasses.Movie;
import Managers.CollectionManager;
import Managers.Response;

public abstract class ReferenceCommand implements Executable{
    String name;
    CollectionManager collectionManager;
    public ReferenceCommand(String name, CollectionManager collectionManager) {
        this.name = name;
        this.collectionManager = collectionManager;
    }
    @Override
    public Response execute(Movie obj, String argument, String username) {return null;}
    public String getName() {return name;}
}
