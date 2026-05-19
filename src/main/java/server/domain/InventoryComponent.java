package server.domain;

import java.io.Serializable;

public abstract class InventoryComponent implements Serializable {
    protected String name;
    protected String code;

    protected InventoryComponent (String name, String code){
        this.name = name;
        this.code = code;
    }
    public abstract void displayInfo();

    public String getCode(){
        return code;
    }
    public String getName(){
        return name;
    }

}