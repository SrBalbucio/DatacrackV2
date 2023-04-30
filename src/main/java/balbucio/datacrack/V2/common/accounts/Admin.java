package balbucio.datacrack.V2.common.accounts;

import balbucio.datacrack.V2.common.enums.OperationType;

import java.util.ArrayList;
import java.util.List;

public class Admin {

    private String username;
    private String password;

    private List<Permission> permissions = new ArrayList<>();

    public Admin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addPermission(String permission){
        permissions.add(new Permission(permission));
    }

    public boolean hasPermission(String permission){
        return !permissions.stream().filter(p -> p.getPermission().equalsIgnoreCase(permission)).findFirst().isEmpty();
    }

    public void hasPermissionForDataPack(String datapack, OperationType type){
        if(hasPermission(type.getPermCheck(datapack))){

        }
    }
}
