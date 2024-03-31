package Managers;

import java.io.Serializable;

public class Response implements Serializable {
    public static final long SerialVersionUID = 11l;
    private String answer;
    public Response(String answer) {
        this.answer = answer;
    }
    public String getAnswer(){return answer;}
}
