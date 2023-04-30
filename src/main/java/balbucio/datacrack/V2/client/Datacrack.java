package balbucio.datacrack.V2.client;

import balbucio.datacrack.V2.client.connector.*;
import balbucio.datacrack.V2.client.data.DataPackSet;
import balbucio.datacrack.V2.client.data.LinkedDataPack;
import balbucio.datacrack.V2.common.accounts.Admin;
import balbucio.datacrack.V2.client.data.DataPack;
import balbucio.datacrack.V2.common.database.sqlite.SQLite;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Datacrack {

    public static void main(String[] args) {
        Datacrack datacrack = new Datacrack();
        SQLiteServer server = datacrack.createSQLiteServer(new File("DatacrackInternal", "server.db"));
        LinkedDataPack datapack = server.getOrCreateLinkedDataPack("Tested");
    }

    public final static String version = "v2";

    public Admin defaultAdmin;
    private List<Server> servers = new ArrayList<>();

    public Datacrack(){

    }

    public void setDefaultAdmin(Admin admin){
        this.defaultAdmin = admin;
    }

    /**
     * Adicione um servidor Datacrack
     * @param server Servidor criado anteriormente
     */
    public void addServer(Server server){
        server.start();
        servers.add(server);
    }

    /**
     * Crie e adicione automaticamente um servidor Datacrack Nativo
     * @param ip
     * @param port
     * @param admin
     * @param ssl
     */
    public void addDatacrackServer(String ip, int port, Admin admin, boolean ssl){
        DatacrackServer server = new DatacrackServer(ip, port, admin, ssl, this);
        server.start();
        servers.add(server);
    }
    
    public void addInternalServer(File file){
        InternalServer server = new InternalServer(file, this);
        server.start();
        servers.add(server);
    }

    public void addInternalServer(String file){
        InternalServer server = new InternalServer(file, this);
        server.start();
        servers.add(server);
    }

    /**
     * Crie automaticamente um servidor Datacrack Nativo
     * @param ip
     * @param port
     * @param admin
     * @param ssl
     */
    public DatacrackServer createDatacrackServer(String ip, int port, Admin admin, boolean ssl){
        DatacrackServer server = new DatacrackServer(ip, port, admin, ssl, this);
        server.start();
        return server;
    }

    /**
     * Crie automaticamente um servidor Datacrack MySQL
     * @param ip
     * @param port
     * @param admin
     * @param database
     * @return
     */
    public MySQLServer createSQLServer(String ip, int port, Admin admin, String database){
        MySQLServer server = new MySQLServer(ip, port, admin, database, "mysql", "", "false", this);
        server.start();
        return server;
    }

    /**
     * Crie automaticamente um servidor Datacrack MySQL
     * @param ip
     * @param port
     * @param admin
     * @param database
     * @param driver
     * @param tlsVersion
     * @param ssl
     * @return
     */
    public MySQLServer createSQLServer(String ip, int port, Admin admin, String database, String driver, String tlsVersion, String ssl){
        MySQLServer server = new MySQLServer(ip, port, admin, database, driver, tlsVersion, ssl, this);
        server.start();
        return server;
    }

    public InternalServer createInternalServer(File file){
        InternalServer server = new InternalServer(file, this);
        server.start();
        return server;
    }

    public InternalServer createInternalServer(String file){
        InternalServer server = new InternalServer(file, this);
        server.start();
        return server;
    }

    /**
     * Crie automaticamente um servidor Datacrack SQLite
     * @param file
     */
    public SQLiteServer createSQLiteServer(String file){
        SQLiteServer server = new SQLiteServer(file, this);
        server.start();
        return server;
    }

    /**
     * Crie automaticamente um servidor Datacrack SQLite
     * @param file
     */
    public SQLiteServer createSQLiteServer(File file){
        SQLiteServer server = new SQLiteServer(file, this);
        server.start();
        return server;
    }

    /**
     * Procura e getta o DataPack mais recente encontrado nos servidores.
     * Lembrando que esse método retorna o DataPack normal, não o Linked
     * @param name
     * @return
     */
    public synchronized DataPack searchAndGetDataPack(String name){
        List<DataPack> dataPacks = new ArrayList<>();
        for(Server servers : getServers()){
            try {
                if(servers.existsDataPack(name)) {
                    dataPacks.add(servers.getOrCreateDataPack(name));
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        Date ls = null;
        DataPack dataPack = null;
        for(DataPack d : dataPacks){
            Date date = new Date(d.getLong("datapack_lastestdate"));
            if(ls == null){
                ls = date;
                dataPack = d;
            } else if(ls.before(date)){
                ls = date;
                dataPack = d;
            }
        }
        if(dataPack == null){
            dataPack = createOrGetDataPack(name);
        }
        return dataPack;
    }

    public synchronized DataPack createOrGetDataPack(String name){
        List<DataPack> dataPacks = new ArrayList<>();
        for(Server servers : getServers()) {
            try {
                dataPacks.add(servers.getOrCreateDataPack(name));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dataPacks.stream().findAny().get();
    }

    public synchronized List<DataPack> getDataPacks(String name){
        List<DataPack> dataPacks = new ArrayList<>();
        for(Server servers : getServers()) {
            try {
                dataPacks.add(servers.getOrCreateDataPack(name));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dataPacks;
    }

    /**
     * Verifica se existe um DataPack nos servidores
     * @param name
     * @return
     */
    public synchronized boolean existsDataPack(String name){
        boolean exists = false;
        for(Server server : getServers()){
            if(!exists && server.existsDataPack(name)){
                exists = true;
            }
        }
        return exists;
    }

    public Admin getDefaultAdmin(){
        return defaultAdmin;
    }

    public List<Server> getServers() {
        return servers;
    }
}
