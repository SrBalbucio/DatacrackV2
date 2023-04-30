package balbucio.datacrack.V2.common.accounts;

import balbucio.datacrack.V2.client.Datacrack;
import balbucio.datacrack.V2.client.connector.Server;
import balbucio.datacrack.V2.common.accounts.Admin;
import org.json.JSONObject;

public class Credential {

    private JSONObject json;

    public Credential(Admin admin){
        this.json = new JSONObject();
        json.put("username", admin.getUsername());
        json.put("password", admin.getPassword());
        json.put("version", Datacrack.version);
    }

    public Credential addParameter(String key, Object value){
        json.put(key, value); return this;
    }

    public String get(){
        return json.toString();
    }
}
