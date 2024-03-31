package Managers;

import CollectionClasses.Movie;

import java.io.Serializable;

public class Request implements Serializable {
    String command;
    Movie movie;
    String argument;
    public static final long SerialVersionUID = 10l;

    public Request(String command, Movie movie, String argument) {
        this.movie = movie;
        this.argument = argument;
        this.command = command;
    }
    public String getCommand() {
        return command;
    }
    public String getArgument() {
        return argument;
    }
    public Movie getMovie() {
        return movie;
    }

    @Override
    public String toString() {
        return "Request{" +
                "command='" + command + '\'' +
                ", movie=" + movie +
                ", argument='" + argument + '\'' +
                '}';
    }
}
