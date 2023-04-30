package balbucio.datacrack.V2.client.data;

import org.json.JSONObject;

public class UniqueDataPack extends JSONObject {

    private String name = "Not Set";

    public UniqueDataPack(String name, JSONObject payload){
        super(payload.toString());
        this.name = name;
    }

    public UniqueDataPack(String name){
        super();
        this.name = name;
    }

    public UniqueDataPack(){
        super();
    }

    public String getName(){
        return name;
    }
}
