package Commands;

import CollectionClasses.Movie;
import Managers.CollectionManager;
import Managers.Response;

public class Clear extends ReferenceCommand{
    public Clear(String name, CollectionManager collectionManager){
        super(name, collectionManager);
    }
    @Override
    public Response execute(Movie obj, String argument) {
        if (obj != null || argument != null) {return new Response("\"clear\" doesn't allow arguments");}
        else {collectionManager.clear();
            System.out.println("Collection cleared"); return new Response("Collection cleared");}
    }
}
