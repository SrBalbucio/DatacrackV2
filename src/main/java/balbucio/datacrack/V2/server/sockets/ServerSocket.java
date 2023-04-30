package balbucio.datacrack.V2.server.sockets;

import balbucio.datacrack.V2.lib.procbridge.IDelegate;
import balbucio.datacrack.V2.lib.procbridge.ProtocolException;
import balbucio.datacrack.V2.lib.procbridge.SSLServer;
import balbucio.datacrack.V2.lib.procbridge.Server;
import balbucio.datacrack.V2.server.manager.ProtocolManager;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerSocket extends Thread{

    private int port;
    private boolean ssl;
    private Server server;

    public ServerSocket(int port, boolean ssl){
        super("Socket-"+port);
        this.port = port;
        this.ssl = ssl;
    }

    @Override
    public void run() {
        console("Iniciando Socket...");
        if(ssl) {
            this.server = new SSLServer(port, new Request());
        } else{
            this.server = new Server(port, new Request());
        }
        server.start();
        console("Socket iniciado e preparado para receber solicitações!");
    }

    public void console(String msg){
        String pattern = "MM-dd-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());

        String hpattern = "HH:mm:ss";
        SimpleDateFormat hsimpleDateFormat = new SimpleDateFormat(hpattern);
        String hour = hsimpleDateFormat.format(new Date());
        System.out.print("[SOCKET-"+port+"] [V2] ["+date+" "+hour+"] "+msg+"\n");
    }

    public class Request implements IDelegate{
        public Request(){}

        @Override
        public @Nullable Object handleRequest(@Nullable String method, @Nullable Object payload) {
            console("Um request "+method+" foi executado!");
            return ProtocolManager.readProtocol(method, payload);
        }
    }
}
