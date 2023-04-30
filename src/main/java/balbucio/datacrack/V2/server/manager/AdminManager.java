package balbucio.datacrack.V2.server.manager;

import balbucio.datacrack.V2.common.accounts.Admin;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class AdminManager {

    private static CopyOnWriteArrayList<Admin> admins = new CopyOnWriteArrayList<>();

    public static void addAdmin(Admin admin){
        admins.add(admin);
    }

    public static boolean containsAdmin(String username){
        return !admins.stream().filter(u -> u.getUsername().equals(username)).collect(Collectors.toList()).isEmpty();
    }

    public static Admin getAdmin(String username){
        return admins.stream().filter(u -> u.getUsername().equals(username)).findAny().get();
    }


}
