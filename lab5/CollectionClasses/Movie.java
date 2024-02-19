package CollectionClasses;
import Exceptions.*;
import java.time.LocalDateTime;

/**
 * class Movie, which instances form collection
 */

public class Movie implements Comparable<Movie>{
    
    private static long idCounter = 0;
    private long id;//Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Integer oscarsCount; //Значение поля должно быть больше 0, Поле может быть null
    private long goldenPalmCount; //Значение поля должно быть больше 0
    private Float totalBoxOffice; //Поле может быть null, Значение поля должно быть больше 0
    private MpaaRating mpaaRating; //Поле не может быть null
    private Person screenwriter;
    
    /*
     * class constructor
     * 
     * @param name  name
     * @param coordinates   coordinates
     * @param oscarsCount   amount of oscars
     * @param goldenPalmCount   amount of golden palms
     * @param totalBoxOffice    total box office
     * @param mpaaRating    mpaa rating
     * @param screenwriter  movie's screenwriter
     */
    public Movie(String name, Coordinates coordinates, Integer oscarsCount, long goldenPalmCount, Float totalBoxOffice, MpaaRating mpaaRating, Person screenWriter) throws IncorrectValueException{
        this.creationDate = LocalDateTime.now();
        this.id = ++idCounter;
        setName(name);
        setCoordinates(coordinates);
        setOscarsCount(oscarsCount);
        setGoldenPalmCount(goldenPalmCount);
        setTotalBoxOffic(totalBoxOffice);
        setMpaaRating(mpaaRating);
        setScreenWriter(screenWriter);
    }
    public void setName(String name) throws IncorrectValueException{
        if (name == null || name.trim() == ""){
            throw new IncorrectValueException("Can't be empty");
        }
        else{    
            this.name = name;
        }
    }
    public void setCoordinates(Coordinates coordinates) throws IncorrectValueException{
        if (coordinates == null){
            throw new IncorrectValueException("X-cor can't be less than -170");
        }
        else if(coordinates.getX() <= -170){
            throw new IncorrectValueException("Coordinates can't be empty");
        }
        else{
            this.coordinates = coordinates;
        }
    }
    public void setOscarsCount(Integer oscarsCount) throws IncorrectValueException{
        if (oscarsCount <= 0 || oscarsCount == null){
            throw new IncorrectValueException("Can't be less than 0 or empty");
        }
        else{
            this.oscarsCount = oscarsCount;
        }
    }
    public void setGoldenPalmCount(long goldenPalmCount) throws IncorrectValueException{
        if (goldenPalmCount <= 0){
            throw new IncorrectValueException("Can't be less than 0");
        }
        else{
            this.goldenPalmCount = goldenPalmCount;
        }
    }
    public void setTotalBoxOffic(Float totalBoxOffice) throws IncorrectValueException{
        if (totalBoxOffice == null || totalBoxOffice > 0){
            this.totalBoxOffice = totalBoxOffice;
        }
        else{
            throw new IncorrectValueException("Can't be less than 0");
        }
    }
    public void setMpaaRating(MpaaRating mpaaRating) throws IncorrectValueException{
        if (mpaaRating == null){
            throw new IncorrectValueException("Can't be empty");
        }
        else{
            this.mpaaRating = mpaaRating;
        }
    }
    public void setScreenWriter(Person screenWriter){
        this.screenwriter = screenWriter;
    }
    public String getName(){
        return name;
    }
    public long getId(){
        return id;
    }
    public Coordinates getCoordinates(){
        return coordinates;
    }
    public LocalDateTime getCreationDate(){
        return creationDate;
    }
    public Integer getOscarsCount(){
        return oscarsCount;
    }
    public long getGoldenPalmCount(){
        return goldenPalmCount;
    }
    public Float getTotalBoxOffice(){
        return totalBoxOffice;
    }
    public MpaaRating getMpaaRating(){
        return mpaaRating;
    }
    public Person getScreenWriter(){
        return screenwriter;
    }
    public void setId(long id) {
        this.id = id;
    }
    @Override
    public String toString(){
        return "Name: " + name + "\n" + 
        "Id: " + id + "\n" +
        "Coordinates: " + coordinates.toString() + "\n" +
        "Creation date: " + creationDate.toString() + "\n" +
        "Oscars amount: " + oscarsCount + "\n" +
        "Golden palms\' amount: " + goldenPalmCount + "\n" +
        "Total box office: " + totalBoxOffice + "\n" +
        "Mpaa rating: " + mpaaRating + "\n" +
        "Screenwriter: " + screenwriter.toString();
    }
    @Override
    public int compareTo(Movie mv) {
        return (int) (this.getId() - mv.getId());
    }
}
