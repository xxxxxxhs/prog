package CollectionClasses;

import Exceptions.IncorrectValueException;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * class Movie, which instances form collection
 */

public class Movie implements Comparable<Movie>, Serializable {
    
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
    private String creatorName;
    public static final long serialVersionUID = 1488228;
    
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
    public Movie(String name, Coordinates coordinates, Integer oscarsCount, long goldenPalmCount, Float totalBoxOffice, MpaaRating mpaaRating, Person screenWriter, String creatorName) throws IncorrectValueException{
        this.creationDate = LocalDateTime.now();
        this.id = ++idCounter;
        setName(name);
        setCoordinates(coordinates);
        setOscarsCount(oscarsCount);
        setGoldenPalmCount(goldenPalmCount);
        setTotalBoxOffic(totalBoxOffice);
        setMpaaRating(mpaaRating);
        setScreenWriter(screenWriter);
        setCreatorName(creatorName);
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
        if (oscarsCount == null) {
            this.oscarsCount = null;
        }
        else if (oscarsCount <= 0){
            throw new IncorrectValueException("Can't be less than 0 or empty");
        }
        else{;
            this.oscarsCount = oscarsCount;
        }
    }
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
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
        if (totalBoxOffice == null) {
            this.totalBoxOffice = null;
        }
        else if (totalBoxOffice > 0){
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
        return id + ". " + name + "\n" +
                "Coordinates: (" + coordinates.getX() + ", " + coordinates.getY() + ")\n" +
                "Created at: " + creationDate + "\n" +
                oscarsCount + " oscars\n" +
                goldenPalmCount + " golden palms\n" +
                "Total box: " + totalBoxOffice + "\n" +
                "Mpaa rating: " + mpaaRating + "\n" +
                "Screenwriter: " + screenwriter.getName();
    }
    @Override
    public int compareTo(Movie mv) {
        return (int) (this.id - mv.id);
    }

    public Boolean validate() {
        if (this.name == null || this.name.equals(""))  {return false;}
        if (this.coordinates == null) {return false;}
        if (this.oscarsCount != null) {if (this.oscarsCount <= 0) {return false;}}
        if (this.goldenPalmCount <= 0) {return false;}
        if (this.totalBoxOffice != null) {if (this.totalBoxOffice <= 0) {return false;}}
        if (this.mpaaRating == null) {return false;}
        if (!this.screenwriter.validate()) {return false;}
        return true;
    }

    public static Boolean staticValidate(String name, float xCor,
                                         double yCor, Integer oscarsCount,
                                         long goldenPalmCount, Float totalBoxOffice,
                                         MpaaRating mpaaRating, String screenwritersName,
                                         String passportID, Color hairColor,
                                         Color eyeColor, Double locX,
                                         Float locY, Long locZ, String locName) {
        if (name == null || name.equals("")) {return false;}
        if (!Coordinates.staticValidate(xCor, yCor)) {return false;}
        if (oscarsCount != null) {if (oscarsCount <= 0) return false;}
        if (goldenPalmCount <= 0) {return false;}
        if (totalBoxOffice != null) {if (totalBoxOffice <= 0) return false;}
        if (mpaaRating == null) return false;
        if (!Person.staticValidate(screenwritersName, passportID,
                eyeColor, hairColor,
                locX, locY, locZ, locName)) {return false;}
        return true;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        Movie mv = (Movie) obj;
        return id == mv.id
                && (name.equals(mv.name))
                && (coordinates.equals(mv.coordinates))
                && (oscarsCount == mv.oscarsCount)
                && (totalBoxOffice == mv.totalBoxOffice)
                && (goldenPalmCount == mv.goldenPalmCount)
                && (mpaaRating.equals(mv.mpaaRating))
                && (screenwriter.equals(mv.screenwriter));
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
    public Person getScreenwriter() {
        return screenwriter;
    }
    public String getCreatorName() {return creatorName;}
}
