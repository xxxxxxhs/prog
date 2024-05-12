package CollectionClasses;

/*
 * class Person
 * describes movie's screenwriter
 */

import Exceptions.IncorrectValueException;

import java.io.Serializable;

public class Person implements Serializable {
    
    private String name; //Поле не может быть null, Строка не может быть пустой
    private String passportID; //Поле не может быть null
    private Color eyeColor; //Поле может быть null
    private Color hairColor; //Поле не может быть null
    private Location location; //Поле может быть null
    public static final long SerialVersionUID = 123488;
    
    public Person (String name, String passportID, Color eyeColor, Color hairColor, Location locantion) throws IncorrectValueException{
        setName(name);
        setPassportID(passportID);
        setEyeColor(hairColor);
        setHairColor(hairColor);
        setLocation(locantion);
    }
    public String getName(){
        return name;
    }
    public String getPassportID(){
        return passportID;
    }
    public Color getEyeColor(){
        return eyeColor;
    }
    public Color getHairColor(){
        return hairColor;
    }
    public Location getLocation(){
        return location;
    }
    public void setName(String name) throws IncorrectValueException{
        if (name == null || name.trim() == ""){
            throw new IncorrectValueException("Can't be empty");
        }
        else{    
            this.name = name;
        }
    }
    public void setPassportID(String passportID) throws IncorrectValueException{
        if (passportID == null){
            throw new IncorrectValueException("Can't be empty");
        }
        else{
            this.passportID = passportID;
        }
    }
    public void setEyeColor(Color color) {
        this.eyeColor = color;
    }
    public void setHairColor(Color color) throws IncorrectValueException{
        if (color == null){
            throw new IncorrectValueException("Cam't be empty");
        }
        else{
            this.hairColor = color;
        }
    }
    public void setLocation(Location location){
        this.location = location;
    }
    @Override
    public String toString(){
        return name + "\n";
    }
    public boolean validate() {
        if (this.name == null || this.name.equals("")) {return false;}
        if (this.passportID == null) {return false;}
        if (this.hairColor == null) {return  false;}
        if (!this.location.validate()) {return false;}
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
        Person pr = (Person) obj;
        return name.equals(pr.name)
                && passportID.equals(pr.passportID)
                && location.equals(pr.location)
                && eyeColor.equals(pr.eyeColor)
                && hairColor.equals(pr.hairColor);
    }
    public String getPassportId() {
        return passportID;
    }
    public static Boolean staticValidate(String name, String passportID,
                                         Color eyeColor, Color hairColor,
                                         Double x, Float y, Long z, String locName) {
        if (name == null || name.equals("")) return false;
        if (passportID == null) return false;
        if (eyeColor == null) return false;
        if (hairColor == null) return false;
        if (!Location.staticValidate(x, y, z, locName)) return false;
        return true;
    }
}

