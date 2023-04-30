package balbucio.datacrack.V2.server.manager;

import balbucio.datacrack.V2.server.utils.JSONFile;
import org.json.JSONObject;

import java.io.File;

public class DataPackManager {

    public static String createOrGetDataPack(String name){
        JSONFile json = new JSONFile(new File("DataPacks", name+"_datapack.json"), name);
        return json.load().toString();
    }

    public static String updateDataPack(String name, JSONObject payload){
        JSONFile json = new JSONFile(new File("DataPacks", name+"_datapack.json"), name);
        json.save(payload.toString());
        return new JSONObject().toString();
    }

    public static String existsDataPack(String name){
        boolean exists = new File("DataPacks", name+"_datapack.json").exists();
        return new JSONObject().put("exists", exists).toString();
    }

    public static String createOrGetMapDataPack(String name){
        JSONFile json = new JSONFile(new File("DataPacks", name+"_mapdatapack.json"), name);
        return json.load().toString();
    }

    public static String updateMapDataPack(String name, JSONObject payload){
        JSONFile json = new JSONFile(new File("DataPacks", name+"_mapdatapack.json"), name);
        json.save(payload.toString());
        return new JSONObject().toString();
    }

    public static String existsMapDataPack(String name){
        boolean exists = new File("DataPacks", name+"_mapdatapack.json").exists();
        return new JSONObject().put("exists", exists).toString();
    }

    public static String createOrGetDataPackSet(String name){
        JSONFile json = new JSONFile(new File("DataPacks", name+"_datapackset.json"), name);
        return json.load().toString();
    }

    public static String updateDataPackSet(String name, JSONObject payload){
        JSONFile json = new JSONFile(new File("DataPacks", name+"_datapackset.json"), name);
        json.save(payload.toString());
        return new JSONObject().toString();
    }

    public static String existsDataPackSet(String name){
        boolean exists = new File("DataPacks", name+"_datapackset.json").exists();
        return new JSONObject().put("exists", exists).toString();
    }

}
