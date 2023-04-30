package balbucio.datacrack.V2.client.connector;

import balbucio.datacrack.V2.client.Datacrack;
import balbucio.datacrack.V2.client.data.DataPack;
import balbucio.datacrack.V2.client.data.DataPackSet;
import balbucio.datacrack.V2.client.data.LinkedDataPack;
import balbucio.datacrack.V2.client.data.MapDataPack;
import balbucio.datacrack.V2.common.accounts.Admin;
import balbucio.datacrack.V2.lib.configuration.file.YamlConfiguration;
import org.json.JSONObject;

import java.io.File;

public class InternalServer extends Server{

    private File file;
    private YamlConfiguration configuration;
    private Datacrack instance;

    public InternalServer(File file, Datacrack instance) {
        super();
        this.file = file;
        this.instance = instance;
    }

    public InternalServer(String fileName, Datacrack instance){
        super();
        this.file = new File("DatacrackInternal", fileName+".yml");
        this.instance = instance;
    }

    @Override
    public void start() {
        try {
            if (!file.exists()) {
                File folder = file.getParentFile();
                if (!folder.exists()) {
                    folder.mkdir();
                }
                file.createNewFile();
            }
            configuration = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public DataPack getOrCreateDataPack(String name) {
        if(configuration.contains(name)){
            return new DataPack(configuration.getString("datapack."+name), true, instance);
        } else{
            return new DataPack(name, instance);
        }
    }

    @Override
    public LinkedDataPack getOrCreateLinkedDataPack(String name) {
        if(configuration.contains(name)){
            return new LinkedDataPack(configuration.getString("datapack."+name), true, instance, this);
        } else{
            return new LinkedDataPack(name, instance, this);
        }
    }

    @Override
    public DataPackSet getOrCreateDataPackSet(String name) {
        if(configuration.contains(name)){
            return new DataPackSet(name, new JSONObject(configuration.getString("datapackset."+name)), instance);
        } else{
            return new DataPackSet(name, instance);
        }
    }

    @Override
    public MapDataPack getOrCreateMapDataPack(String name) {
        if(configuration.contains("maps."+name)){
            return new MapDataPack(new JSONObject(configuration.getString("maps."+name)), instance);
        } else{
            return new MapDataPack(name, instance);
        }
    }

    @Override
    public JSONObject getDataPack(String name) {
        return new JSONObject(configuration.getString("datapack."+name));
    }

    @Override
    public JSONObject getLinkedDataPack(String name) {
        return new JSONObject(configuration.getString("datapack."+name));
    }

    @Override
    public JSONObject getDataPackSet(String name) {
        return new JSONObject(configuration.getString("datapackset."+name));
    }

    @Override
    public JSONObject getMapDataPack(String name) {
        return new JSONObject(configuration.getString("maps."+name));
    }

    @Override
    public boolean existsDataPack(String name) {
        return configuration.contains("datapack."+name);
    }

    @Override
    public boolean existsLinkedDataPack(String name) {
        return configuration.contains("datapack."+name);
    }

    @Override
    public boolean existsMapDataPack(String name) {
        return configuration.contains("maps."+name);
    }

    @Override
    public void updateDataPack(DataPack dataPack) {
        try {
            configuration.set("datapack."+dataPack.getName(), dataPack.toString());
            configuration.save(file);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void updateLinkedDataPack(LinkedDataPack dataPack) {
        try {
            configuration.set("datapack."+dataPack.getName(), dataPack.toString());
            configuration.save(file);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void updateDataPackSet(String name, JSONObject json) {
        try{
            configuration.set("datapackset."+name, json.toString());
            configuration.save(file);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void updateMapDataPack(MapDataPack dataPack) {
        try {
            JSONObject json = new JSONObject();
            json.put("datapackmap_name", dataPack.getName());
            json.put("datapackmap_lastestdate", System.currentTimeMillis());
            for(String key : dataPack.getMap().keySet()){
                json.put(key, dataPack.getMap().get(key));
            }
            configuration.set("datapack."+dataPack.getName(), json.toString());
            configuration.save(file);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
