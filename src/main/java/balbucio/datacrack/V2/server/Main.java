package balbucio.datacrack.V2.server;

import balbucio.datacrack.V2.common.accounts.Admin;
import balbucio.datacrack.V2.lib.configuration.ConfigurationSection;
import balbucio.datacrack.V2.lib.configuration.file.YamlConfiguration;
import balbucio.datacrack.V2.server.manager.AdminManager;
import balbucio.datacrack.V2.server.sockets.ServerSocket;

import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {

    private static File configFolder = new File("Configuration");
    private static File cacheFolder = new File("Cache");
    private static File cnf = new File(configFolder, "config.yml");
    private static File adm = new File(configFolder, "admins.yml");
    private static File sck = new File(configFolder, "sockets.yml");
    private static YamlConfiguration configuration, admins, sockts;

    private static List<ServerSocket> sockets = new ArrayList<>();

    private static Main instance;

    public static void main(String[] args) {
        instance = new Main();
    }

    public Main(){
        System.out.print("DATACRACK V2 - DESENVOLVIDO POR SR_BALBUCIO\n");
        System.out.print("DATACRACK V2 - https://balbucio.xyz\n");
        console("Instância criada com sucesso!");
        loadFiles();
        console("Configurações carregadas com sucesso!");
        loadAdmins();
        loadSockets();
        console("Sockets iniciados e configurados com sucesso!");
    }

    private void loadFiles(){
        try {
            if (!configFolder.exists()) {
                configFolder.mkdir();
            }
            if(!cacheFolder.exists()){
                cacheFolder.mkdir();
            }
            if (!cnf.exists()) {
                Files.copy(this.getClass().getResourceAsStream("/config.yml"), cnf.toPath());
            }
            if (!adm.exists()) {
                Files.copy(this.getClass().getResourceAsStream("/admins.yml"), adm.toPath());
            }
            if (!sck.exists()) {
                Files.copy(this.getClass().getResourceAsStream("/sockets.yml"), sck.toPath());
            }
            configuration = YamlConfiguration.loadConfiguration(cnf);
            admins = YamlConfiguration.loadConfiguration(adm);
            sockts = YamlConfiguration.loadConfiguration(sck);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private void loadAdmins(){
        ConfigurationSection section = admins.getConfigurationSection("admins");
        for(String key : section.getKeys(false)){
            Admin admin = new Admin(section.getString(key+".username"), section.getString(key+".password"));
            for(String perm : section.getStringList(key+".permissions")){
                admin.addPermission(perm);
            }
            AdminManager.addAdmin(admin);
        }
    }

    private void loadSockets(){
        for(String key : sockts.getKeys(false)){
            boolean isSSL = sockts.getBoolean(key+".ssl");
            ServerSocket socket = new ServerSocket(sockts.getInt(key+".port"), isSSL);
            socket.start();

        }
    }

    public static void console(String msg){
        String pattern = "MM-dd-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());

        String hpattern = "HH:mm:ss";
        SimpleDateFormat hsimpleDateFormat = new SimpleDateFormat(hpattern);
        String hour = hsimpleDateFormat.format(new Date());
        System.out.print("[DATACRACK V2] ["+date+" "+hour+"] "+msg+"\n");
    }
}
