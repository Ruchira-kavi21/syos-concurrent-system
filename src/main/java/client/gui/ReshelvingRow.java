package client.gui;

import java.io.Serializable;

public class ReshelvingRow implements Serializable {

    private final String code;
    private final String name;
    private final int shelfQuantity;
    private final int storeQuantity;

    public ReshelvingRow(
            String code,
            String name,
            int shelfQuantity,
            int storeQuantity
    )
    {

        this.code = code;
        this.name = name;
        this.shelfQuantity = shelfQuantity;
        this.storeQuantity = storeQuantity;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getShelfQuantity() {
        return shelfQuantity;
    }

    public int getStoreQuantity() {
        return storeQuantity;
    }
}