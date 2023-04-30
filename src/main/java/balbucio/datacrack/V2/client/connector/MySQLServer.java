package balbucio.datacrack.V2.client.connector;

import balbucio.datacrack.V2.client.Datacrack;
import balbucio.datacrack.V2.client.data.DataPack;
import balbucio.datacrack.V2.client.data.DataPackSet;
import balbucio.datacrack.V2.client.data.LinkedDataPack;
import balbucio.datacrack.V2.client.data.MapDataPack;
import balbucio.datacrack.V2.common.accounts.Admin;
import balbucio.datacrack.V2.common.database.mysql.SQL;
import org.json.JSONObject;

public class MySQLServer extends Server{

    private SQL sql;
    private String database, tlsVersion, ssl, driver;
    private Datacrack instance;

    public MySQLServer(String ip, int port, Admin admin, String database, String driver, String tlsVersion, String ssl, Datacrack instance){
        super(ip, port, admin);
        this.tlsVersion = tlsVersion;
        this.ssl = ssl;
        this.driver = driver;
        this.database = database;
        this.instance = instance;
    }

    @Override
    public void start() {
        this.sql = new SQL(this.ip, this.admin.getUsername(), this.admin.getPassword(), this.database, String.valueOf(this.port), this.driver, this.tlsVersion, this.ssl);
        sql.connect();
        sql.createTable("DatacrackDataPacks", "name VARCHAR(255), content TEXT");
        sql.createTable("DatacrackMaps", "name VARCHAR(255), content TEXT");
    }

    @Override
    public DataPack getOrCreateDataPack(String name) {
        if(sql.exists("name", name, "DatacrackDataPacks")){
            return new DataPack(sql.getText("content", "name", "=", name, "DatacrackDataPacks"), true, instance);
        } else{
            return new DataPack(name, instance);
        }
    }

    @Override
    public LinkedDataPack getOrCreateLinkedDataPack(String name) {
        if(sql.exists("name", name, "DatacrackDataPacks")){
            return new LinkedDataPack(sql.getText("content", "name", "=", name, "DatacrackDataPacks"), true, instance, this);
        } else{
            return new LinkedDataPack(name, instance, this);
        }
    }

    @Override
    public DataPackSet getOrCreateDataPackSet(String name) {
        return null;
    }

    @Override
    public MapDataPack getOrCreateMapDataPack(String name) {
        if(sql.exists("name", name, "DatacrackMaps")){
            return new MapDataPack(new JSONObject(sql.getText("content", "name", "=", name, "DatacrackMaps")), instance);
        } else{
            return new MapDataPack(name, instance);
        }
    }

    @Override
    public JSONObject getDataPack(String name) {
        return new JSONObject(sql.getText("content", "name", "=", name, "DatacrackDataPacks"));
    }

    @Override
    public JSONObject getLinkedDataPack(String name) {
        return new JSONObject(sql.getText("content", "name", "=", name, "DatacrackDataPacks"));
    }

    @Override
    public JSONObject getDataPackSet(String name) {
        return null;
    }

    @Override
    public JSONObject getMapDataPack(String name) {
        return null;
    }

    @Override
    public boolean existsDataPack(String name) {
        return sql.exists("name", name, "DatacrackDataPacks");
    }

    @Override
    public boolean existsLinkedDataPack(String name) {
        return sql.exists("name", name, "DatacrackDataPacks");
    }

    @Override
    public boolean existsMapDataPack(String name) {
        return sql.exists("name", name, "DatacrackMaps");
    }

    @Override
    public void updateDataPack(DataPack dataPack) {
        if(existsDataPack(dataPack.getName())){
            sql.insertData("name, content", "'"+dataPack.name+"', '"+dataPack.toString()+"'", "DatacrackDataPacks");
        } else{
            sql.set("content", dataPack.toString(), "name", "=", dataPack.getName(), "DatacrackDataPacks");
        }
    }

    @Override
    public void updateLinkedDataPack(LinkedDataPack dataPack) {
        if(existsDataPack(dataPack.getName())){
            sql.insertData("name, content", "'"+dataPack.name+"', '"+dataPack.toString()+"'", "DatacrackDataPacks");
        } else{
            sql.set("content", dataPack.toString(), "name", "=", dataPack.getName(), "DatacrackDataPacks");
        }
    }

    @Override
    public void updateDataPackSet(String name, JSONObject json) {

    }

    @Override
    public void updateMapDataPack(MapDataPack dataPack) {
        JSONObject json = new JSONObject();
        json.put("datapackmap_name", dataPack.getName());
        json.put("datapackmap_lastestdate", System.currentTimeMillis());
        for(String key : dataPack.getMap().keySet()){
            json.put(key, dataPack.getMap().get(key));
        }
        if(existsMapDataPack(dataPack.getName())){
            sql.insertData("name, content", "'"+dataPack.getName()+"', '"+json.toString()+"'", "DatacrackMaps");
        } else{
            sql.set("content", json.toString(), "name", "=", dataPack.getName(), "DatacrackMaps");
        }
    }
}
