package balbucio.datacrack.V2.client.data;

import balbucio.datacrack.V2.client.Datacrack;
import balbucio.datacrack.V2.client.connector.Server;
import org.json.JSONObject;

import java.util.Date;

public class LinkedDataPack extends DataPack{

    private Server server;

    public LinkedDataPack(String content, boolean autoUpdate, Datacrack instance, Server server) {
        super(content, autoUpdate, instance);
        this.server = server;
    }

    public LinkedDataPack(String name, Datacrack instance, Server server) {
        super(name, instance);
        this.server = server;
    }

    @Override
    public void save() {
        this.put("datapack_lastestdate", new Date().getTime());
        server.updateDataPack(this);
    }

    public void update(){
        try {
            JSONObject json = server.getDataPack(this.name);
            if (json.has("error") && json.getBoolean("error")) {
                return;
            }
            for (String key : json.keySet()) {
                this.put(key, json.get(key));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
