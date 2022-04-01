package org.affluentproductions.idlepokemon.util;

import org.affluentproductions.idlepokemon.IdlePokemon;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.TreeMap;

public class GuideUtil {

    private static HashMap<String, String> guides = new HashMap<>();

    public static void loadGuides() {
        try (ResultSet rs = IdlePokemon.getBot().getDatabase().query("SELECT * FROM guides;")) {
            while (rs.next()) {
                String gn = rs.getString("guideName");
                String gt = rs.getString("guideText");
                guides.put(gn.toLowerCase(), gt);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static TreeMap<String, String> getGuides() {
        return new TreeMap<>(guides);
    }
}