package balbucio.datacrack.V2.client.data;

import balbucio.datacrack.V2.client.Datacrack;
import org.json.JSONObject;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class MapDataPack {

    private ConcurrentHashMap<String, UniqueDataPack> maps = new ConcurrentHashMap<>();
    private Datacrack instance;
    private String name;
    private JSONObject payload;

    public MapDataPack(String name, Datacrack instance){
        this.instance = instance;
        this.name = name;
    }

    public MapDataPack(JSONObject payload, Datacrack instance){
        this.instance = instance;
        this.payload = payload;
        this.name = payload.getString("datapackmap_name");
        for(String key : payload.keySet()){
            if(!key.equals("datapackmap_name")){
                maps.put(key, new UniqueDataPack(key, payload.getJSONObject(key)));
            }
        }
    }

    /**
     * Adiciona um UniqueDataPack ao map, é necessário salvar para que seja enviado ao servidor
     * @param name
     * @param dataPack
     */
    public void put(String name, UniqueDataPack dataPack){
        maps.put(name, dataPack);
    }

    /**
     * Pega ou cria um UniqueDataPack, lembrando que se ele não existir é necessário adiciona-lo depois usando o método add
     * @param name
     * @return
     */
    public UniqueDataPack getOrCreate(String name){
        return maps.getOrDefault(name, new UniqueDataPack(name));
    }

    public void remove(String name){
        maps.remove(name);
    }

    public ConcurrentHashMap<String, UniqueDataPack> getMap(){
        return maps;
    }

    public Datacrack getInstance() {
        return instance;
    }

    public String getName() {
        return name;
    }

    public void save(){
        JSONObject json = new JSONObject();
        json.put("datapackmap_name", name);
        json.put("datapackmap_lastestdate", System.currentTimeMillis());
        for(String key : maps.keySet()){
            json.put(key, maps.get(key));
        }

    }
}
