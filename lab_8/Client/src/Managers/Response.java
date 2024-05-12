package Managers;

import CollectionClasses.Movie;

import java.io.Serializable;
import java.util.LinkedList;

public class Response implements Serializable {
    public static final long SerialVersionUID = 11l;
    private String answer;
    private LinkedList<Movie> collection;
    private Boolean containsCollection;
    public Response(String answer) {
        this.answer = answer;
        containsCollection = false;
    }
    public Response(LinkedList<Movie> collection) {
        this.collection = collection;
        containsCollection = true;
    }
    public String getAnswer(){return answer;}
    public LinkedList<Movie> getCollection(){
        return collection;
    }
    public Boolean isContainCollection() {
        return containsCollection;
    }
}
