package com.cryo.db.impl;

import com.cryo.Main;
import com.cryo.db.DBConnectionManager;
import com.cryo.db.DatabaseConnection;
import com.cryo.entities.SQLQuery;
import com.cryo.utils.BCrypt;

import java.sql.Timestamp;

public class LoginConnection extends DatabaseConnection {

    public LoginConnection() {
        super("room_controller");
    }

    public static LoginConnection connection() {
        return (LoginConnection) Main.getInstance().getConnectionManager().getConnection(DBConnectionManager.Connection.LOGIN);
    }

    @Override
    public Object[] handleRequest(Object... data) {
        String opcode = (String) data[0];
        switch(opcode) {
            case "compare":
                String username = (String) data[1];
                String password = (String) data[2];
                data = select("users", "username=?", GET_SALT_AND_HASH, username);
                if(data == null) return null;
                String hash = (String) data[0];
                String salt = (String) data[1];
                String hashed = BCrypt.hashPassword(password, salt);
                if(!hash.equals(hashed)) return null;
                return new Object[] { true };
        }
        return null;
    }

    private final SQLQuery GET_SALT_AND_HASH = set ->  {
        if(wasNull(set)) return null;
        String hash = getString(set, "hash");
        String salt = getString(set, "salt");
        return new Object[] { hash, salt };
    };

    private final SQLQuery GET_SESSION = set -> {
        if(wasNull(set)) return null;
        Timestamp expires = getTimestamp(set, "expires");
        return new Object[] { expires };
    };
}
