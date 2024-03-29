package Commands;

import CollectionClasses.Movie;
import Managers.CollectionManager;
import Managers.Response;

public class PrintFieldDescendingMpaaRating extends ReferenceCommand {
    public PrintFieldDescendingMpaaRating (String name, CollectionManager collectionManager) {
        super(name, collectionManager);
    }
    @Override
    public Response execute(Movie obj, String argument) {
        if(obj != null || argument != null) {
            return new Response("command doesn't allow arguments");
        } else {return new Response(collectionManager.printFieldDescendingMpaaRating());}
    }
}
