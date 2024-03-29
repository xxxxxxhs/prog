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
    public Response execute(Movie obj, String argument) {
        if (obj != null) {return new Response("expected 1 argument: id");}
        else{
            try {
                long id = Long.parseLong(argument);
                collectionManager.removeById(id);
                return new Response("removed element " + id);
            } catch (NumberFormatException e) {return new Response("id should be a number");}
            catch (EmptyCollection e) {return new Response("collection is already empty");}
            catch (IdDoesntExists e) {return new Response("element with such id doesn't exists");}
        }
    }
}
