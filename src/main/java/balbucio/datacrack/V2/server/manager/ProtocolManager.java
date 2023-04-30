package balbucio.datacrack.V2.server.manager;

import balbucio.datacrack.V2.common.accounts.Admin;
import org.json.JSONObject;

public class ProtocolManager {

    public static String readProtocol(String method, Object payload) {
        try {
            JSONObject content = new JSONObject((String) payload);
            Admin admin = AdminManager.containsAdmin(content.getString("username")) ? AdminManager.getAdmin(content.getString("username")) : null;
            String password = content.getString("password");
            if (admin == null || !admin.getPassword().equals(password)) {
                return new JSONObject().put("error", true).put("message", "Credenciais incorretas!").toString();
            }
            String path = "root";
            JSONObject jsonContent;
            switch (method) {
                case "GETDATAPACK":
                    path = content.getString("path");
                    return DataPackManager.createOrGetDataPack(path);
                case "GETMAPDATAPACK":
                    path = content.getString("path");
                    return DataPackManager.createOrGetMapDataPack(path);
                case "GETDATAPACKSET":
                    path = content.getString("path");
                    return DataPackManager.createOrGetDataPackSet(path);
                case "UPDATEDATAPACK":
                    path = content.getString("path");
                    jsonContent = content.getJSONObject("jsonContent");
                    return DataPackManager.updateDataPack(path, jsonContent);
                case "UPDATEMAPDATAPACK":
                    path = content.getString("path");
                    jsonContent = content.getJSONObject("jsonContent");
                    return DataPackManager.updateMapDataPack(path, jsonContent);
                case "UPDATEDATAPACKSET":
                    path = content.getString("path");
                    jsonContent = content.getJSONObject("jsonContent");
                    return DataPackManager.updateDataPackSet(path, jsonContent);
                case "EXISTDATAPACK":
                    path = content.getString("path");
                    return DataPackManager.existsDataPack(path);
                case "EXISTMAPDATAPACK":
                    path = content.getString("path");
                    return DataPackManager.existsMapDataPack(path);
                case "EXISTDATAPACKSET":
                    path = content.getString("path");
                    return DataPackManager.existsDataPackSet(path);
                case "GETTEMPPACK":
                    path = content.getString("path");
                    return TempPackManager.getTempPack(path);
                case "UPDATETEMPPACK":
                    path = content.getString("path");
                    jsonContent = content.getJSONObject("jsonContent");
                    return TempPackManager.updateTempPack(path, jsonContent);
                case "EXISTTEMPPACK":
                    path = content.getString("path");
                    return TempPackManager.hasTempPack(path);
                default:
                    throw new Exception("Erro ao identificar e executar o método");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject().put("error", true).put("message", "Ocorreu um falha ao executar o request! Tente Novamente!").toString();
        }
    }

}
