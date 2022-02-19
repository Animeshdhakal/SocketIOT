package app.socketiot.server.core.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import app.socketiot.server.core.db.DB;
import app.socketiot.server.core.json.JsonParser;
import app.socketiot.server.core.json.model.BluePrintJson;
import app.socketiot.server.core.model.blueprint.BluePrint;

public class BluePrintDBDao {
    private final DB db;

    public BluePrintDBDao(DB db) {
        this.db = db;
    }

    public ConcurrentHashMap<String, BluePrint> getAllBluePrints() {
        ConcurrentHashMap<String, BluePrint> bluePrints = new ConcurrentHashMap<>();

        try (Connection connection = db.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM blueprints");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    bluePrints.put(rs.getString("id"),
                            new BluePrint(rs.getString("name"), rs.getString("email"), rs.getString("id"),
                                    JsonParser.parse(BluePrintJson.class, rs.getString("json"))));
                }
            }
        } catch (Exception e) {
        }
        return bluePrints;
    }

    public void saveAllBluePrints(ArrayList<BluePrint> blueprints) {
        try (Connection connection = db.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO blueprints (name, email, id, json) VALUES (?, ?, ?, ?) ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name, json = EXCLUDED.json");
            for (BluePrint bluePrint : blueprints) {
                stmt.setString(1, bluePrint.name);
                stmt.setString(2, bluePrint.email);
                stmt.setString(3, bluePrint.id);
                stmt.setString(4, JsonParser.toString(bluePrint.json));
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (Exception e) {
        }
    }

    public void deleteBluePrint(String blueprintId) {
        try (Connection connection = db.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM blueprints WHERE id = ?");
            stmt.setString(1, blueprintId);
            stmt.execute();
        } catch (Exception e) {
        }
    }
}
