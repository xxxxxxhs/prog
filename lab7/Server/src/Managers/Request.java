package Managers;

import CollectionClasses.Movie;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;

public class Request implements Serializable {
    String command;
    Movie movie;
    String argument;
    private String username;
    private String password;
    public static final long SerialVersionUID = 10l;

    public Request(String command, Movie movie, String argument, String username, String password) {
        this.movie = movie;
        this.argument = argument;
        this.command = command;
        this.username = username;
        this.password = password;
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
    public String getUsername() {return username;}
    public String getHashPassword() {return password;}

    @Override
    public String toString() {
        return "Request{" +
                "command='" + command + '\'' +
                ", movie=" + movie +
                ", argument='" + argument + '\'' +
                '}';
    }
}
