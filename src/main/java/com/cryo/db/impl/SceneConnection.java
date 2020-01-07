package com.cryo.db.impl;

import com.cryo.Main;
import com.cryo.controllers.SceneController;
import com.cryo.db.DBConnectionManager;
import com.cryo.db.DatabaseConnection;
import com.cryo.entities.Effect;
import com.cryo.entities.SQLQuery;
import com.cryo.entities.Scene;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

public class SceneConnection extends DatabaseConnection {

    public SceneConnection() {
        super("room_controller");
    }

    public static SceneConnection connection() {
        return (SceneConnection) Main.getInstance().getConnectionManager().getConnection(DBConnectionManager.Connection.SCENE);
    }

    @Override
    public Object[] handleRequest(Object... data) {
        String opcode = (String) data[0];
        switch(opcode) {
            case "get-scenes":
                return select("scenes", GET_SCENES);
            case "get-scene-by-id":
                return select("scenes", "id=?", GET_SCENE, data[1]);
            case "get-scene-by-name":
                return select("scenes", "name LIKE ?", "LIMIT 1", GET_SCENE, data[1]);
        }
        return null;
    }

    private final SQLQuery GET_SCENES = set -> {
        ArrayList<Scene> scenes = new ArrayList<>();
        if(wasNull(set)) return new Object[] { scenes };
        while(next(set)) scenes.add(loadScene(set));
        return new Object[] { scenes };
    };

    private final SQLQuery GET_SCENE = set -> {
        if(empty(set)) return null;
        return new Object[] { loadScene(set) };
    };

    private Scene loadScene(ResultSet set) {
        int id = getInt(set, "id");
        String name = getString(set, "name");
        String effectsString = getString(set, "effects");
        Timestamp added = getTimestamp(set, "added");
        Timestamp updated = getTimestamp(set, "updated");
        Effect[] effects = Main.getGson().fromJson(effectsString, Effect[].class);
        return new Scene(id, name, effects, added, updated);
    }
}
