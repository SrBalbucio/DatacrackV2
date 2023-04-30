package balbucio.datacrack.V2.client.data;

import balbucio.datacrack.V2.client.Datacrack;
import balbucio.datacrack.V2.client.connector.Server;
import org.json.JSONObject;

import java.util.Date;

public class DataPack extends JSONObject {

    public String name;
    public boolean autoUpdate = false;
    public Datacrack instance;

    public DataPack(String content, boolean autoUpdate, Datacrack instance){
        super(content);
        this.name = this.getString("datapack_name");
        this.autoUpdate = autoUpdate;
        this.instance = instance;
    }

    public DataPack(String name, Datacrack instance){
        super();
        this.name = name;
        this.put("datapack_name", name).put("datapack_lastestdate", new Date().getTime());
        this.instance = instance;
    }

    public String getName() {
        return name;
    }


    public void save(){
        this.put("datapack_lastestdate", new Date().getTime());
        for(Server servers : instance.getServers()){
            servers.updateDataPack(this);
        }
    }

    public void update(){
        JSONObject json = instance.searchAndGetDataPack(name);
        for(String key : json.keySet()){
            this.put(key, json.get(key));
        }
    }
}
