package Managers;

import CollectionClasses.Movie;
import Exceptions.EmptyCollection;
import Exceptions.IdDoesntExists;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

/*
 * class Collection manager is need to manage linked list of Movie
 */

public class CollectionManager {
    
    private LocalDateTime initTime;
    private SafeLinkedList<Movie> collection = new SafeLinkedList<>();
    private SqlDumper dumper;
    private LinkedList<String> history = new LinkedList<>();

    public CollectionManager(SqlDumper dumper) {
        this.dumper = dumper;
        this.initTime = LocalDateTime.now();
        update();
    }

    /*
     * @return text of info-command
     */
    public String getInfo() {
        return "Init date: " + initTime + "\n" +
        "Collection type is Linked list of movies\n" +
        "Contains " + collection.size() + (collection.size() == 1 ? " element" : " elements");
    }

    /*
     * @return elements of collection
     */
    public LinkedList<Movie> show() {
        return collection.show();
    }

    /*
     * @param mv    new Movie element
     * adds new Movie generated with Ask.class into the collection
     */
    public void add(Movie movie, String username) {
        try {
            dumper.addMovie(movie, username);
            update();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /* 
     * @param mv    new Movie-element
     * @param id    id of old Movie-element
     * updates Movie-element with Ask.class according to its id
     */
    public void updateById(Movie mv, long id) {
        if (getById(id) == null) {
            //console.printError("element with this id doesn't exist");
        } else {
            dumper.update(mv, id);
            update();
        }
    }

    /*
     * @param id
     * @return bool-value if element with such id exists
     */
    public boolean isExistById(long id) {
        return collection.isExistById(id);
    }

    /*
     * @param id
     * @return collection-element with specified id
     */
    public Movie getById(long id) {
        return collection.getById(id);
    }

    /*
     * @param id
     * removes element with specified id
     */
    public void removeById(long id) throws EmptyCollection, IdDoesntExists{
        if (collection == null || collection.isEmpty()) {
            throw new EmptyCollection("collection is empty");
        }
        if (!collection.isExistById(id)) {
            throw new IdDoesntExists("Such id doesn't exists");
        }
        dumper.removeById(id);
        update();
    }

    /*
     * clears collection
     */
    public void clear(String username) {
        dumper.clear(username);
        update();
    }

    /*
     * saves collection into json-file
     */
    //public void save() {dumper.save(collection);}

    /*
     * removes first element of the collection
     */
    public void removeFirst() throws EmptyCollection {
        if (!collection.isEmpty()) {
            dumper.removeFirst();
            update();
        } else {throw new EmptyCollection("collection is already empty");}
        // else {console.printError("collection is empty");}
    }

    /*
     * @return first element of the collection
     */
    public Movie head() throws EmptyCollection {
        try {
            return collection.getFirst();
        } catch (NoSuchElementException e) {
            throw new EmptyCollection("collection is empty");
            //console.printError("collection is empty");
        }
    }

    /*
     * @param goldenPalmCount
     * @returns amount of collection elements with specified value of goldenPalmCount
     */
    public int countByGoldenPalmCount(long goldenPalmCount) {
        return collection.countByGoldenPalmCount(goldenPalmCount);
    }
    public boolean minIdOwnedBy(String username) {
        return collection.minIdOwnedBy(username);
    }
    /*
     * @return string of unique values of oscarsCount
     */
    public String printUniqueOscarsCount() {
        return collection.printUniqueOscarsCount();
    }

    /*
     * @return string of descending values of mpaa rating of existing collection elements
     */
    public String printFieldDescendingMpaaRating() {
        return collection.printFieldDescendingMpaaRating();
    }

    /*
     * @param id
     * @return bool-value if specified id unique or not
     */
    public Boolean isIdUnique(long id) {
        return collection.isIdUnique(id);
    }

    /*
     * @return new unique id
     */
    public long formNewId() {
        long id = 1;
        while (!isIdUnique(id)) {id++;}
        return id;
    }
    public void sort() {
        collection.sort();
    }
    public void addToHistory(String command) {
        if (history.size() < 13) {history.add(command);}
        else {history.removeFirst(); history.add(command);}
    }
    public String getHistory(){return String.join("\n", history.toArray(new String[0]));}
    private void update() {
        collection.update(dumper);
    }
    public Movie getFirst(){
        return collection.getFirst();
    }
}
