package Commands;

import CollectionClasses.Movie;
import Managers.CollectionManager;
import Managers.Response;

public class Clear extends ReferenceCommand{
    public Clear(String name, CollectionManager collectionManager){
        super(name, collectionManager);
    }
    @Override
    public Response execute(Movie obj, String argument, String username) {
        if (obj != null || argument != null) {return new Response("\"clear\" doesn't allow arguments");}
        else {collectionManager.clear(username);
            System.out.println("Elements owned by " + username + " deleted"); return new Response("Your own elements deleted");}
    }
}
