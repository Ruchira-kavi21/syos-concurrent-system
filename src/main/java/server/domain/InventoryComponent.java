package server.domain;

public abstract class InventoryComponent {
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