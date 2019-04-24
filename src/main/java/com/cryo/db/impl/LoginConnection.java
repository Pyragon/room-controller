package com.cryo.db.impl;

import com.cryo.Main;
import com.cryo.db.DBConnectionManager;
import com.cryo.db.DatabaseConnection;
import com.cryo.entities.SQLQuery;
import com.cryo.utils.BCrypt;

import java.sql.Timestamp;

public class LoginConnection extends DatabaseConnection {

    public LoginConnection() {
        super("led_controller");
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
            case "get-session":
                String session_id = (String) data[1];
                data = select("sessions", "session_id=? AND active=1", GET_SESSION, session_id);
                if(data == null) return null;
                Timestamp stamp = (Timestamp) data[0];
                if(stamp.getTime() <= System.currentTimeMillis()) {
                    set("sessions", "active=0", "session_id=?", session_id);
                    return null;
                }
                return new Object[] { stamp };
            case "set-session":
                username = (String) data[1];
                session_id = (String) data[2];
                long expires =  (long) data[3];
                insert("sessions", new Object[] { "DEFAULT", username, session_id, expires, 1 });
                break;
            case "remove-session":
                set("sessions", "active=0", "session_id=?", data[1]);
                break;
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
