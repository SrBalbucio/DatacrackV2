package balbucio.datacrack.V2.client.connector;

import balbucio.datacrack.V2.client.Datacrack;
import balbucio.datacrack.V2.client.data.DataPack;
import balbucio.datacrack.V2.client.data.DataPackSet;
import balbucio.datacrack.V2.client.data.LinkedDataPack;
import balbucio.datacrack.V2.client.data.MapDataPack;
import balbucio.datacrack.V2.common.accounts.Admin;
import balbucio.datacrack.V2.common.accounts.Credential;
import balbucio.datacrack.V2.lib.procbridge.Client;
import balbucio.datacrack.V2.lib.procbridge.SSLClient;
import org.json.JSONObject;

import java.util.concurrent.CompletableFuture;

public class DatacrackServer extends Server{

    private boolean useSSL = false;
    private Client client;
    private Admin admin;
    private Datacrack instance;

    public DatacrackServer(String ip, int port, Admin admin, boolean ssl, Datacrack instance){
        super(ip, port, admin);
        this.useSSL = ssl;
        this.admin = admin;
        this.instance = instance;
    }

    @Override
    public void start() {
        if(useSSL){
            this.client = new SSLClient(ip, port);
        } else{
            this.client = new Client(ip, port);
        }
    }

    @Override
    public DataPack getOrCreateDataPack(String name) {
        DataPack pack = new DataPack((String) client.request("GETDATAPACK", new Credential(admin).addParameter("path", name).get()), true, instance);
        return pack;
    }

    @Override
    public LinkedDataPack getOrCreateLinkedDataPack(String name) {
        LinkedDataPack pack = new LinkedDataPack((String) client.request("GETDATAPACK", new Credential(admin).addParameter("path", name).get()), true, instance, this);
        return pack;
    }

    @Override
    public DataPackSet getOrCreateDataPackSet(String name) {
        DataPackSet pack = new DataPackSet(name, new JSONObject((String) client.request("GETDATAPACKSET", new Credential(admin).addParameter("path", name).get())), instance);
        return pack;
    }

    @Override
    public MapDataPack getOrCreateMapDataPack(String name) {
        MapDataPack pack = new MapDataPack(new JSONObject((String) client.request("GETMAPDATAPACK", new Credential(admin).addParameter("path", name).get())), instance);
        return pack;
    }

    @Override
    public JSONObject getDataPack(String name){
        return new JSONObject((String) client.request("GETDATAPACK", new Credential(admin).addParameter("path", name).get()));
    }

    @Override
    public JSONObject getLinkedDataPack(String name) {
        return new JSONObject((String) client.request("GETDATAPACK", new Credential(admin).addParameter("path", name).get()));
    }

    @Override
    public JSONObject getDataPackSet(String name) {
        return new JSONObject((String) client.request("GETDATAPACKSET", new Credential(admin).addParameter("path", name).get()));
    }

    @Override
    public JSONObject getMapDataPack(String name) {
        return new JSONObject((String) client.request("GETMAPDATAPACK", new Credential(admin).addParameter("path", name).get()));
    }

    @Override
    public boolean existsDataPack(String name) {
        return new JSONObject((String) client.request("EXISTDATAPACK", new Credential(admin).addParameter("path", name).get())).getBoolean("exists");
    }

    @Override
    public boolean existsLinkedDataPack(String name) {
        return new JSONObject((String) client.request("EXISTDATAPACK", new Credential(admin).addParameter("path", name).get())).getBoolean("exists");
    }

    @Override
    public boolean existsMapDataPack(String name) {
        return new JSONObject((String) client.request("EXISTMAPDATAPACK", new Credential(admin).addParameter("path", name).get())).getBoolean("exists");
    }

    @Override
    public void updateDataPack(DataPack dataPack) {
        CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                client.request("UPDATEDATAPACK", new Credential(admin).addParameter("path", dataPack.getName()).addParameter("jsonContent", dataPack).get());
            }
        });
    }

    @Override
    public void updateLinkedDataPack(LinkedDataPack dataPack) {
        CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                client.request("UPDATEDATAPACK", new Credential(admin).addParameter("path", dataPack.getName()).addParameter("jsonContent", dataPack).get());
            }
        });
    }

    @Override
    public void updateDataPackSet(String name, JSONObject json) {
        CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                client.request("UPDATEDATAPACKSET", new Credential(admin).addParameter("path", name).addParameter("jsonContent", json).get());
            }
        });
    }

    @Override
    public void updateMapDataPack(MapDataPack dataPack) {
        CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                client.request("UPDATEMAPDATAPACK", new Credential(admin).addParameter("path", dataPack.getName()).addParameter("jsonContent", dataPack).get());
            }
        });
    }
}
