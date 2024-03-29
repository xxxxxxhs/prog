package Commands;

import CollectionClasses.Movie;
import Exceptions.EmptyCollection;
import Managers.CollectionManager;
import Managers.Response;

public class RemoveFirst extends ReferenceCommand{
    public RemoveFirst(String name, CollectionManager collectionManager) {
        super(name, collectionManager);
    }
    @Override
    public Response execute(Movie obj, String argument) {
        if (obj != null || argument != null) {return new Response("\"command doesn't allow arguments\"");}
        else {try {collectionManager.removeFirst(); return new Response("first element was deleted");}
            catch (EmptyCollection e) {return new Response("collection was already empty");}}
    }
}
