package enums;

public enum Prepositions{
    BYTWOSIDES("с обеих сторон"),
    BEHIND("позади"),
    AROUND("вокруг"),
    FROM("из-за"),
    ABOVE("над"),
    ACROSS("по"),
    TO("к"),
    EVERYWHERE("повсюду"),
    UNNOTICED("незаметно");

    private String value;

    Prepositions(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
