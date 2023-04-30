package balbucio.datacrack.V2.server.utils;

import org.json.JSONObject;

import java.io.*;
import java.util.Date;

public class JSONFile {

    private File file;

    public JSONFile(File file, String name){
        this.file = file;
        try {
            if (!file.exists()) {
                File folder = file.getParentFile();
                if (!folder.exists()) {
                    folder.mkdir();
                }
                file.createNewFile();
                Date date = new Date();
                save(new JSONObject().put("datapack_name", name).put("datapack_lastestdate", date.getTime()).toString());
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public synchronized JSONObject load() {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }

            bufferedReader.close();
            inputStreamReader.close();
            fileInputStream.close();
            JSONObject json = new JSONObject(builder.toString());
            return json;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public synchronized void save(String jsonBytes) {
        try {
            if(file.exists()) {
                FileOutputStream outputStream = new FileOutputStream(file);
                outputStream.write(jsonBytes.getBytes());
                outputStream.flush();
                outputStream.close();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
