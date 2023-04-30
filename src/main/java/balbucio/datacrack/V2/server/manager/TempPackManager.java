package balbucio.datacrack.V2.server.manager;

import org.json.JSONObject;

import java.util.HashMap;

public class TempPackManager {

    public static HashMap<String, JSONObject> temp = new HashMap<>();

    public static String getTempPack(String name){
        return temp.getOrDefault(name, new JSONObject()).toString();
    }

    public static String updateTempPack(String name, JSONObject json){
        if(temp.containsKey(name)){
            temp.replace(name, json);
        } else{
            temp.put(name, json);
        }
        return new JSONObject().toString();
    }

    public static String hasTempPack(String name){
        return new JSONObject().put("exists", temp.containsKey(name)).toString();
    }
}
