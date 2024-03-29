package Commands;

import CollectionClasses.Movie;
import Exceptions.EmptyCollection;
import Managers.CollectionManager;
import Managers.Response;

public class Head extends ReferenceCommand {
    public Head (String name, CollectionManager collectionManager) {
        super(name, collectionManager);
    }
    @Override
    public Response execute(Movie obj, String argument) {
        if (obj != null || argument != null) {return new Response("command doesn't allow arguments");}
        else {
            try {return new Response(collectionManager.head().toString());}
            catch (EmptyCollection e) {return new Response("collection is already empty");}}
    }
}
