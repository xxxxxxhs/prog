package CollectionClasses;

import Exceptions.IncorrectValueException;

import java.io.Serializable;

/*
 * class coordinates
 */

public class Coordinates implements Serializable {
    
    private float x; //Значение поля должно быть больше -170
    private double y;
    public static final long SerialVersionUID = 1234288;
    
    public Coordinates(float x, double y) throws IncorrectValueException{
        setX(x);
        setY(y);
    }
    public void setX(float x) throws IncorrectValueException{
        if (x <= -170){
            throw new IncorrectValueException("Must be more than -170");
        }
        else{
            this.x = x;
        }
    }
    public void setY(double y){
        this.y = y;
    }
    public float getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    @Override
    public String toString(){
        return "(x == " + x + ", y == " + y + ")";
    }
}
