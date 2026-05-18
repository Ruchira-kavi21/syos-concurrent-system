package shared.dto;

import java.io.Serializable;

public class Request implements Serializable {
    private String action;
    private String data;

    public Request (String action, String data){
        this.action = action;
        this.data = data;
    }
    public String getAction(){
        return action;
    }
    public String getData(){
        return data;
    }
}
