package Commands;

import CollectionClasses.Movie;
import Exceptions.EmptyCollection;
import Exceptions.IdDoesntExists;
import Managers.CollectionManager;
import Managers.Response;

public class RemoveById extends ReferenceCommand{
    public RemoveById(String name, CollectionManager collectionManager) {
        super(name, collectionManager);
    }
    @Override
    public Response execute(Movie obj, String argument, String username) {
        if (obj != null) {return new Response("expected 1 argument: id");}
        else{
            try {
                long id = Long.parseLong(argument);
                if (collectionManager.getById(id) == null) {
                    return new Response("Such element doesn't exist");
                }
                else if(collectionManager.getById(id).getCreatorName().equals(username)){
                    collectionManager.removeById(id);
                    System.out.println("removed element " + id);
                    return new Response("removed element " + id);
                } else {
                    return new Response("You are not this movie owner");
                }
            } catch (NumberFormatException e) {return new Response("id should be a number");}
            catch (EmptyCollection e) {return new Response("collection is already empty");}
            catch (IdDoesntExists e) {return new Response("element with such id doesn't exists");}
        }
    }
}
