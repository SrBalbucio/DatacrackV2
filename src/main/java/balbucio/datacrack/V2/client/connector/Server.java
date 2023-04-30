package balbucio.datacrack.V2.client.connector;

import balbucio.datacrack.V2.client.data.DataPack;
import balbucio.datacrack.V2.client.data.DataPackSet;
import balbucio.datacrack.V2.client.data.LinkedDataPack;
import balbucio.datacrack.V2.client.data.MapDataPack;
import balbucio.datacrack.V2.common.accounts.Admin;
import org.json.JSONObject;

public abstract class Server {

    public String ip;
    public int port;
    public Admin admin;

    public Server(String ip, int port, Admin admin){
        this.ip = ip;
        this.port = port;
        this.admin = admin;
    }

    public Server(){}

    public abstract void start();

    public abstract DataPack getOrCreateDataPack(String name);
    public abstract LinkedDataPack getOrCreateLinkedDataPack(String name);
    public abstract DataPackSet getOrCreateDataPackSet(String name);
    public abstract MapDataPack getOrCreateMapDataPack(String name);
    public abstract JSONObject getDataPack(String name);
    public abstract JSONObject getLinkedDataPack(String name);
    public abstract JSONObject getDataPackSet(String name);
    public abstract JSONObject getMapDataPack(String name);
    public abstract boolean existsDataPack(String name);
    public abstract boolean existsLinkedDataPack(String name);
    public abstract boolean existsMapDataPack(String name);
    public abstract void updateDataPack(DataPack dataPack);
    public abstract void updateLinkedDataPack(LinkedDataPack dataPack);
    public abstract void updateDataPackSet(String name, JSONObject json);
    public abstract void updateMapDataPack(MapDataPack dataPack);
}
