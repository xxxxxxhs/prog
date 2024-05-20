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
    public Response execute(Movie obj, String argument, String username) {
        if (obj != null || argument != null) {return new Response("\"command doesn't allow arguments\"");}
        else {
            try {
                if (collectionManager.minIdOwnedBy(username)) {
                    collectionManager.removeFirst();
                    System.out.println("first element was deleted");
                    return new Response("first element was deleted");
                } else return new Response("You are not owner of the first element");
            } catch (EmptyCollection e) {return new Response("collection was already empty");}}
    }
}
