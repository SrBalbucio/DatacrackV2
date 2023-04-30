package balbucio.datacrack.V2.client.data;

import balbucio.datacrack.V2.client.Datacrack;
import balbucio.datacrack.V2.client.connector.Server;
import org.checkerframework.common.aliasing.qual.Unique;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DataPackSet {

    public List<UniqueDataPack> packs = new ArrayList<>();
    public String name;
    public Datacrack instance;

    public DataPackSet(String name, Datacrack instance){
        this.name = name;
        this.instance = instance;
    }

    public DataPackSet(String name, JSONObject payload, Datacrack instance){
        this.name = name;
        this.instance = instance;
        for(String key : payload.keySet()){
            packs.add(new UniqueDataPack(key, payload.getJSONObject(key)));
        }
    }

    public void add(UniqueDataPack pack){
        packs.add(pack);
    }

    public void remove(UniqueDataPack pack){
        packs.remove(pack);
    }

    public void save(){
        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put("datapackset_name", name);
        jsonPayload.put("datapackset_lastestdate", new Date().getTime());
        for(UniqueDataPack pack : packs){
            jsonPayload.put(pack.getName(), pack);
        }
    }
}
