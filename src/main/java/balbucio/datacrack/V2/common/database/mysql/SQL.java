package balbucio.datacrack.V2.common.database.mysql;

import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;

public class SQL {

    private String host, user, password, database, port, driver, tlsVersion, ssl;

    public SQL(final String host, final String user, final String password, final String database, final String port, final String driver, final String tlsVersion, final String ssl){
        this.host = host;
        this.user = user;
        this.password = password;
        this.database = database;
        this.port = port;
        this.driver = driver;
        this.tlsVersion = tlsVersion;
        this.ssl = ssl;
    }
    private Connection con;

    public Connection getConnection() {
        return this.con;
    }

    public void connect() {
        if (host == null || user == null || password == null || database == null) {
            return;
        }

        if(!isConnected()) {
            try {
                if (driver.length() == 0) {
                } else {
                    this.con = DriverManager.getConnection("jdbc:" + driver + "://" + host + ":" + port + "/" + database + "?autoReconnect=true&maxReconnects=10" + ((tlsVersion != null && tlsVersion.length() > 0) ? ("&enabledTLSProtocols=TLSv" + tlsVersion) : "") + "&useSSL=" + ssl, user, password);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void disconnect() {
        disconnect(true);
    }

    private void disconnect(final boolean message) {
        try {
            if (isConnected()) {
                this.con.close();
            }
        }
        catch (Exception e) {
        }
        this.con = null;
    }

    public boolean isConnected() {
        if (this.con != null) {
            try {
                return !this.con.isClosed();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean update(final String command) {
        if (command == null) {
            return false;
        }
        boolean result = false;
        connect();
        try {
            if (this.con != null) {
                final Statement st = this.con.createStatement();
                st.executeUpdate(command);
                st.close();
                result = true;
            }
        } catch (Exception e) {
            final String message = e.getMessage();
        }
        disconnect(false);
        return result;
    }

    public ResultSet query(final String command) {
        if (command == null) {
            return null;
        }
        connect();
        ResultSet rs = null;
        try {
            if (this.con != null) {
                final Statement st = this.con.createStatement();
                rs = st.executeQuery(command);
            }
        }
        catch (Exception e) {
            final String message = e.getMessage();
        }
        return rs;
    }

    public boolean tableExists(final String table) {
        try {
            final Connection connection = this.getConnection();
            if (connection == null) {
                return false;
            }
            final DatabaseMetaData metadata = connection.getMetaData();
            if (metadata == null) {
                return false;
            }
            final ResultSet rs = metadata.getTables(null, null, table, null);
            if (rs.next()) {
                return true;
            }
        }
        catch (Exception ex) {}
        return false;
    }

    public boolean insertData(final String columns, final String values, final String table) {
        return this.update("INSERT INTO " + table + " (" + columns + ") VALUES (" + values + ");");
    }

    public boolean deleteData(final String column, final String logic_gate, String data, final String table) {
        if (data != null) {
            data = "'" + data + "'";
        }
        return this.update("DELETE FROM " + table + " WHERE " + column + logic_gate + data + ";");
    }

    public boolean exists(final String column, String data, final String table) {
        if (data != null) {
            data = "'" + data + "'";
        }
        try {
            final ResultSet rs = this.query("SELECT * FROM " + table + " WHERE " + column + "=" + data + ";");
            if (rs.next()) {
                return true;
            }
        }
        catch (Exception ex) {}
        return false;
    }

    public boolean deleteTable(final String table) {
        return this.update("DROP TABLE " + table + ";");
    }

    public boolean truncateTable(final String table) {
        return this.update("TRUNCATE TABLE " + table + ";");
    }

    public boolean createTable(final String table, final String columns) {
        return this.update("CREATE TABLE IF NOT EXISTS " + table + " (" + columns + ");");
    }

    public boolean upsert(final String selected, Object object, final String column, String data, final String table) {
        if (object != null) {
            object = "'" + object + "'";
        }
        if (data != null) {
            data = "'" + data + "'";
        }
        try {
            final ResultSet rs = this.query("SELECT * FROM " + table + " WHERE " + column + "=" + data + ";");
            if (rs.next()) {
                this.update("UPDATE " + table + " SET " + selected + "=" + object + " WHERE " + column + "=" + data + ";");
            }
            else {
                insertData(column + ", " + selected, data + ", " + object, table);
            }
        }
        catch (Exception ex) {}
        return false;
    }

    public boolean set(final String selected, Object object, final String column, final String logic_gate, String data, final String table) {
        if (object != null) {
            object = "'" + object + "'";
        }
        if (data != null) {
            data = "'" + data + "'";
        }
        return this.update("UPDATE " + table + " SET " + selected + "=" + object + " WHERE " + column + logic_gate + data + ";");
    }

    public boolean set(final String selected, Object object, final String[] where_arguments, final String table) {
        String arguments = "";
        for (final String argument : where_arguments) {
            arguments = arguments + argument + " AND ";
        }
        if (arguments.length() <= 5) {
            return false;
        }
        arguments = arguments.substring(0, arguments.length() - 5);
        if (object != null) {
            object = "'" + object + "'";
        }
        return this.update("UPDATE " + table + " SET " + selected + "=" + object + " WHERE " + arguments + ";");
    }

    public Object get(final String selected, final String[] where_arguments, final String table) {
        String arguments = "";
        for (final String argument : where_arguments) {
            arguments = arguments + argument + " AND ";
        }
        if (arguments.length() <= 5) {
            return false;
        }
        arguments = arguments.substring(0, arguments.length() - 5);
        try {
            final ResultSet rs = this.query("SELECT * FROM " + table + " WHERE " + arguments + ";");
            if (rs.next()) {
                return rs.getObject(selected);
            }
        }
        catch (Exception ex) {}
        return null;
    }

    public ArrayList<Object> listGet(final String selected, final String[] where_arguments, final String table) {
        final ArrayList<Object> array = new ArrayList<Object>();
        String arguments = "";
        for (final String argument : where_arguments) {
            arguments = arguments + argument + " AND ";
        }
        if (arguments.length() <= 5) {
            return array;
        }
        arguments = arguments.substring(0, arguments.length() - 5);
        try {
            final ResultSet rs = this.query("SELECT * FROM " + table + " WHERE " + arguments + ";");
            while (rs.next()) {
                array.add(rs.getObject(selected));
            }
        }
        catch (Exception ex) {}
        return array;
    }

    public Object get(final String selected, final String column, final String logic_gate, String data, final String table) {
        if (data != null) {
            data = "'" + data + "'";
        }
        try {
            final ResultSet rs = this.query("SELECT * FROM " + table + " WHERE " + column + logic_gate + data + ";");
            if (rs.next()) {
                return rs.getObject(selected);
            }
        }
        catch (Exception ex) {}
        return null;
    }

    public String getText(final String selected, final String column, final String logic_gate, String data, final String table) {
        if (data != null) {
            data = "'" + data + "'";
        }
        try {
            final ResultSet rs = this.query("SELECT * FROM " + table + " WHERE " + column + logic_gate + data + ";");
            if (rs.next()) {
                return rs.getString(selected);
            }
        }
        catch (Exception ex) {}
        return null;
    }

    public ArrayList<Object> listGet(final String selected, final String column, final String logic_gate, String data, final String table) {
        final ArrayList<Object> array = new ArrayList<Object>();
        if (data != null) {
            data = "'" + data + "'";
        }
        try {
            final ResultSet rs = this.query("SELECT * FROM " + table + " WHERE " + column + logic_gate + data + ";");
            while (rs.next()) {
                array.add(rs.getObject(selected));
            }
        }
        catch (Exception ex) {}
        return array;
    }

    public int countRows(final String table) {
        int i = 0;
        if (table == null) {
            return i;
        }
        final ResultSet rs = this.query("SELECT * FROM " + table + ";");
        try {
            while (rs.next()) {
                ++i;
            }
        }
        catch (Exception ex) {}
        return i;
    }

}
