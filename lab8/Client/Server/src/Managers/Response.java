package Managers;

import CollectionClasses.Movie;

import java.io.Serializable;
import java.util.LinkedList;

public class Response implements Serializable {
    public static final long SerialVersionUID = 11l;
    private String answer;
    private LinkedList<Movie> collection;
    private Boolean containsCollection, hasNext;
    private int totalParts, partNumber;
    public Response(String answer) {
        this.answer = answer;
        containsCollection = false;
        this.totalParts = 1;
        this.partNumber = 1;
        this.hasNext = false;
    }
    public Response(LinkedList<Movie> collection) {
        this.collection = collection;
        containsCollection = true;
        this.totalParts = 1;
        this.partNumber = 1;
        this.hasNext = false;
    }
    public Response(LinkedList<Movie> collection, int totalParts, int partNumber, boolean hasNext) {
        this(collection);
        this.totalParts = totalParts;
        this.partNumber = partNumber;
        this.hasNext = hasNext;
    }
    public String getAnswer(){return answer;}
    public LinkedList<Movie> getCollection(){
        return collection;
    }
    public Boolean isContainCollection() {
        return containsCollection;
    }
    public int getTotalParts() {return totalParts;}
    public int getPartNumber() {return partNumber;}
    public Boolean hasNext() {return hasNext;}
    public static LinkedList<Response> split(LinkedList<Movie> collection) {
        LinkedList<Response> responses = new LinkedList<>();
        final int MAX_SIZE = 300;
        int totalParts = (int) Math.ceil((double) collection.size() / MAX_SIZE); // Расчет общего количества частей
        int partNumber = 1; // Номер текущей части начинается с 1

        int index = 0;
        while (index < collection.size()) {
            // Calculate the end index for the sublist (exclusive)
            int end = Math.min(index + MAX_SIZE, collection.size());
            LinkedList<Movie> subList = new LinkedList<>(collection.subList(index, end));
            // Determine if there is a next list
            boolean hasNext = end < collection.size();
            // Создаем ответ с текущей подколлекцией, общим количеством частей и номером текущей части
            responses.add(new Response(subList, totalParts, partNumber, hasNext));
            index += MAX_SIZE;
            partNumber++; // Увеличиваем номер части
        }
        return responses;
    }

}
